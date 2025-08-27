package com.salihyusufcankurt.document_service.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


import java.net.URI;


@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final S3Props p;


    @Bean
    S3Client s3Client(){
        var cfg = S3Configuration.builder().pathStyleAccessEnabled(p.isPathStyleAccess()).build();
        var builder = S3Client.builder().region(Region.of(p.getRegion())).serviceConfiguration(cfg)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(p.getAccessKey(), p.getSecretKey())));
        if (p.getEndpointOverride()!=null && !p.getEndpointOverride().isBlank()) builder = builder.endpointOverride(URI.create(p.getEndpointOverride()));
        return builder.build();
    }


    @Bean
    S3Presigner s3Presigner(){
        var builder = S3Presigner.builder().region(Region.of(p.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(p.getAccessKey(), p.getSecretKey())));
        if (p.getEndpointOverride()!=null && !p.getEndpointOverride().isBlank()) builder = builder.endpointOverride(URI.create(p.getEndpointOverride()));
        return builder.build();
    }
}