package com.pahanasolutions.PahanaEdu.servlet;

import com.pahanasolutions.PahanaEdu.dao.UserDao;
import com.pahanasolutions.PahanaEdu.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDao.login(username, password);

        if (user != null) {
            // Login successful, create a session
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            // Redirect to the main application dashboard
            response.sendRedirect(request.getContextPath() + "/app/dashboard.jsp");
        } else {
            // Login failed, set an error message and forward back to the login page
            request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}