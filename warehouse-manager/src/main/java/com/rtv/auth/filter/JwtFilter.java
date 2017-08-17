package com.rtv.auth.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtv.api.auth.User;
import com.rtv.auth.JwtHelper;
import com.rtv.auth.UserContext;
import com.rtv.store.UserDAO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

public class JwtFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final String jwtTokenKey;

    public static final Set<String> byPassedUrls = new HashSet<>();
    static {
        byPassedUrls.add("/authenticate");
    }

    public JwtFilter(@NotBlank String jwtTokenKey) {
        this.jwtTokenKey = jwtTokenKey;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            if (!isByPassed(servletRequest.getRequestURI())) {
                String jwtToken = getTokenFromCookie(servletRequest);
                if (StringUtils.isNotBlank(jwtToken)) {
                    Jws<Claims> claimsJws;
                    try {
                        claimsJws = JwtHelper.verifyAndDecode(jwtToken);
                        updateUserContext(claimsJws);
                    } catch (JwtException e) {
                        log.debug("Invalid Token Received");
                    }
                } else {
                    UserContext.current().setUser(null);
                }
            }
        }
        chain.doFilter(request, response);
    }

    public static boolean isByPassed(String requestURI) {
        for (String url :  byPassedUrls) {
            if (requestURI.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    private String getTokenFromCookie(HttpServletRequest servletRequest) {
        String token = null;
        Cookie[] cookies = servletRequest.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (jwtTokenKey.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private void updateUserContext(Jws<Claims> claims) {
        String username = claims.getBody().getSubject();
        User user = UserDAO.getUserByEmailOrMobile(username);
        if (null != user) {
            UserContext.current().setUser(user);
        }
    }

    @Override
    public void destroy() {

    }
}
