// Utility class containing helper methods for common validations
public class Utility {

    /**
     * Validates if the provided string is a valid email format.
     * 
     * @param email The string to be checked.
     * @return true if the string is a valid email format, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        // Regular expression to match most common email formats
        return email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Validates if the provided string is a valid phone number format.
     * 
     * @param phone The string to be checked.
     * @return true if the string is a valid 10-digit phone number, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phone) {
        // Regular expression to match a 10-digit phone number
        return phone.matches("\\d{10}");
    }

    // Automatically generate a unique user code
    public static String generateUniqueCode() {
        // Implement your logic to generate a unique alphanumeric code
        // For example, you can use UUID or any custom logic
        // For simplicity, let's use a placeholder here
        int randomNumber = (int) (Math.random() * 99999) + 10000;
        //return "UC_" + System.currentTimeMillis();
        return "UC_" + randomNumber;
    }
}
