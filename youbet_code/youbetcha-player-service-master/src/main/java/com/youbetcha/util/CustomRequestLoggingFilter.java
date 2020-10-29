package com.youbetcha.util;

import org.springframework.lang.Nullable;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isDebugEnabled();
    }

    /**
     * Writes a log message before the request is processed.
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    // Matches user input for the password field on signup
    public static final String PASSWORD_REGEX_SIGNUP = "(?<=\"password\":\").*?(?=\",\"bonusOptIn\")";

    // Matches user input for the password field on login
    public static final String PASSWORD_REGEX_LOGIN = "(?<=\"password\":\").*?(\")";

    @Nullable
    @Override
    protected String getMessagePayload(HttpServletRequest request) {

        String payloadString = super.getMessagePayload(request);

        if (request.getRequestURI().endsWith("/players") || request.getRequestURI().endsWith("/login")) {

            if (payloadString == null || payloadString.equals("[unknown]")) {
                return payloadString;
            }

            return request.getRequestURI().endsWith("/login") ?
                    this.filterByRegexPattern(payloadString, PASSWORD_REGEX_LOGIN) :
                    this.filterByRegexPattern(payloadString, PASSWORD_REGEX_SIGNUP);
        }

        return payloadString;
    }

    public String filterByRegexPattern(String message, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

         if (pattern.matcher(message).find()) {
             Matcher matcher = pattern.matcher(message);
             StringBuffer  sb = new StringBuffer();

             while (matcher.find()) {

                 // Fixed length string replacement used to avoid exposing masked string length
                 String repString = "***********";
                 matcher.groupCount();

                 matcher.appendReplacement(sb, repString);

                 matcher.appendTail(sb);
             }

             return (sb.toString());
         }

        return message;
    }
}
