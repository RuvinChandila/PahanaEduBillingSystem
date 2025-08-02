package com.pahanasolutions.PahanaEdu.servlet;

import com.pahanasolutions.PahanaEdu.dao.UserDao;
import com.pahanasolutions.PahanaEdu.model.User;
import com.pahanasolutions.PahanaEdu.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // --- Server-Side Validation ---
        if (fullName == null || fullName.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (userDao.isUsernameExists(username)) {
            request.setAttribute("errorMessage", "This username is already taken. Please choose another one.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // --- If validation passes, create the user ---
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtil.hashPassword(password)); // Hash the password before saving

        boolean isRegistered = userDao.registerUser(newUser);

        if (isRegistered) {
            // Set a success message in the session and redirect to the login page
            request.getSession().setAttribute("successMessage", "Registration successful! You can now log in.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            // Handle potential database errors during registration
            request.setAttribute("errorMessage", "An unexpected error occurred during registration. Please contact support.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
