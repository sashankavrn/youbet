package com.youbetcha.config;

import com.youbetcha.jwt.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class to get jwt token out of request and extract sessionId into SecurityContext
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final String COOKIE_NAME = "youbetcha";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
    	
		try {
			Cookie jwtCookie = WebUtils.getCookie(req, COOKIE_NAME);
			String jwtValue;
			//TODO alan hack remove later when cookies work properly
			if (jwtCookie != null) {
				jwtValue = jwtCookie.getValue();
			} else {
				jwtValue = req.getHeader("jwt");
			}
			
			LOGGER.info("D> Checking jwt for path: " + req.getRequestURI());
			String path = req.getRequestURI();
			// if token is valid configure Spring Security to manually set authentication
			if (jwtValue != null 
					&& !path.equalsIgnoreCase("/player-service/api/v1/login")
					&& !path.equalsIgnoreCase("/player-service/api/v1/countries")
					&& !path.equalsIgnoreCase("/player-service/api/v1/geoip")
					&& !path.equalsIgnoreCase("/player-service/api/v1/languages")
					&& !path.equalsIgnoreCase("/player-service/api/v1/currencies") 
					&& !path.equalsIgnoreCase("/player-service/api/v1/players") 
					&& !path.toLowerCase().startsWith("/player-service/api/v1/preregistration/")) { 
				LOGGER.info("looking up jwtValue {}", jwtValue);
				if (!jwtTokenUtil.validateToken(jwtValue)) {
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The jwt token is not valid.");
				}
				MDC.put("email", jwtTokenUtil.getEmailFromToken(jwtValue));
				MDC.put("countryCode", jwtTokenUtil.getCountryFromToken(jwtValue));
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						jwtTokenUtil.getSessionIdFromToken(jwtValue), null, null);
				SecurityContextHolder.getContext().setAuthentication(token);
			}
			chain.doFilter(req, res);
		} finally {
			MDC.clear();
		}
    }
}
