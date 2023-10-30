import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends JFrame {
    private List<User> userList;

    public LandingPage(List<User> userList) {
        this.userList = userList;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton customerButton = new JButton("Are you a Customer?");
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LandingPage.this.setVisible(false); // Hide the landing page
                CRMForm crmForm = new CRMForm(userList, LandingPage.this); // Pass the userList and LandingPage instance to CRMForm
            }
        });

        JButton adminButton = new JButton("Are you an Admin?");
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputName = JOptionPane.showInputDialog(LandingPage.this, "Enter Username:");
                String inputPassword = JOptionPane.showInputDialog(LandingPage.this, "Enter Password:");
                if (inputName != null && inputPassword != null && inputName.equals("admin") && inputPassword.equals("admin")) {
                    LandingPage.this.setVisible(false); // Hide the landing page
                    AdminPage adminPage = new AdminPage(new ArrayList<>(userList),userList, LandingPage.this); // Pass the userList, separate list, and LandingPage instance to AdminPage
                } else {
                    JOptionPane.showMessageDialog(LandingPage.this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(customerButton);
        panel.add(adminButton);

        add(panel);
        setTitle("Landing Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        List<User> userList = Main.getUserList(); // Access the shared user list from the Main class

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LandingPage(userList);
            }
        });
    }
}
