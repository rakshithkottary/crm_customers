import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();

        // Creating an instance of CRMForm
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CRMForm(userList);
            }
        });

        // Creating an instance of AdminPage
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminPage(userList);
            }
        });
    }
}
