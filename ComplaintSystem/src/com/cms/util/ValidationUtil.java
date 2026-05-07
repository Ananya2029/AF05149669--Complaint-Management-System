package com.cms.util;

public class ValidationUtil {

    public static boolean isValidName(String name) {
        return name != null && name.trim().matches("[A-Za-z ]{3,30}");
    }

    public static boolean isValidPassword(String password) {
        return password != null &&
                password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$");
    }

    public static boolean isValidRole(String role) {
        return role.equalsIgnoreCase("USER");
    }

    public static boolean isValidCategory(String category) {

        if (category == null) return false;

        category = category.trim();

        return category.length() >= 3 &&
               category.length() <= 50 &&
               category.matches("^[A-Za-z ]+$");
    }

    public static boolean isValidDescription(String desc) {
        return desc != null && desc.trim().length() >= 10 && desc.length() <= 500;
    }

    public static boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Pending") ||
               status.equalsIgnoreCase("In Progress") ||
               status.equalsIgnoreCase("Resolved") ||
               status.equalsIgnoreCase("Rejected") ||
        	   status.equalsIgnoreCase("Escalated");
    }
}