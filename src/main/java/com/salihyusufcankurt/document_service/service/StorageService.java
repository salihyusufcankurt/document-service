package com.salihyusufcankurt.document_service.service;

public interface StorageService {
    String putAndSign(String key, String contentType, byte[] content);
}