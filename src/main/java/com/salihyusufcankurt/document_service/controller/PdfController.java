package com.salihyusufcankurt.document_service.controller;

import com.salihyusufcankurt.document_service.dto.request.RenderRequest;
import com.salihyusufcankurt.document_service.dto.response.RenderResponse;
import com.salihyusufcankurt.document_service.service.PdfRenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// ---- PDF & ZIP ----
@RestController
@RequestMapping("/v1/documents/pdf")
@RequiredArgsConstructor
class PdfController {
    private final PdfRenderService service;
    @PostMapping("/render")
    public ResponseEntity<RenderResponse> renderPdf(@Valid @RequestBody RenderRequest req){
        System.out.println("[REQ] templateId=" + req.templateId());
        System.out.println("[REQ] data keys=" + req.data().keySet());
        Object cust = req.data().get("customer");
        System.out.println("[REQ] customer=" + cust + " (" + (cust==null? "null" : cust.getClass().getName()) + ")");
        Object items = req.data().get("items");
        System.out.println("[REQ] items=" + (items==null? "null" : items.getClass().getName()));
        Object total = req.data().get("total");
        System.out.println("[REQ] total=" + total + " (" + (total==null? "null" : total.getClass().getName()) + ")");

        return ResponseEntity.ok(service.renderPdf(req));
    }
    @PostMapping("/bundle")
    public ResponseEntity<RenderResponse> renderBundle(@Valid @RequestBody RenderRequest req){
        return ResponseEntity.ok(service.renderBundle(req));
    }
}
