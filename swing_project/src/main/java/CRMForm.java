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

                User user = new User(name, email, phone, status);
                userList.add(user);

                for (User us : userList) {
                    System.out.println(us);
                }
                System.out.println("*************************************");
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
        setTitle("CRM Feedback Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        JButton viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame userListFrame = new JFrame("Customer List");
                JTextArea userListArea = new JTextArea(10, 30);
                JScrollPane userListScrollPane = new JScrollPane(userListArea);
                userListFrame.add(userListScrollPane);

                int countPl = 0;
                int countFo = 0;
                int countRc = 0;

                StringBuilder sb = new StringBuilder();

                for (User user : userList) {
                    if (user.getStatus().equals("Potential Lead")) {
                        countPl++;
                    } else if (user.getStatus().equals("Future Opportunity")) {
                        countFo++;
                    } else {
                        countRc++;
                    }
                }

                sb.append("Potential Lead : ").append(countPl).append(", Future Opportunity : ").append(countFo).append(", Regular Customer : ").append(countRc).append("\n");
                sb.append("********************************\n");

                for (User user : userList) {
                    sb.append(user.toString()).append("\n");
                }

                userListArea.setText(sb.toString());

                userListFrame.setSize(400, 400);
                userListFrame.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        submitButton.setPreferredSize(new Dimension(150, 30));
        viewCustomersButton.setPreferredSize(new Dimension(150, 30));

        buttonPanel.add(submitButton);
        buttonPanel.add(viewCustomersButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("CRM Feedback Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CRMForm();
    }
}