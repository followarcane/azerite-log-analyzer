package com.azerite.log_analyzer.service.converter;

import com.azerite.log_analyzer.dto.ReportActorDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportActorConverter {

    private final ObjectMapper objectMapper;

    public ReportActorConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ReportActorDTO> convert(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode actorsNode = findNode(jsonNode, "actors");

            return objectMapper.convertValue(actorsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, ReportActorDTO.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse actors from response: " + response, e);
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