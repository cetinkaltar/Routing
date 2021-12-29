package com.country.routing.service;

import com.country.routing.dto.CountryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataLoader {

    private final ObjectMapper objectMapper;

    @Value("${countries.url}")
    private String url;

    public List<CountryDto> getCountries() {
        try {
            return objectMapper.readValue(new URL(url), new TypeReference<List<CountryDto>>(){});
        } catch (IOException e) {
            throw new RuntimeException(String.format("unable to upload data", e.getMessage()));
        }
    }
}
