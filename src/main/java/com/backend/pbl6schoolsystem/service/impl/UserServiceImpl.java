package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.UserMapper;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.jpa.UserRepository;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.security.CustomUser;
import com.backend.pbl6schoolsystem.service.UserService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final String ADMIN = Constants.ADMIN_ROLE;
    private final String SCHOOL = Constants.SCHOOL_ROLE;
    private final String TEACHER = Constants.TEACHER_ROLE;
    private final String STUDENT = Constants.STUDENT_ROLE;

    @Override
    public UserInfoResponse getInfoAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
        List<String> authorities = List.of(Constants.LOG_IN, Constants.LOG_OUT,
                Constants.UPDATE_PERSONAL_INFORMATION, Constants.CHANGE_PASSWORD);
        switch (user.getRole().getRole()) {
            case ADMIN:
                authorities.addAll(List.of(Constants.MANAGE_SCHOOL, Constants.MANAGE_SCHOOL_ADMIN));
                break;
            case SCHOOL:
                authorities.addAll(List.of(Constants.MANAGE_TEACHER, Constants.MANAGE_STUDENT, Constants.SETUP_CALENDAR,
                        Constants.SETUP_INFORMATION_SCHOOL_YEAR));
                break;
            case TEACHER:
                authorities.addAll(List.of(Constants.MANAGE_STUDENT, Constants.SEE_CALENDAR, Constants.INPUT_SCORE));
                break;
            case STUDENT:
                authorities.addAll(List.of(Constants.SEE_SCORE, Constants.SEE_CALENDAR));
                break;
        }
        return UserInfoResponse.builder()
                .setSuccess(true)
                .setUser(UserMapper.entity2dto(user))
                .setAuthorities(authorities)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("Not found username " + username);
        } else {
            Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
            List.of(user.getRole()).forEach(role -> {
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            return new CustomUser(user.getUsername(), user.getPassword(), simpleGrantedAuthorities, user.getUserId(), user.getRole().getRole()
                    , user.getFirstName(), user.getLastName(), user.getSchool().getSchoolId(), RequestUtil.blankIfNull(user.getStreet()), RequestUtil.blankIfNull(user.getDistrict()),
                    RequestUtil.blankIfNull(user.getCity()), user.getCreatedDate() != null ? user.getCreatedDate() : new Timestamp(System.currentTimeMillis()));
        }
    }
}

