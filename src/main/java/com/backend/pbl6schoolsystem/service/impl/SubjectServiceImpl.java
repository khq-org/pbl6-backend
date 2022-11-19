package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import com.backend.pbl6schoolsystem.model.entity.SubjectEntity;
import com.backend.pbl6schoolsystem.repository.dsl.SubjectDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SubjectRepository;
import com.backend.pbl6schoolsystem.request.subject.GetListSubjectRequest;
import com.backend.pbl6schoolsystem.response.subject.GetListSubjectResponse;
import com.backend.pbl6schoolsystem.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectDslRepository subjectDslRepository;

    @Override
    public GetListSubjectResponse getListSubject(GetListSubjectRequest request) {
        List<SubjectEntity> subjects = subjectDslRepository.listSubject(request);
        return GetListSubjectResponse.builder()
                .setSuccess(true)
                .setItems(subjects.stream()
                        .map(s -> SubjectDTO.builder()
                                .setSubjectId(s.getSubjectId())
                                .setSubject(s.getSubject())
                                .setCode(s.getCode())
                                .setDescription(s.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
