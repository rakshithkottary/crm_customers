import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CRMForm extends JFrame {

    // UI components for the form
    private JLabel nameLabel, emailLabel, phoneLabel, questionLabel;
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton;
    private List<User> userList;
    private List<List<JComponent>> components; // List to store components (JRadioButtons, JCheckBoxes, or JTextFields) for questions

    // Method to reset all fields in the form
    private void resetFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");

        for (List<JComponent> group : components) {
            for (JComponent component : group) {
                if (component instanceof AbstractButton) {
                    ((AbstractButton) component).setSelected(false);
                } else if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                }
            }
        }
    }

    // Constructor for the CRMForm class
    public CRMForm(List<User> userList, JFrame previousFrame) {
        this.userList = userList;
        components = new ArrayList<>();

        // Initialize labels and text fields
        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        questionLabel = new JLabel("Please answer the following questions by selecting a rating from 1 to 5 (1 - Very Dissatisfied, 5 - Very Satisfied):");

        // Set alignment for the question label
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(10);
        emailField = new JTextField(10);
        phoneField = new JTextField(10);

        // Initialize and set action listener for the submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

                // Check if the user has already submitted a response
                for (User existingUser : userList) {
                    if (existingUser.getEmail().equals(email) && existingUser.getPhone().equals(phone)) {
                        JOptionPane.showMessageDialog(CRMForm.this, "User already submitted a response!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Create a new user object and add it to the user list
                User user = new User(name, email, phone, "status", "comment");
                List<Integer> ratingList = new ArrayList<>();
                List<String> purchaseList;
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(0)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(1)))));
                purchaseList = getSelectedCheckboxValues(components.get(2));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(3)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(4)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(5)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(6)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(7)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(8)))));
                ratingList.add(Integer.parseInt(Objects.requireNonNull(getSelectedRadioButtonValue(components.get(10)))));

                user.setCustomerType(customerType(ratingList.stream().mapToInt(Integer::intValue).sum()));
                user.setPurchasedProducts(purchaseList);

                // Get feedback answers from the form
                List<String> feedbackAnswers = getFeedbackAnswers();
                user.setFeedbackAnswers(feedbackAnswers);

                userList.add(user);

                // Save the user and feedback answers to the database
                DatabaseManager.saveUserToDatabase(user);

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

        // Add the labels and text fields to a panel with a horizontal layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);

        // Set the background color for the input panel
        inputPanel.setBackground(Color.BLACK);

        // Set the text color for the labels
        nameLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        phoneLabel.setForeground(Color.WHITE);

        // Add the input panel to the form
        add(inputPanel);

        // Set the background color for the question label
        questionLabel.setBackground(Color.BLACK);
        questionLabel.setForeground(Color.WHITE);

        add(questionLabel);

        // Add survey questions with radio buttons, checkboxes, and text fields
        questionButton("How satisfied are you with the computer hardware products you purchased from us?");
        questionButton("How likely are you to recommend our computer hardware products to others?");
        questionButtonCheckbox("Which specific computer hardware product(s) did you purchase from us? (Check all that apply)");
        questionButton("How satisfied are you with the performance of the computer hardware product(s) you purchased?");
        questionButton("Did the product(s) meet your expectations in terms of quality and durability?");
        questionButton("How would you rate the ease of setup and installation of our computer hardware products?");
        questionButton("Have you ever contacted our customer support for assistance with your computer hardware product(s)?");
        questionButton("How satisfied were you with the assistance provided by our customer support team?");
        questionButton("On a scale of 1 to 5, how would you rate the responsiveness of our customer support?");
        questionTextField("What features or improvements would you like to see in our future computer hardware products?");
        questionButton("How likely are you to purchase from us again in the future?");
        questionTextField("Are there any specific brands or types of computer hardware you would like us to offer in the future?");

        // Set the background color for the submit button
        submitButton.setBackground(new Color(0, 0, 128)); // Navy blue

        // Set the text color for the submit button
        submitButton.setForeground(Color.WHITE);

        // Add the submit button to the form
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        submitButton.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(submitButton);

        // Set the background color for the button panel
        buttonPanel.setBackground(Color.BLACK);

        add(buttonPanel);

        // Set the background color for the form
        getContentPane().setBackground(new Color(0, 0, 128)); // Navy blue color

        // Set the background color for the content pane
        getRootPane().setBackground(new Color(0, 0, 128));

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the title for the form
        setTitle("CRM Feedback Form");

        // Set the size of the form
        setSize(550, 680);

        // Make the form visible
        setVisible(true);
    }

    public String customerType(int ratingsSum) {
        float ratio = (float) ratingsSum /45;

        if(ratio >= 0.8) return "Opportunity";
        if(ratio>=0.5) return "Potential Lead";
        return "Regular Customer";
    }

    // Method to add a question with radio buttons to the form (for 1 to 5 ratings)
    public void questionButton(String questionString) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel question = new JLabel(questionString);
        panel.add(question);

        List<JRadioButton> group = new ArrayList<>();
        ButtonGroup buttonGroup = new ButtonGroup();

        for (int i = 1; i <= 5; i++) {
            JRadioButton radioButton = new JRadioButton(Integer.toString(i));
            group.add(radioButton);
            buttonGroup.add(radioButton);
            panel.add(radioButton);
        }

        // Add the group of radio buttons to the list
        components.add(new ArrayList<>(List.copyOf(group)));
        add(panel);
    }

    // Method to add a question with checkboxes to the form
    public void questionButtonCheckbox(String questionString) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel question = new JLabel(questionString);
        panel.add(question);

        String[] options = {"CPU", "Motherboard", "Hard Drive", "Monitor", "Mouse"};

        List<AbstractButton> checkboxes = new ArrayList<>();

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkboxes.add(checkBox);
            panel.add(checkBox);
        }

        // Add the group of checkboxes to the list
        components.add(new ArrayList<>(List.copyOf(checkboxes)));
        add(panel);
    }

    // Method to add a question with an open-ended text field to the form
    public void questionTextField(String questionString) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel question = new JLabel(questionString);
        panel.add(question);

        JTextField textField = new JTextField(15);
        panel.add(textField);

        // Add the text field to the list
        components.add(new ArrayList<>(List.of(textField)));
        add(panel);
    }

    private String getSelectedRadioButtonValue(List<JComponent> radioButtonGroup) {
        for (JComponent component : radioButtonGroup) {
            if (component instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) component;
                if (radioButton.isSelected()) {
                    return radioButton.getText();
                }
            } else if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                return textField.getText();
            }
        }
        return null; // Or a default value
    }


    private List<String> getSelectedCheckboxValues(List<JComponent> checkboxGroup) {
        List<String> selectedValues = new ArrayList<>();
        for (JComponent component : checkboxGroup) {
            JCheckBox checkBox = (JCheckBox) component;
            if (checkBox.isSelected()) {
                selectedValues.add(checkBox.getText());
            }
        }
        return selectedValues;
    }

    // Add a method to get feedback answers from the form
    private List<String> getFeedbackAnswers() {
        List<String> feedbackAnswers = new ArrayList<>();

        for (int i = 0; i < components.size(); i++) {
            if (i != 2 && i != 9) { // Skip checkbox and text field questions
                String answer = getSelectedRadioButtonValue(components.get(i));
                feedbackAnswers.add(answer);
            }
        }

        return feedbackAnswers;
    }

}
