package me.alexisdev.fineriomicroservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.alexisdev.fineriomicroservice.models.Movement;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String keyToValueEntity(String key , ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(response.getBody());
        String value = node.path(key).asText();
        return value;
    }

    public static List<Movement> responseToMovements(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> empMap = objectMapper.readValue(response.getBody(), Map.class);

        return objectMapper.convertValue(empMap.get("data"),new TypeReference<List<Movement>>(){});
    }
}
