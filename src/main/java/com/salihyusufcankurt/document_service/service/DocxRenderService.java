package com.salihyusufcankurt.document_service.service;

import com.salihyusufcankurt.document_service.dto.request.RenderRequest;
import com.salihyusufcankurt.document_service.dto.response.FileLink;
import com.salihyusufcankurt.document_service.dto.response.RenderResponse;
import com.salihyusufcankurt.document_service.infra.convert.DocxStamperAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocxRenderService {

    private final TemplateService templateService;
    private final DocxStamperAdapter stamper;
    private final StorageService storage;
    private final ModelEnricher modelEnricher;

    public RenderResponse render(RenderRequest req){
        String jobId = UUID.randomUUID().toString();

        byte[] tpl = templateService.load(req.templateId());
        Map<String,Object> model = modelEnricher.enrich(req.data());

        byte[] docx = stamper.fill(tpl, model);

        String url = storage.putAndSign(
                jobId + "/output.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                docx
        );
        return new RenderResponse(jobId, List.of(new FileLink("docx", url)));
    }
}
