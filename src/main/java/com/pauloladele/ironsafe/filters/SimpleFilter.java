package com.pauloladele.ironsafe.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/credentials") || request.getRequestURI().equals("/register")  || request.getRequestURI().equals("/add-credential")  || request.getRequestURI().equals("/login")) {
            RequestDispatcher requestDispatcher = request
                    .getRequestDispatcher("/");
            requestDispatcher.forward(request, response);
        }
        filterChain.doFilter(request, response);
    }
}
