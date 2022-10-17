package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.UserMapper;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.jpa.RoleRepository;
import com.backend.pbl6schoolsystem.repository.jpa.UserRepository;
import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.request.user.UpdateUserRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.security.CustomUser;
import com.backend.pbl6schoolsystem.service.UserService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final String ADMIN = Constants.ADMIN_ROLE;
    private final String SCHOOL = Constants.SCHOOL_ROLE;
    private final String TEACHER = Constants.TEACHER_ROLE;
    private final String STUDENT = Constants.STUDENT_ROLE;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoResponse getInfoAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
        List<String> authorities = new ArrayList<>();
        authorities.addAll(Arrays.asList(Constants.LOG_IN, Constants.LOG_OUT,
                Constants.UPDATE_PERSONAL_INFORMATION, Constants.CHANGE_PASSWORD));
        switch (user.getRole().getRole()) {
            case ADMIN:
                authorities.addAll(Arrays.asList(Constants.MANAGE_SCHOOL, Constants.MANAGE_SCHOOL_ADMIN));
                break;
            case SCHOOL:
                authorities.addAll(Arrays.asList(Constants.MANAGE_TEACHER, Constants.MANAGE_STUDENT, Constants.SETUP_CALENDAR,
                        Constants.SETUP_INFORMATION_SCHOOL_YEAR));
                break;
            case TEACHER:
                authorities.addAll(Arrays.asList(Constants.MANAGE_STUDENT, Constants.SEE_CALENDAR, Constants.INPUT_SCORE));
                break;
            case STUDENT:
                authorities.addAll(Arrays.asList(Constants.SEE_SCORE, Constants.SEE_CALENDAR));
                break;
        }
        return UserInfoResponse.builder()
                .setSuccess(true)
                .setUser(UserMapper.entity2dto(user))
                .setAuthorities(authorities)
                .build();
    }

    @Override
    public OnlyIdResponse updateInfoAccount(Long userId, UpdateUserRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("FirstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("LastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getEmail())) {
            errors.put("Email", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getWorkingPosition())) {
            errors.put("WorkingPosition", ErrorCode.MISSING_VALUE.name());
        }

        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        UserEntity user = userRepository.findById(userId).get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(RequestUtil.blankIfNull(request.getPhone()));
        user.setStreet(RequestUtil.blankIfNull(request.getStreet()));
        user.setDistrict(RequestUtil.blankIfNull(request.getDistrict()));
        user.setCity(RequestUtil.blankIfNull(request.getCity()));
        user.setPlaceOfBirth(RequestUtil.blankIfNull(request.getPlaceOfBirth()));
        user.setRole(roleRepository.findById(request.getRoleId()).get());

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public NoContentResponse changePassword(Long userId, ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(request.getCurrentPassword())) {
            errors.put("CurrentPassword", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getNewPassword())) {
            errors.put("NewPassword", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getConfirmPassword())) {
            errors.put("ConfirmPassword", ErrorCode.MISSING_VALUE.name());
        }

        if (errors.isEmpty()) {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
            log.info(passwordEncoder.encode(request.getCurrentPassword()));
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                errors.put("CurrentPassword", ErrorCode.INVALID_VALUE.name());
            }
            if (!request.getConfirmPassword().equals(request.getNewPassword())) {
                errors.put("ConfirmPassword", ErrorCode.INVALID_VALUE.name());
            }
            if (errors.isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return NoContentResponse.builder()
                        .setSuccess(true)
                        .build();
            }
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        return NoContentResponse.builder()
                .setSuccess(false)
                .setErrorResponse(ErrorResponse.builder()
                        .setErrors(errors)
                        .build())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username invalid"));
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        List.of(user.getRole()).forEach(role -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return new CustomUser(user.getUsername(), user.getPassword(), simpleGrantedAuthorities, user.getUserId(), user.getRole().getRole(),
                user.getFirstName(), user.getLastName(), (user.getSchool() != null) ? user.getSchool().getSchoolId() : -1L,
                RequestUtil.blankIfNull(user.getStreet()), RequestUtil.blankIfNull(user.getDistrict()), RequestUtil.blankIfNull(user.getCity()),
                user.getCreatedDate() != null ? user.getCreatedDate() : new Timestamp(System.currentTimeMillis()));
    }
}

