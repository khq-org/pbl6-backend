package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.model.entity.QUserEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
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
public class StudentDslRepository {
    private final QUserEntity user = QUserEntity.userEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserEntity> getListStudent(ListStudentRequest request) {
        JPAQuery<UserEntity> query = queryFactory.select(user)
                .from(user)
                .where(user.role.roleId.eq(UserRole.STUDENT_ROLE.getRoleId()));
        if (StringUtils.hasText(request.getSearch())) {
            query.where(user.firstName.containsIgnoreCase(request.getSearch())
                    .or(user.lastName.containsIgnoreCase(request.getSearch()))
                    .or(user.middleName.containsIgnoreCase(request.getSearch())));
        }
        if (StringUtils.hasText(request.getLastName())) {
            query.where(user.lastName.containsIgnoreCase(request.getLastName()));
        }
        if (StringUtils.hasText(request.getFirstName())) {
            query.where(user.firstName.containsIgnoreCase(request.getFirstName()));
        }
        if (StringUtils.hasText(request.getDistrict())) {
            query.where(user.district.equalsIgnoreCase(request.getDistrict()));
        }
        if (StringUtils.hasText(request.getCity())) {
            query.where(user.city.equalsIgnoreCase(request.getCity()));
        }
        if (request.getSchoolId() > 0) {
            query.where(user.school.schoolId.eq(request.getSchoolId()));
        }
        if (request.getGradeId() > 0) {
            query.where(user.clazz.grade.gradeId.eq(request.getGradeId()));
        }
        if (request.getClassId() > 0) {
            query.where(user.clazz.classId.eq(request.getClassId()));
        }
        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("lastName".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, user.lastName));
        } else if ("studentId".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, user.userId));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        query.leftJoin(user.school).fetch();
        query.leftJoin(user.clazz).fetch();
        query.leftJoin(user.status).fetch();
        return query.fetch();
    }
}
