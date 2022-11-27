package com.dou.adm.security;

import com.dou.adm.shared.SaveActivitiesLogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by Tu.Tran on 2018.09.21.
 */
@WebFilter(urlPatterns="/api/*")
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String REQ_USR = "jwtUser";

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private SaveActivitiesLogs saveActivitiesLogs;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (tokenProvider == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                tokenProvider = webApplicationContext.getBean(JwtProvider.class);
            }
            if (saveActivitiesLogs == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                saveActivitiesLogs = webApplicationContext.getBean(SaveActivitiesLogs.class);
            }
            String pathApi = request.getRequestURI();

            String CORSAllowHeaders = request.getHeader("Access-Control-Request-Headers");
            if (CORSAllowHeaders == null || !CORSAllowHeaders.contains("authorization")) {
                String jwt = request.getHeader("Authorization");

                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    JwtUser jwtUser = tokenProvider.getJwtUser(jwt);
                    request.setAttribute(REQ_USR, jwtUser);
                    try {
                        saveActivitiesLogs.funcSaveLog(pathApi, jwtUser.getUsername());
                    }catch (Exception e){
                        System.out.println(e);
                        logger.error("Error Log user actions: ", e);
                    }
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
