package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        validateParameters(REQUIRED_FORM_FIELD_MISSING, parameter);
        parameter = parameter.replace(",", ".").replaceAll("\\s", "");
        BigDecimal number;
        try {
            number = new BigDecimal(parameter);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(STRING_TO_PARSE_IS_INVALID);
        }
        return number;

    }

    public static String extractValidatedPath(HttpServletRequest request, String errorMessage) {
        String path = request.getPathInfo();
        String trimmedPath = path != null && path.startsWith("/") ? path.substring(1) : path;
        validateParameters(errorMessage, trimmedPath);
        return trimmedPath;
    }

    public static String getRateParameter(HttpServletRequest req) throws IOException {
        String stringRate = "";
        BufferedReader reader = req.getReader();
        StringBuilder formBody = new StringBuilder();

        while (reader.ready()) {
            formBody.append(reader.readLine());
        }
        Pattern pattern = Pattern.compile("rate=(.*?)(&|$)");
        Matcher matcher = pattern.matcher(formBody.toString());
        if (matcher.find()) {
            stringRate = matcher.group(1);
        }

        stringRate = stringRate.replace(',', '.');

        stringRate = stringRate.replaceAll("[^\\d.-]", "");

        return stringRate.trim();
    }
}
