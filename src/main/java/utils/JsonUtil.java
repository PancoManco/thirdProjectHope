package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class JsonUtil {

    private static final String ERROR_MESSAGE_JSON_NAME = "message";
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static <T> String toJson(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Произошла ошибка при сериализации в JSON", e);
        }
    }


    public static String errorToJson(String errorMessage) {
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put(ERROR_MESSAGE_JSON_NAME, errorMessage);
        return jsonNode.toString();
    }

}