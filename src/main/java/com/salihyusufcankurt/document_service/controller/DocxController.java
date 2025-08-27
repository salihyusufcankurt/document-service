package com.salihyusufcankurt.document_service.controller;

import com.salihyusufcankurt.document_service.dto.request.RenderRequest;
import com.salihyusufcankurt.document_service.dto.response.RenderResponse;
import com.salihyusufcankurt.document_service.service.DocxRenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// ---- DOCX ----
@RestController
@RequestMapping("/v1/documents/docx")
@RequiredArgsConstructor
public class DocxController {
    private final DocxRenderService service;
    @PostMapping("/render")
    public ResponseEntity<RenderResponse> renderDocx(@Valid @RequestBody RenderRequest req){
        return ResponseEntity.ok(service.render(req));
    }

}