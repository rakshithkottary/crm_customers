import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<User> userList = new ArrayList<>(); // Shared user list

    public static void main(String[] args) {
        // Creating an instance of LandingPage
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LandingPage(userList);
            }
        });
    }

    public static List<User> getUserList() {
        return userList;
    }
}
