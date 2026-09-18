package smartparking.manager;

public class PasswordValidator {

    public static String validate(String password) {

        boolean upperCase = false;
        boolean lowerCase = false;
        boolean number = false;
        boolean special = false;

        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (ch >= 'A' && ch <= 'Z') {
                upperCase = true;
            }
            else if (ch >= 'a' && ch <= 'z') {
                lowerCase = true;
            }
            else if (ch >= '0' && ch <= '9') {
                number = true;
            }
            else {
                special = true;
            }
        }

        String error = "";

        if (password.length() < 6) {
            error += "Password must be at least 6 characters long.\n";
        }

        if (!upperCase) {
            error += "Password must contain at least 1 uppercase letter.\n";
        }

        if (!lowerCase) {
            error += "Password must contain at least 1 lowercase letter.\n";
        }

        if (!number) {
            error += "Password must contain at least 1 number.\n";
        }

        if (!special) {
            error += "Password must contain at least 1 special character.\n";
        }

        return error;
    }
}