package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.clazz.CreateUpdateClassRequest;
import com.backend.pbl6schoolsystem.request.clazz.ListClassRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.clazz.GetClassResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;

public interface ClazzService {
    ListClassResponse getListClass(ListClassRequest request);

    GetClassResponse getClass(Long clazzId);

    OnlyIdResponse createClass(CreateUpdateClassRequest request);

    OnlyIdResponse updateClass(Long clazzId, CreateUpdateClassRequest request);

    NoContentResponse deleteClass(Long clazzId);
}
