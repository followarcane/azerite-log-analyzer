package com.azerite.log_analyzer.service.converter;

import com.azerite.log_analyzer.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportConverter {

    private final ObjectMapper objectMapper;

    public ReportConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List<T> convert(String response, String targetKey, Class<T> targetClass) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode targetNode = findNode(jsonNode, targetKey);

            return objectMapper.convertValue(targetNode, objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass));
        } catch (Exception e) {
            throw new ApiException("Failed to parse actors from response: " + response);
        }
    }

    private JsonNode findNode(JsonNode rootNode, String targetKey) {
        if (rootNode.has(targetKey)) {
            return rootNode.get(targetKey);
        }
        for (JsonNode childNode : rootNode) {
            JsonNode result = findNode(childNode, targetKey);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}