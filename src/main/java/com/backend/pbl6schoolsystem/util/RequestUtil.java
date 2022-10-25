package com.backend.pbl6schoolsystem.util;

import com.backend.pbl6schoolsystem.request.PageRequest;
import org.springframework.util.StringUtils;

public class RequestUtil {
    public static int getPage(Integer page) {
        return page < 1 ? 0 : page - 1;
    }

    public static int getSize(Integer size) {
        return size < 1 ? 20 : size;
    }

    public static int getTotalPages(Long totalItems, Integer size) {
        return (int) (totalItems + size - 1) / size;
    }

    public static String blankIfNull(String str) {
        return StringUtils.hasText(str) ? str : "";
    }

    public static String defaultIfNull(String str, String df) {
        return StringUtils.hasText(str) ? str : df;
    }

    public static Long defaultIfNull(Long id, Long df) {
        return id == null ? df : id;
    }


    public static Integer defaultIfNull(Integer id, Integer df) {
        return id == null ? df : id;
    }
}
