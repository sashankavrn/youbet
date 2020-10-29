package com.youbetcha.jwt;

import com.youbetcha.client.Login;
import com.youbetcha.model.SessionIdValidationResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final long serialVersionUID = -2550185165626007488L;
    @Autowired
    Login loginClient;
    private String secret = "38782F413F4428472B4B6250655368566D597133743677397A244226452948404D635166546A576E5A7234753778214125442A462D4A614E645267556B587032";

    /**
     * Get the countryCode from the token
     *
     * @param token
     * @return
     */
    public String getCountryFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("country");
    }

    /**
     * Get the Session Id from the token
     *
     * @param token
     * @return
     */
    public String getSessionIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("sessionId");
    }

    /**
     * Get the email from the token
     *
     * @param token
     * @return
     */
    public String getEmailFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("email");
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * validate token
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token) && validateSessionId(token);
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token validation error", e.getMessage());
        }
        return false;
    }

    private boolean validateSessionId(String token) {
        String sessionId = getSessionIdFromToken(token);
        Optional<SessionIdValidationResponse> response = loginClient.validateSession(sessionId);
        if (!response.isPresent()) {
            return false;
        }
        return response.get().getIsActive() == 1;
    }
}