package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.model.entity.QTeacherClassEntity;
import com.backend.pbl6schoolsystem.model.entity.QUserEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.request.teacher.ListTeacherRequest;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeacherDslRepository {
    private final QUserEntity user = QUserEntity.userEntity;
    private final QTeacherClassEntity teacherClass = QTeacherClassEntity.teacherClassEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserEntity> getListTeacherInSchool(ListTeacherRequest request, UserPrincipal principal) {
        JPAQuery<UserEntity> query = queryFactory.select(user)
                .from(user)
                .where(user.school.schoolId.eq(principal.getSchoolId()))
                .where(user.role.roleId.eq(UserRole.TEACHER_ROLE.getRoleId()));
        if (StringUtils.hasText(request.getSearch())) {
            query.where(user.lastName.containsIgnoreCase(request.getSearch())
                    .or(user.firstName.containsIgnoreCase(request.getSearch())));
        }
        if (StringUtils.hasText(request.getFirstName())) {
            query.where(user.firstName.containsIgnoreCase(request.getFirstName()));
        }
        if (StringUtils.hasText(request.getLastName())) {
            query.where(user.lastName.containsIgnoreCase(request.getLastName()));
        }
        if (StringUtils.hasText(request.getDistrict())) {
            query.where(user.district.containsIgnoreCase(request.getDistrict()));
        }
        if (request.getSubjectId() != null && request.getSubjectId() > 0) {
            query.where(user.subject.subjectId.eq(request.getSubjectId()));
        }
        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("lastName".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, user.lastName));
        } else if ("firstName".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, user.firstName));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        query.leftJoin(user.school).fetch();
        return query.fetch();
    }
}
