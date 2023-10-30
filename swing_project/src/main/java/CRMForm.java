import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;



public class CRMForm extends JFrame {


    private JLabel nameLabel, emailLabel, phoneLabel, questionLabel;
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton;
    private List<User> userList;
    private List<JRadioButton> radioButtons;

    private void resetFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");

        for (JRadioButton radioButton : radioButtons) {
            radioButton.setSelected(false);
        }
    }

    public CRMForm(List<User> userList, JFrame previousFrame) {
        this.userList = userList;
        radioButtons = new ArrayList<>();

        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        questionLabel = new JLabel("Please answer the following questions (Yes/No):");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 0;
                for (JRadioButton radioButton : radioButtons) {
                    if (radioButton.isSelected()) {
                        count++;
                    }
                }

                double percentage = (double) count / radioButtons.size();
                String status;
                if (percentage >= 0.9) {
                    status = "Potential Lead";
                } else if (percentage >= 0.7) {
                    status = "Future Opportunity";
                } else {
                    status = "Regular Customer";
                }

                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (!Utility.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Utility.isValidPhoneNumber(phone)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid phone number format! It should be 10 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (User existingUser : userList) {
                    if (existingUser.getEmail().equals(email) && existingUser.getPhone().equals(phone)) {
                        JOptionPane.showMessageDialog(CRMForm.this, "User already submitted response!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                User user = new User(name, email, phone, status, "comment"); // Updated here
                userList.add(user);

                for (User us : userList) {
                    System.out.println(us);
                }
                System.out.println("*************************************");

                resetFields();

                JOptionPane.showMessageDialog(CRMForm.this, "Successfully submitted!", "Submission Message", JOptionPane.INFORMATION_MESSAGE);

                previousFrame.setVisible(true); // Show the previous frame (LandingPage)
                dispose(); // Close the current frame (CRMForm)
            }
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(phoneLabel);
        add(phoneField);
        add(questionLabel);

        questionButton("Do you understand our product , its ingredients?" + ":");
        questionButton("Do you understand its advantages and disadvantages?" + ":");
        questionButton("Do you think this product would make a difference in your business?" + ":");
        questionButton("Would you refer this product to your business partners" + ":");
        questionButton("Would you be willing to purchase this product in the next 2 days? "+ ":");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        submitButton.setPreferredSize(new Dimension(150, 30));

        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set the background color to dark orange
        getContentPane().setBackground(new Color(255, 140, 0));

        setTitle("CRM Feedback Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void questionButton(String questionString) {
        JPanel panel = new JPanel();
        JLabel question = new JLabel(questionString);
        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        ButtonGroup group = new ButtonGroup();
        group.add(yesButton);
        group.add(noButton);
        radioButtons.add(yesButton);

        panel.add(question);
        panel.add(yesButton);
        panel.add(noButton);
        add(panel);
    }

    public static void main(String[] args) {
        // Simulate a list of users
        List<User> userList = new ArrayList<>();

        // Create an instance of CRMForm
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CRMForm(userList, null);
            }
        });
    }
}
