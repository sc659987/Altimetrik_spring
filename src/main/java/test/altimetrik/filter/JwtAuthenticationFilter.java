package test.altimetrik.filter;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import test.altimetrik.token.JWTAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static String AUTHENTICATION_BEARER_SCHEMA = "Bearer";
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = getToken(request);
        if (authorization != null && authorization.startsWith(AUTHENTICATION_BEARER_SCHEMA)) {
            final String token = authorization.substring(AUTHENTICATION_BEARER_SCHEMA.length()).trim();
            try {
                Authentication authentication = authenticationManager.authenticate(new JWTAuthenticationToken(token));
                if (authentication.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (final Exception e) {
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
