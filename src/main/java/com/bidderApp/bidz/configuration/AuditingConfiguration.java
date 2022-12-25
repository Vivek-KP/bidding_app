package com.bidderApp.bidz.configuration;

import com.bidderApp.bidz.implementation.AuditorAwareImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


// Configuration for auditing services
@Configuration
@EnableMongoAuditing
public class AuditingConfiguration {
    @Bean
    public AuditorAware<String> myAuditorProvider() {
        return new AuditorAwareImplementation();
    }
}
