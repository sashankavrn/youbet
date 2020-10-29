package com.youbetcha.jwt;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.youbetcha.client.Login;
import com.youbetcha.model.Player;
import com.youbetcha.model.response.SessionIdValidationResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final long serialVersionUID = -2550185165626007488L;
    @Autowired
    Login loginClient;
    private String secret = "38782F413F4428472B4B6250655368566D597133743677397A244226452948404D635166546A576E5A7234753778214125442A462D4A614E645267556B587032";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the sessionId
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

    /**
     * Generate token for player - including vales in the claims
     *
     * @param player
     * @return
     */
    public String generateToken(Player player, String sessionId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", player.getFirstName());
        claims.put("lastName", player.getLastName());
        claims.put("email", player.getEmail());
        claims.put("language", player.getLanguage());
        claims.put("country", player.getCountryCode());
        claims.put("depositCount", player.getDepositCount());
        claims.put("sessionId", sessionId);
        return doGenerateToken(claims, player.getEmail());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

//    	SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
//                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
        if (isEmpty(sessionId)) {
            LOGGER.info("SessionId is null .. not checking");
            return false;
        }
        Optional<SessionIdValidationResponse> response = loginClient.validateSession(sessionId);
        if (!response.isPresent()) {
            return false;
        }
        return response.get().getIsActive() == 1;
    }
}