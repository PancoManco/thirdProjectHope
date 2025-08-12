package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

import static exception.ErrorMessages.ParameterError.REQUIRED_FORM_FIELD_MISSING;
import static exception.ErrorMessages.ParameterError.STRING_TO_PARSE_IS_INVALID;

public final class RequestParameterUtil {

    private RequestParameterUtil() {
    }

    public static void validateParameters(String errorMessage, String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null || parameter.isBlank()) {
                throw new InvalidParameterException(errorMessage);
            }
        }
    }

    public static BigDecimal extractBigDecimal(String parameter) {
        validateParameters(REQUIRED_FORM_FIELD_MISSING, parameter); // todo ???
        parameter = parameter.replace(",", ".").replaceAll("\\s", "");
        BigDecimal number;
        try {
            number = new BigDecimal(parameter);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(STRING_TO_PARSE_IS_INVALID);
        }
        return number;

    }

    public static String extractTrimmedPath(HttpServletRequest request, String errorMessage) {
        String path = request.getPathInfo();
        String trimmedPath = path != null && path.startsWith("/") ? path.substring(1) : path;
        validateParameters(errorMessage, trimmedPath);
        return trimmedPath;
    }
}
