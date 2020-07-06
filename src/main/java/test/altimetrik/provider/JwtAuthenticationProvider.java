package test.altimetrik.provider;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import test.altimetrik.entity.UserDo;
import test.altimetrik.service.JwtTokenService;
import test.altimetrik.token.JWTAuthenticationToken;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JWTAuthenticationToken) {
            UserDo userDo = jwtTokenService.parseToken(((JWTAuthenticationToken) authentication).getCredentials());
            if (userDo == null) {
                throw new BadCredentialsException("auth failed");
            }
            return new JWTAuthenticationToken(new User(userDo.getUserName(), "password", new ArrayList<>()), ((JWTAuthenticationToken) authentication).getCredentials(), new ArrayList<>());
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JWTAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
