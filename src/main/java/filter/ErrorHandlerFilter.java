package filter;

import exception.AlreadyExistsException;
import exception.DBException;
import exception.EmptyException;
import exception.NotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.InvalidParameterException;

import static utils.JsonUtil.errorToJson;
import static utils.ServletUtil.sendResponse;

@WebFilter("/*")
public class ErrorHandlerFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (EmptyException | InvalidParameterException e) {
            handleException(res, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DBException e) {
            handleException(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (NotFoundException e) {
            handleException(res, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (AlreadyExistsException e) {
            handleException(res, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }

    }

    private void handleException(HttpServletResponse res, int statusCode, String message) throws IOException {
        sendResponse(res, statusCode, errorToJson(message));
    }

}
