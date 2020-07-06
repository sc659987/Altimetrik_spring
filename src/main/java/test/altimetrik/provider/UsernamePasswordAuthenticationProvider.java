package test.altimetrik.provider;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import test.altimetrik.entity.UserDo;
import test.altimetrik.service.AuthService;
import test.altimetrik.service.JwtTokenService;
import test.altimetrik.token.JWTAuthenticationToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

@Component
@AllArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDo userDo = authService.getByUsernameAndPassword((String) authentication.getPrincipal(), (String) authentication.getCredentials());
            if (userDo != null) {
                final String jwToken = jwtTokenService.generateToken(userDo);
                return new JWTAuthenticationToken(new User(userDo.getUserName(), userDo.getPassword(), new LinkedList<>()), jwToken, null);
            }
        }
        throw new BadCredentialsException("auth failed");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
