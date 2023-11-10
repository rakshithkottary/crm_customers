// Import necessary libraries for data structures
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Shared user list across the application
    private static List<User> userList = new ArrayList<>();

    // Shared admin list across the application
    private static List<User> adminList = new ArrayList<>();

    public static void main(String[] args) {
        // Ensuring that the GUI creation happens on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Creating and initializing an instance of LandingPage
                new LandingPage(userList, adminList);
            }
        });
    }

    // Getter method to retrieve the user list
    public static List<User> getUserList() {
        return userList;
    }

    // Getter method to retrieve the admin list
    public static List<User> getAdminList() {
        return adminList;
    }
}
