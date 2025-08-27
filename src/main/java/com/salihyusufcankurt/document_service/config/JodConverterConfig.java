package com.salihyusufcankurt.document_service.config;


import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JodConverterConfig {

    // Dev: LibreOffice local kurulum
    @Bean(initMethod = "start", destroyMethod = "stop")
    public OfficeManager officeManager() {
        return LocalOfficeManager.builder()
                .officeHome("C:\\\\Program Files\\\\LibreOffice") // yolunu doğru gir
                .portNumbers(2002)
                .build();
    }

    @Bean
    public DocumentConverter documentConverter(OfficeManager officeManager) {
        return LocalConverter.make(officeManager);
    }
}