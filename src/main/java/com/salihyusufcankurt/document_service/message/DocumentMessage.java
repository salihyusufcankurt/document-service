package com.salihyusufcankurt.document_service.message;

public enum DocumentMessage {
    RENDERED("Document rendered successfully."),
    UPLOADED("Files uploaded successfully."),
    TEMPLATE_NOT_FOUND("Template not found.");
    private final String message;
    DocumentMessage(String m){ this.message = m; }
    public String getMessage(){ return message; }
}