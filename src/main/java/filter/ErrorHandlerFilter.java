package filter;

import exception.EmptyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JsonUtil;
import utils.ServletUtil;

import java.io.IOException;
import java.security.InvalidParameterException;

import static utils.JsonUtil.errorToJson;
import static utils.ServletUtil.sendResponse;

@WebFilter("/*")
public class ErrorHandlerFilter  extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        }
        catch (EmptyException | InvalidParameterException e) {
           handleException(res,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }

    }
    private void handleException(HttpServletResponse res, int statusCode, String message) throws IOException {
        sendResponse(res, statusCode, errorToJson(message));
    }

}
