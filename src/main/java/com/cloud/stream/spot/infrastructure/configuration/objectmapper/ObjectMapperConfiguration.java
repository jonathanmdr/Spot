package com.cloud.stream.spot.infrastructure.configuration.objectmapper;

import com.cloud.stream.spot.infrastructure.configuration.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }

}
