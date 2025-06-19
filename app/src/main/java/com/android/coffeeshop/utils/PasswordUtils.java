package com.android.coffeeshop.utils;

public class PasswordUtils {
    public static boolean validatePassword(String password) {
        // 1. Độ dài >= 6
        if (password.length() < 6) {
            return false;
        }
        // 2. Bắt đầu bằng chữ hoa
        if (!Character.isUpperCase(password.charAt(0))) {
            return false;
        }
        // 3. Chứa ít nhất 1 ký tự đặc biệt
        String specialChars = "!@#$%^&*()_+{}|:\"<>?`~[]\\;',./-=";
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (specialChars.indexOf(c) != -1) {
                hasSpecial = true;
                break;
            }
        }
        return hasSpecial;
    }
}
