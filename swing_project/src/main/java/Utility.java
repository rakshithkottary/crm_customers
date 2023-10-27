public class Utility {

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }
}
