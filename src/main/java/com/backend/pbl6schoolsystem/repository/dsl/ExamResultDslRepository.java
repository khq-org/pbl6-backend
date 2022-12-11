package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.enums.Semester;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultClassRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultStudentRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExamResultDslRepository {
    private final QExamResultEntity result = QExamResultEntity.examResultEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final QStudentClazzEntity studentClazz = QStudentClazzEntity.studentClazzEntity;
    private final JPAQueryFactory queryFactory;

    public List<ExamResultEntity> listExamResult(LoadExamResultStudentRequest request) {
        JPAQuery<ExamResultEntity> query = queryFactory.select(result)
                .from(result)
                .where(result.subject.subjectId.eq(request.getSubjectId()))
                .where(result.student.userId.eq(request.getStudentId()))
                .where(result.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
        if (List.of(Semester.SEMESTER_I.getId(), Semester.SEMESTER_II.getId()).contains(request.getSemesterId())) {
            query.where(result.semester.semesterId.eq(request.getSemesterId()));
        }
        return query.fetch();
    }
}
