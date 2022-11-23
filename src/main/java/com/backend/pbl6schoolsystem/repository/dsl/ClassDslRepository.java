package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.QClassEntity;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.request.clazz.ListClassRequest;
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
public class ClassDslRepository {
    private final QClassEntity clazz = QClassEntity.classEntity;
    private final JPAQueryFactory queryFactory;

    public List<ClassEntity> getListClass(ListClassRequest request) {
        JPAQuery<ClassEntity> query = queryFactory.select(clazz)
                .from(clazz)
                .where(clazz.school.schoolId.eq(request.getSchoolId()));
        if (StringUtils.hasText(request.getClazzName())) {
            query.where(clazz.clazz.containsIgnoreCase(request.getClazzName()));
        }
        if (request.getGradeId() > 0) {
            query.where(clazz.grade.gradeId.eq(request.getGradeId()));
        }
        query.leftJoin(clazz.grade).fetch();
        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("name".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, clazz.clazz));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        return query.fetch();
    }
}
