package com.dou.adm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String CORSAllowHeaders = request.getHeader("Access-Control-Request-Headers");
            if (CORSAllowHeaders == null || !CORSAllowHeaders.contains("authorization")) {
                String jwt = request.getHeader("Authorization");

                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    JwtUser jwtUser = tokenProvider.getJwtUser(jwt);
                    request.setAttribute("jwtUser", jwtUser);
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

//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
}
