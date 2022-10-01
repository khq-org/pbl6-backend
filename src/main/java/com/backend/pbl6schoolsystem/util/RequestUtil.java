package com.backend.pbl6schoolsystem.util;

import com.backend.pbl6schoolsystem.request.PageRequest;
import org.springframework.util.StringUtils;

public class RequestUtil {
    public static int getPage(PageRequest pageRequest) {
        return pageRequest.getPage() < 1 ? 0 : pageRequest.getPage() - 1;
    }

    public static int getSize(PageRequest pageRequest) {
        return pageRequest.getSize() < 1 ? 20 : pageRequest.getSize();
    }

    public static int getTotalPages(Long totalItems, Integer size) {
        return (int) (totalItems + size - 1) / size;
    }

    public static String blankIfNull(String str) {
        return StringUtils.hasText(str) ? str : "";
    }

}
