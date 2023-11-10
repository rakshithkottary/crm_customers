// Import necessary libraries for GUI, event handling, and data structures
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Main class for the admin page, extending JFrame for GUI window
public class AdminPage extends JFrame {
    // Lists to store admin and customer data
    private List<User> adminUserList;
    private List<User> viewCustomerList;
    // Components for displaying and editing user data
    private JTable table;
    private JScrollPane scrollPane;
    private JTextField nameField, emailField, phoneField, expenseField;
    private JFrame landingPage;

    // Constructor for the AdminPage class
    public AdminPage(List<User> adminUserList, List<User> viewCustomerList, JFrame landingPage) {
        this.adminUserList = new ArrayList<>(adminUserList);
        this.viewCustomerList = new ArrayList<>(viewCustomerList);
        this.landingPage = landingPage;

        // Create a main panel with a custom background color
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(255, 140, 0)); // Dark orange color
            }
        };
        panel.setOpaque(true);

        // Add a heading using JLabel
        JLabel headingLabel = new JLabel("UIC MED REP");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Adjust the font and size
        headingLabel.setHorizontalAlignment(JLabel.CENTER); // Center align the heading
        panel.add(headingLabel);

        // Set the layout for the main panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create an input panel for user details
        JPanel inputPanel = new JPanel();
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        phoneField = new JTextField(15);
        expenseField = new JTextField(15);

        // Add labels and text fields to the input panel
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Expense:"));
        inputPanel.add(expenseField);
        panel.add(inputPanel);

        // Create a button panel for CRUD operations and navigation
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Icons for the buttons (paths should be adjusted based on the actual location)
        ImageIcon addIcon = new ImageIcon("resources/images/icons8-add-30.png");
        ImageIcon updateIcon = new ImageIcon("resources/images/icons8-update-30.png");
        ImageIcon deleteIcon = new ImageIcon("resources/images/icons8-delete-30.png");

        // Add button with its action listener
        JButton addButton = new JButton(addIcon);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Extract user details from text fields
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String expenseStr = expenseField.getText();
                double expense = Double.parseDouble(expenseStr);
                String comment = (expense > 10000) ? "This user is eligible for a gift card worth $1000" : "This user is not eligible for any incentives";
                User user = new User(name, email, phone, "Regular Customer", comment);
                adminUserList.add(user);
                updateTableData(adminUserList);
            }
        });

        // Update button with its action listener
        JButton updateButton = new JButton(updateIcon);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Update the user details based on the selected row
                    User user = adminUserList.get(selectedRow);
                    user.setName(nameField.getText());
                    user.setEmail(emailField.getText());
                    user.setPhone(phoneField.getText());
                    String expenseStr = expenseField.getText();
                    double expense = Double.parseDouble(expenseStr);
                    String comment = (expense > 10000) ? "This user is eligible for a gift card worth $10000" : "This user is not eligible for any incentives";
                    user.setComment(comment);
                    updateTableData(adminUserList);
                }
            }
        });

        // Delete button with its action listener
        JButton deleteButton = new JButton(deleteIcon);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Remove the user based on the selected row
                    adminUserList.remove(selectedRow);
                    updateTableData(adminUserList);
                }
            }
        });

        // Button to view customer details
        JButton viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new frame to display customer details
                JFrame userListFrame = new JFrame("Customer List");
                JTextArea userListArea = new JTextArea(10, 30);
                JScrollPane userListScrollPane = new JScrollPane(userListArea);
                userListFrame.add(userListScrollPane);

                // Count the number of users based on their status
                int countPl = 0;
                int countFo = 0;
                int countRc = 0;

                StringBuilder sb = new StringBuilder();

                for (User user : viewCustomerList) {
                    if (user.getStatus().equals("Opportunity")) {
                        countPl++;
                    } else if (user.getStatus().equals("Potential Lead")) {
                        countFo++;
                    } else {
                        countRc++;
                    }
                }

                // Append the user details to the text area
                sb.append("Opportunity : ").append(countPl).append(", Potential Lead : ").append(countFo).append(", Regular Customer : ").append(countRc).append("\n");
                sb.append("********************************\n");

                for (User user : viewCustomerList) {
                    sb.append(user.toString()).append("\n");
                }

                userListArea.setText(sb.toString());

                userListFrame.setSize(400, 400);
                userListFrame.setVisible(true);
            }
        });

        // Button to navigate back to the landing page
        JButton backButton = new JButton("Back to Landing Page");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                landingPage.setVisible(true);
                AdminPage.this.dispose();
            }
        });

        // Add all buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewCustomersButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);

        // Create a table to display user details
        String[] columnNames = {"Name", "Email", "Phone", "Incentives"};
        String[][] data = new String[adminUserList.size()][4];
        for (int i = 0; i < adminUserList.size(); i++) {
            User user = adminUserList.get(i);
            data[i][0] = user.getName();
            data[i][1] = user.getEmail();
            data[i][2] = user.getPhone();
            data[i][3] = user.getComment();
        }
        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        // Add the main panel to the JFrame
        add(panel);
        setTitle("Admin Page");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to update the table data
    private void updateTableData(List<User> userList) {
        String[] columnNames = {"Name", "Email", "Phone", "Comment"};
        String[][] data = new String[userList.size()][4];
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            data[i][0] = user.getName();
            data[i][1] = user.getEmail();
            data[i][2] = user.getPhone();
            data[i][3] = user.getComment();
        }
        table = new JTable(data, columnNames);
        scrollPane.setViewportView(table);
    }
}
