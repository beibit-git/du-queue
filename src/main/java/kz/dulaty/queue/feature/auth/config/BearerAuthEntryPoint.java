package kz.dulaty.queue.feature.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.dulaty.queue.core.exception.BearerAuthError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BearerAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        BearerAuthError error = new BearerAuthError();
        error.setMessage(authException.getMessage());

        if ("/api/auth/sign-in".equals(request.getServletPath())) {
            response.setStatus(406);
        } else {
            response.setStatus(401);
        }

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
