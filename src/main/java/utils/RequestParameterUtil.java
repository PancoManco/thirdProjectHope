package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.security.InvalidParameterException;

import static exception.ErrorMessages.ParameterError.PARAMETER_CANNOT_BE_NULL_OR_EMPTY;

public class RequestParameterUtil {


    public static boolean validateParameters(String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null || parameter.isBlank()) {
                throw new InvalidParameterException(PARAMETER_CANNOT_BE_NULL_OR_EMPTY);
            }
        }
        return true;
    }
    public static String extractTrimmedPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        String trimmedPath = path.startsWith("/") ? path.substring(1) : path;
        if (trimmedPath.isBlank()) {
            throw new InvalidParameterException(PARAMETER_CANNOT_BE_NULL_OR_EMPTY);
        }
        return trimmedPath;
    }
}
