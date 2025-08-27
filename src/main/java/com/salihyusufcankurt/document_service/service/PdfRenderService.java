package com.salihyusufcankurt.document_service.service;

import com.salihyusufcankurt.document_service.dto.request.RenderRequest;
import com.salihyusufcankurt.document_service.dto.response.FileLink;
import com.salihyusufcankurt.document_service.dto.response.RenderResponse;
import com.salihyusufcankurt.document_service.infra.convert.DocxStamperAdapter;
import com.salihyusufcankurt.document_service.infra.templating.JodConverterAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class PdfRenderService {

    private final TemplateService templateService;
    private final DocxStamperAdapter stamper;
    private final JodConverterAdapter converter;
    private final StorageService storage;
    private final ModelEnricher modelEnricher;

    public RenderResponse renderPdf(RenderRequest req){
        String jobId = UUID.randomUUID().toString();

        byte[] tpl = templateService.load(req.templateId());
        Map<String,Object> model = modelEnricher.enrich(req.data());

        byte[] docx = stamper.fill(tpl, model);
        byte[] pdf  = converter.docxToPdf(docx);

        String url = storage.putAndSign(jobId + "/output.pdf", "application/pdf", pdf);
        return new RenderResponse(jobId, List.of(new FileLink("pdf", url)));
    }

    public RenderResponse renderBundle(RenderRequest req){
        String jobId = UUID.randomUUID().toString();

        byte[] tpl = templateService.load(req.templateId());
        Map<String,Object> model = modelEnricher.enrich(req.data());

        byte[] docx = stamper.fill(tpl, model);
        byte[] pdf  = converter.docxToPdf(docx);

        byte[] zip = zip(docx, pdf);
        String url = storage.putAndSign(jobId + "/bundle.zip", "application/zip", zip);
        return new RenderResponse(jobId, List.of(new FileLink("zip", url)));
    }

    private byte[] zip(byte[] docx, byte[] pdf){
        try (var out = new ByteArrayOutputStream(); var zos = new ZipOutputStream(out)){
            zos.putNextEntry(new ZipEntry("output.docx"));
            zos.write(docx);
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("output.pdf"));
            zos.write(pdf);
            zos.closeEntry();

            zos.finish();
            return out.toByteArray();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
