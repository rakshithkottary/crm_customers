import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CRMForm extends JFrame {
    private JLabel nameLabel, emailLabel, phoneLabel, questionLabel;
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton;
    private List<User> userList;
    private List<JRadioButton> radioButtons;
    private JTextArea outputArea;

    public CRMForm() {
        userList = new ArrayList<>();
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

                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!isValidPhoneNumber(phone)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid phone number format! It should be 10 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (User existingUser : userList) {
                    if (existingUser.getEmail().equals(email) && existingUser.getPhone().equals(phone)) {
                        JOptionPane.showMessageDialog(CRMForm.this, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                User user = new User(name, email, phone, status);
                userList.add(user);

                for (User us : userList) {
                    System.out.println(us);
                }
                System.out.println("*************************************");

                updateOutputArea();
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

        for (int i = 1; i <= 5; i++) {
            JPanel panel = new JPanel();
            JLabel question = new JLabel("Question " + i + ":");
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

        add(submitButton);

        setTitle("CRM Feedback Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        outputArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);

        setTitle("CRM Feedback Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateOutputArea() {
        StringBuilder sbLeads = new StringBuilder("Potential Leads:\n");
        StringBuilder sbOpportunities = new StringBuilder("Future Opportunities:\n");

        for (User user : userList) {
            if (user.getStatus().equals("Potential Lead")) {
                sbLeads.append(user.toString()).append("\n");
            } else if (user.getStatus().equals("Future Opportunity")) {
                sbOpportunities.append(user.toString()).append("\n");
            }
        }

        outputArea.setText(sbLeads.toString() + "\n" + sbOpportunities.toString());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    public static void main(String[] args) {
        new CRMForm();
    }
}
