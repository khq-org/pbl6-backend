package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.model.entity.QStudentClazzEntity;
import com.backend.pbl6schoolsystem.model.entity.QUserEntity;
import com.backend.pbl6schoolsystem.model.entity.StudentClazzEntity;
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
    private final QStudentClazzEntity studentClazz = QStudentClazzEntity.studentClazzEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserEntity> getListStudent(ListStudentRequest request, Long schoolId) {
        JPAQuery<UserEntity> query = queryFactory.select(user)
                .from(user)
                .where(user.school.schoolId.eq(schoolId))
                .where(user.role.roleId.eq(UserRole.STUDENT_ROLE.getRoleId()));
        if (StringUtils.hasText(request.getSearch())) {
            query.where(user.firstName.containsIgnoreCase(request.getSearch())
                    .or(user.lastName.containsIgnoreCase(request.getSearch())));
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

        if (request.getClassId() > 0 && request.getSchoolYearId() > 0) {
            JPAQuery<Long> studentClazzIds = queryFactory.select(studentClazz.student.userId)
                    .from(studentClazz)
                    .where(studentClazz.clazz.classId.eq(request.getClassId()))
                    .where(studentClazz.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
            query.where(user.userId.in(studentClazzIds));
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
        } else if ("studentId".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, user.userId));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        query.leftJoin(user.school).fetch();
        return query.fetch();
    }
}
