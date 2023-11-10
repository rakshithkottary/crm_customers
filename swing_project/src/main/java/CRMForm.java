import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// Class representing a CRM (Customer Relationship Management) form
public class CRMForm extends JFrame {

    // UI components for the form
    private JLabel nameLabel, emailLabel, phoneLabel, questionLabel;
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton;
    private List<User> userList;
    private List<JRadioButton> radioButtons; // List to store radio buttons for questions

    // Method to reset all fields in the form
    private void resetFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");

        for (JRadioButton radioButton : radioButtons) {
            radioButton.setSelected(false);
        }
    }

    // Constructor for the CRMForm class
    public CRMForm(List<User> userList, JFrame previousFrame) {
        this.userList = userList;
        radioButtons = new ArrayList<>();

        // Initialize labels and text fields
        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        questionLabel = new JLabel("Please answer the following questions (Yes/No):");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        // Initialize and set action listener for the submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Count the number of 'Yes' answers
                int count = 0;
                for (JRadioButton radioButton : radioButtons) {
                    if (radioButton.isSelected()) {
                        count++;
                    }
                }

                // Determine user status based on the percentage of 'Yes' answers
                double percentage = (double) count / radioButtons.size();
                String status;
                if (percentage >= 0.9) {
                    status = "Opportunity";
                } else if (percentage >= 0.7) {
                    status = "Potential Lead";
                } else {
                    status = "Regular Customer";
                }

                // Extract user details from text fields
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                // Validate email and phone number formats
                if (!Utility.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Utility.isValidPhoneNumber(phone)) {
                    JOptionPane.showMessageDialog(CRMForm.this, "Invalid phone number format! It should be 10 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if user has already submitted a response
                for (User existingUser : userList) {
                    if (existingUser.getEmail().equals(email) && existingUser.getPhone().equals(phone)) {
                        JOptionPane.showMessageDialog(CRMForm.this, "User already submitted response!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Create a new user object and add to the user list
                User user = new User(name, email, phone, status, "comment");
                userList.add(user);

                // Print the user list to the console (for debugging purposes)
                for (User us : userList) {
                    System.out.println(us);
                }
                System.out.println("*************************************");

                // Reset the form fields
                resetFields();

                // Show a success message
                JOptionPane.showMessageDialog(CRMForm.this, "Successfully submitted!", "Submission Message", JOptionPane.INFORMATION_MESSAGE);

                // Navigate back to the previous frame (if provided)
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                }
                dispose(); // Close the current frame (CRMForm)
            }
        });

        // Set the layout and add components to the form
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(phoneLabel);
        add(phoneField);
        add(questionLabel);

        // Add questions with radio buttons
        questionButton("Do you understand our product , its ingredients?");
        questionButton("Do you understand its advantages and disadvantages?");
        questionButton("Do you think this product would make a difference in your business?");
        questionButton("Would you refer this product to your business partners?");
        questionButton("Would you be willing to purchase this product in the next 2 days?");

        // Add the submit button to the form
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        submitButton.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the background color and other properties of the form
        getContentPane().setBackground(new Color(255, 140, 0)); // Dark orange color
        setTitle("CRM Feedback Form");
        setSize(550, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to add a question with 'Yes' and 'No' radio buttons to the form
    public void questionButton(String questionString) {
        JPanel panel = new JPanel();
        JLabel question = new JLabel(questionString);
        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        ButtonGroup group = new ButtonGroup();
        group.add(yesButton);
        group.add(noButton);
        radioButtons.add(yesButton); // Add the 'Yes' button to the radioButtons list

        panel.add(question);
        panel.add(yesButton);
        panel.add(noButton);
        add(panel);
    }

    public static void main(String[] args) {
        // Simulate a list of users (for testing purposes)
        List<User> userList = new ArrayList<>();

        // Create an instance of CRMForm
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CRMForm(userList, null);
            }
        });
    }
}
