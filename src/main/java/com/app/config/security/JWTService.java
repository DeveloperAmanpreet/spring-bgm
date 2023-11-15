package com.app.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTService {

    public static final String ROLES = "roles";
    public static final String SIGNATURE_KEY = "SignatureKey";

    ObjectMapper objectMapper = new ObjectMapper();

    // TODO: Key should not be hard coded. Should be read from config or env
    private final Algorithm algorithm = Algorithm.HMAC256(SIGNATURE_KEY);

    public String createJWT(String userName) {
        return createJWT(
                userName,
                new Date(),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
        );
    }

    protected String createJWT(String userName, Date iat, Date exp) {
        return JWT.create()
                .withSubject(userName)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .sign(algorithm);
    }

    public String createJWT(String userName, List<String> roles) throws JsonProcessingException {
        return JWT.create()
            .withSubject(userName)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
            .withArrayClaim(ROLES, roles.toArray(new String[0]))
            .sign(algorithm);
    }

    public String getUserNameFromJWT(String jwt) throws JsonProcessingException {
        var decodedJWT = JWT.decode(jwt);
        return decodedJWT.getSubject();
    }

    public List<String> getRolesFromJWT(String jwt) throws JsonProcessingException {
        var decodedJWT = JWT.decode(jwt);
        return decodedJWT.getClaim(ROLES).asList(String.class);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        var decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt();
    }
}
