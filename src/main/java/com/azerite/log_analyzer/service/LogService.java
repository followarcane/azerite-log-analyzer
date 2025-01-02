package com.azerite.log_analyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class LogService {

    @Value("${warcraftlogs.client-id}")
    private String clientId;

    @Value("${warcraftlogs.client-secret}")
    private String clientSecret;

    @Value("${provider.warcraftlogs.token-uri}")
    private String tokenUri;

    @Value("${provider.warcraftlogs.client-uri}")
    private String clientUri;

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedCredentials);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, requestEntity, String.class);

        return extractAccessTokenFromResponse(response.getBody());
    }

    private String extractAccessTokenFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token from response: " + responseBody, e);
        }
    }

    public String getResponseFromAPI(String code, String additionalQueryPart) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        String graphqlQuery = "{ \"query\": \"query { reportData { report(code: \\\"" + code + "\\\") ";
        graphqlQuery += additionalQueryPart;
        graphqlQuery += " } }\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(graphqlQuery, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(clientUri, requestEntity, String.class);

        return response.getBody();
    }
}