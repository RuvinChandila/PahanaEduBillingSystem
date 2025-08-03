package com.pahanasolutions.PahanaEdu.util;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        // The gensalt() method automatically handles creating a salt
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            // Log error or handle cases where the hash is invalid
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
}
