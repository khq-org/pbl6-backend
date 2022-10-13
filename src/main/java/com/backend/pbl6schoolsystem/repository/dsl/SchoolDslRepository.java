package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QSchoolEntity;
import com.backend.pbl6schoolsystem.model.entity.QUserEntity;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
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
public class SchoolDslRepository {
    private final QSchoolEntity school = QSchoolEntity.schoolEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final JPAQueryFactory queryFactory;

    public List<SchoolEntity> getListSchool(ListSchoolRequest request) {
        JPAQuery<SchoolEntity> query = queryFactory.select(school)
                .from(school);
        if (StringUtils.hasText(request.getSchoolName())) {
            query.where(school.school.containsIgnoreCase(request.getSchoolName()));
        }
        if (StringUtils.hasText(request.getCity())) {
            query.where(school.city.containsIgnoreCase(request.getCity()));
        }
        if (StringUtils.hasText(request.getDistrict())) {
            query.where(school.district.containsIgnoreCase(request.getDistrict()));
        }
        if (StringUtils.hasText(request.getSchoolType())) {
            query.where(school.schoolType.containsIgnoreCase(request.getSchoolType()));
        }
        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("name".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, school.school));
        } else if ("city".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, school.city));
        } else if ("district".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, school.district));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);
        return query.fetch();
    }

}
