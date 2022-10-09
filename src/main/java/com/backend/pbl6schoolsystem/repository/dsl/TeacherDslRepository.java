package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDslRepository {
    private QUserEntity user = QUserEntity.userEntity;

}
