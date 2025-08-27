package com.salihyusufcankurt.document_service.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "storage.s3")
public class S3Props {
    private String bucket;
    private String region;
    private String endpointOverride;
    private boolean pathStyleAccess;
    private String accessKey;
    private String secretKey;
    private String publicBaseUrl; // optional
}