package com.rtv.auth.filter;

import com.rtv.api.auth.User;
import com.rtv.auth.JwtHelper;
import com.rtv.auth.UserContext;
import com.rtv.store.UserDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final String jwtTokenKey;

    public static final Set<String> byPassedUrls = new HashSet<>();
    static {
        byPassedUrls.add("/wm/authenticate");
        byPassedUrls.add("/wm/swagger");
        byPassedUrls.add("/wm/bills");
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
            if (!isByPassed(servletRequest)) {
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

    public static boolean isByPassed(HttpServletRequest request) {
        for (String url :  byPassedUrls) {
            if (request.getRequestURI().startsWith(url)) {
                return true;
            }
        }
        return "OPTIONS".equals(request.getMethod());
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
        User user = UserDAO.getUserByUsername(username);
        if (null != user) {
            UserContext.current().setUser(user);
        }
    }

    @Override
    public void destroy() {

    }
}
