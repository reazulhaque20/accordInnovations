package com.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public Map<String, Object> searchGoogleBooks(String query) {
        log.info("Calling Google Books API with query: {}", query);

        String url = "https://www.googleapis.com/books/v1/volumes";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", query)
                .queryParam("maxResults", 5);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            log.info("Google Books API call successful. Status: {}", response.getStatusCode());

            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling Google Books API: {}", e.getMessage(), e);

            // Fallback response
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("error", "Unable to fetch from external API");
            fallbackResponse.put("localMessage", "Please try again later");
            return fallbackResponse;
        }
    }

    public String callExternalApi(String url) {
        log.info("Calling external API: {}", url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("External API call successful. Status: {}", response.getStatusCode());

            // Return only first 500 characters to avoid large responses
            String body = response.getBody();
            return body != null && body.length() > 500 ?
                    body.substring(0, 500) + "..." : body;
        } catch (Exception e) {
            log.error("Error calling external API: {}", e.getMessage(), e);
            return "Error calling external API: " + e.getMessage();
        }
    }
}
