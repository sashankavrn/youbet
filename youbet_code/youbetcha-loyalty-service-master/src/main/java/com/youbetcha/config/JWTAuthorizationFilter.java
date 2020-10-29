package com.youbetcha.config;

import com.youbetcha.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final String COOKIE_NAME = "youbetcha";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        Cookie jwtCookie = WebUtils.getCookie(req, COOKIE_NAME);
        String jwtValue;
        //TODO alan hack remove later when cookies work properly
        if (jwtCookie != null) {
            jwtValue = jwtCookie.getValue();
        } else {
            jwtValue = req.getHeader("jwt");
        }

        // if token is valid configure Spring Security to manually set authentication
        if (jwtValue != null && jwtTokenUtil.validateToken(jwtValue)) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    jwtValue, null, null);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(req, res);
    }
}
