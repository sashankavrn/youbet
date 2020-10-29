package com.youbetcha.config;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

//    @Override
//    public void commence(
//            HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
//            throws IOException {
//        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        PrintWriter writer = response.getWriter();
//        writer.println("HTTP Status 401 - " + authEx.getMessage());
//    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Youbetcha");
        super.afterPropertiesSet();
    }
}
