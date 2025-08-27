package com.salihyusufcankurt.document_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;


public record RenderRequest(
        @NotBlank String templateId,
        @NotNull Map<String, Object> data,
        String locale,
        Map<String, String> formatting
) {}