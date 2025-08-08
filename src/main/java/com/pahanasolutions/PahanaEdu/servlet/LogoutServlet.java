package com.pahanasolutions.PahanaEdu.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get the current session, but don't create a new one if it doesn't exist.
        HttpSession session = request.getSession(false);

        // 2. If a session exists, invalidate it to clear all session data.
        if (session != null) {
            session.invalidate();
        }

        // 3. Redirect the user back to the login page.
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}