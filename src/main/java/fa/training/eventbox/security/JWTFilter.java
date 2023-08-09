package fa.training.eventbox.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjAxQGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9DVVNUT01FUiIsImV4cCI6MTY4MzUzODY5MX0.NkZtQdBz3j8u0eo9kjad0clNpdC6WFvIQWi7_ETEeGhirmw-BL8-fj5Zk2Qt67fwjDGN-LB51wKyfiTxzAX2jw
        String bearerToken = request.getHeader("Authorization");
        // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjAxQGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9DVVNUT01FUiIsImV4cCI6MTY4MzUzODY5MX0.NkZtQdBz3j8u0eo9kjad0clNpdC6WFvIQWi7_ETEeGhirmw-BL8-fj5Zk2Qt67fwjDGN-LB51wKyfiTxzAX2jw
        String jwtToken = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            jwtToken = bearerToken.substring(7);
        }
        Authentication authentication = tokenProvider.getAuthentication(jwtToken);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
