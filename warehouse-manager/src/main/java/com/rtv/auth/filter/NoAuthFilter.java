package com.rtv.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rtv.auth.UserContext;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class NoAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (!JwtFilter.isByPassed(((HttpServletRequest) request).getRequestURI())) {
            if (null == UserContext.current().getUser()) {
                if (response instanceof HttpServletResponse) {
                    HttpServletResponse servletResponse = (HttpServletResponse) response;
                    servletResponse.sendError(UNAUTHORIZED.getStatusCode());
                }
            } else {
                chain.doFilter(request, response);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
