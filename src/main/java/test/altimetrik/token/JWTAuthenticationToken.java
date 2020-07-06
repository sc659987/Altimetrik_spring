package test.altimetrik.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class JWTAuthenticationToken extends AbstractAuthenticationToken {
    private final String credentials;
    private final UserDetails principal;

    public JWTAuthenticationToken(final String token) {
        super(null);
        this.credentials = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public JWTAuthenticationToken(final UserDetails userDetails, final String token,
                                  final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDetails;
        this.credentials = token;
        super.setAuthenticated(true); // must use super, as we override
    }
}
