package ru.ezuykow.socialmediabackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author ezuykow
 */
@Component
public class JWTUtil {

    @Value("${token.expiration.minutes}")
    private int tokenExpirationMinutes;

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.subject}")
    private String tokenSubject;

    @Value("${token.username_claim_name}")
    private String tokenUsernameClaimName;

    @Value("${token.issuer}")
    private String tokenIssuer;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(tokenExpirationMinutes).toInstant());

        return JWT.create()
                .withSubject(tokenSubject)
                .withClaim(tokenUsernameClaimName, username)
                .withIssuedAt(new Date())
                .withIssuer(tokenIssuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecret))
                .withSubject(tokenSubject)
                .withIssuer(tokenIssuer)
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim(tokenUsernameClaimName).asString();
    }
}
