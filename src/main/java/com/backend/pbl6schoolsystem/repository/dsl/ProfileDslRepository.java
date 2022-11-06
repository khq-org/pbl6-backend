package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QProfileStudentEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileDslRepository {
    private QProfileStudentEntity profileStudent = QProfileStudentEntity.profileStudentEntity;
    private JPAQueryFactory queryFactory;
}
