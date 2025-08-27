package com.salihyusufcankurt.document_service.infra.templating;

import lombok.RequiredArgsConstructor;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
@RequiredArgsConstructor
public class JodConverterAdapter {

    private final DocumentConverter converter;

    public byte[] docxToPdf(byte[] docx) {
        try (var in = new ByteArrayInputStream(docx);
             var out = new ByteArrayOutputStream()) {

            converter.convert(in)
                    .as(DefaultDocumentFormatRegistry.DOCX)   // kaynak
                    .to(out)                                  // çıktı stream'i
                    .as(DefaultDocumentFormatRegistry.PDF)    // hedef
                    .execute();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF conversion failed", e);
        }
    }
}