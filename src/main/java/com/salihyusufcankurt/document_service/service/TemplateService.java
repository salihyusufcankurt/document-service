package com.salihyusufcankurt.document_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TemplateService {

    @Value("${templates.dir:}")
    private String templatesDir;

    public byte[] load(String templateId) {
        // 1) External dir
        if (templatesDir != null && !templatesDir.isBlank()) {
            Path p = Paths.get(templatesDir, templateId + ".docx");

            System.out.println("Using template: " + p.toAbsolutePath());

            if (Files.exists(p)) {
                try {
                    return Files.readAllBytes(p);
                } catch (IOException e) {
                    throw new RuntimeException("Template read failed: " + p, e);
                }
            }
        }

        // 2) Classpath fallback
        try (InputStream in = getClass().getResourceAsStream("/templates/" + templateId + ".docx")) {
            if (in == null) {
                throw new RuntimeException("Template not found: " + templateId);
            }
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}