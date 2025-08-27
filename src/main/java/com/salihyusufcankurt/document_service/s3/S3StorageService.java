package com.salihyusufcankurt.document_service.s3;

import com.salihyusufcankurt.document_service.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


import java.time.Duration;


@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    private final S3Client s3;
    private final S3Presigner presigner;
    private final S3Props props;


    @Override
    public String putAndSign(String key, String contentType, byte[] content){
        s3.putObject(b -> b.bucket(props.getBucket()).key(key).contentType(contentType), RequestBody.fromBytes(content));
        var get = GetObjectRequest.builder().bucket(props.getBucket()).key(key).build();
        var url = presigner.presignGetObject(p -> p.signatureDuration(Duration.ofMinutes(60)).getObjectRequest(get)).url().toString();
        return url;
    }
}