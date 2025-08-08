package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

import static exception.ErrorMessages.ParameterError.*;

public class RequestParameterUtil {


    public static boolean validateParameters(String errorMessage,String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null || parameter.isBlank()) {
                throw new InvalidParameterException(errorMessage);
            }
        }
        return true;
    }
    public static BigDecimal extractBigDecimal(String parameter) {
        validateParameters(REQUIRED_FORM_FIELD_MISSING, parameter);
        parameter = parameter.replace(",", ".").replaceAll("\\s", "");
        BigDecimal number;
        try {
            number = new BigDecimal(parameter);
        } catch (Exception e) {
            throw new InvalidParameterException(STRING_TO_PARSE_IS_INVALID);
        }
        return number;
    }
    public static String extractTrimmedPath(HttpServletRequest request,String errorMessage) {
        String path = request.getPathInfo();
        String trimmedPath = path.startsWith("/") ? path.substring(1) : path;
        if (trimmedPath.isBlank()) {
            throw new InvalidParameterException(errorMessage);
        }
        return trimmedPath;
    }
}
