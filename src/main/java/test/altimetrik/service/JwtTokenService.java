package test.altimetrik.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.altimetrik.entity.UserDo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    @Value("${jwt.expiration.time}")
    private String EXPIRATION_TIME;

    public UserDo parseToken(String token) {
        try {
            final Claims body = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
            final UserDo userDo = new UserDo(body.getSubject(), null);
            return userDo;
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(UserDo u) {
        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(u.getUserName()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
    }
}
