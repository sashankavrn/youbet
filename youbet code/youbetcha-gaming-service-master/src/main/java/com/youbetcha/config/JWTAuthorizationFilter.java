package com.youbetcha.config;

import com.youbetcha.jwt.JwtTokenUtil;
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

    private static final String COOKIE_NAME = "youbetcha";
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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

            // if token is valid configure Spring Security to manually set authentication
            if (jwtValue != null) {
                if (!jwtTokenUtil.validateToken(jwtValue)) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The jwt token is not valid.");
                }
                MDC.put("email", jwtTokenUtil.getEmailFromToken(jwtValue));
                MDC.put("countryCode", jwtTokenUtil.getCountryFromToken(jwtValue));
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        jwtValue, null, null);
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
