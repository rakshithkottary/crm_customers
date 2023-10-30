import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<User> userList = new ArrayList<>(); // Shared user list

    private static List<User> adminList = new ArrayList<>();

    public static void main(String[] args) {
        // Creating an instance of LandingPage
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LandingPage(userList,adminList);
            }
        });
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static List<User> getAdminList() {
        return adminList;
    }
}