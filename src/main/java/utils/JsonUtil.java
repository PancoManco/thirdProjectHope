package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ERROR_MESSAGE_JSON_NAME = "message";
    public static void sendJsonResponse(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        objectMapper.writeValue(resp.getWriter(), data);
    }

    public static String errorToJson(String errorMessage) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put(ERROR_MESSAGE_JSON_NAME, errorMessage);
        return jsonNode.toString();
    }
}