package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QTeacherClassEntity;
import com.backend.pbl6schoolsystem.model.entity.TeacherClassEntity;
import com.backend.pbl6schoolsystem.request.clazz.ListClassRequest;
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
public class TeacherClassDslRepository {
    private final QTeacherClassEntity teacherClass = QTeacherClassEntity.teacherClassEntity;
    private final JPAQueryFactory queryFactory;

    public List<TeacherClassEntity> listTeacherClass(ListClassRequest request, UserPrincipal principal) {
        JPAQuery<TeacherClassEntity> query = queryFactory.select(teacherClass)
                .from(teacherClass);
        if (principal.isTeacher()) {
            query.where(teacherClass.teacher.userId.eq(principal.getUserId()));
            query.where(teacherClass.isClassLeader.eq(Boolean.FALSE));
        } else {
            query.where(teacherClass.isClassLeader.eq(Boolean.TRUE));
        }
        query.where(teacherClass.clazz.school.schoolId.eq(request.getSchoolId()))
                .where(teacherClass.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
        if (StringUtils.hasText(request.getClazzName())) {
            query.where(teacherClass.clazz.clazz.containsIgnoreCase(request.getClazzName()));
        }
        if (request.getGradeId() > 0) {
            query.where(teacherClass.clazz.grade.gradeId.eq(request.getGradeId()));
        }
        query.leftJoin(teacherClass.clazz).fetch();
        query.leftJoin(teacherClass.teacher).fetch();
        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("name".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, teacherClass.clazz.clazz));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        return query.fetch();
    }
}
