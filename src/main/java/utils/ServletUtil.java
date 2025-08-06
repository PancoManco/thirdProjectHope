package utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public final class ServletUtil {
    private ServletUtil() {
    }

    public static void sendResponse(HttpServletResponse response, int status, String responseMessage) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.println(responseMessage);
    }
}
