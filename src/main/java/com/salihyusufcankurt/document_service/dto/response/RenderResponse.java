package com.salihyusufcankurt.document_service.dto.response;

import java.util.List;


public record RenderResponse(String jobId, java.util.List<FileLink> files) {}