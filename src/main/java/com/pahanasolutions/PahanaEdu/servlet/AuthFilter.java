package com.pahanasolutions.PahanaEdu.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter("/app/*") // This filter applies to all URLs starting with /app/
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // false = do not create new session

        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);

        if (isLoggedIn) {
            // User is logged in, continue with the request
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to the login page
            // httpResponse.getContextPath() ensures the correct path even if app is deployed under a different name
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    // Other filter methods (init, destroy) can be left empty for this simple case
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}