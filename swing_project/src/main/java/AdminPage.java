// ... (existing imports)
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Main class for the admin page, extending JFrame for GUI window
public class AdminPage extends JFrame {
    // Lists to store admin and customer data
    private List<User> adminUserList;
    private List<User> viewCustomerList;
    // Components for displaying and editing user data
    private JTable table;
    private JScrollPane scrollPane;
    private JTextField nameField, emailField, phoneField, expenseField,searchField;
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
        JLabel headingLabel = new JLabel("My Rep Application");
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

        // Create a search panel for user code search
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        // Add labels, text field, and search button to the search panel
        searchPanel.add(new JLabel("Search User Code:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel);

        // Add a key listener to the search field for real-time filtering
        searchField.addKeyListener(new KeyAdapter() {
            List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(allAdminUsers);
            }
        });

        // Add an action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(allAdminUsers);
            }
        });

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
                String userCode = Utility.generateUniqueCode();
                User user = new User(name, email, phone, "Regular Customer", comment);
                user.setUserCode(userCode);
                // Save user to the AdminPage database table
                DatabaseManager.saveAdminUserToDatabase(user);
                adminUserList.add(user);
                List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
                updateTableData(allAdminUsers);
            }
        });

        // Update button with its action listener
        JButton updateButton = new JButton(updateIcon);
        updateButton.addActionListener(new ActionListener() {
            List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Update the user details based on the selected row
                    User user = allAdminUsers.get(selectedRow);
                    user.setName(nameField.getText());
                    user.setEmail(emailField.getText());
                    user.setPhone(phoneField.getText());
                    String expenseStr = expenseField.getText();
                    double expense = Double.parseDouble(expenseStr);
                    String comment = (expense > 10000) ? "This user is eligible for a gift card worth $10000" : "This user is not eligible for any incentives";
                    user.setComment(comment);
                    // Update user in the AdminPage database table
                    DatabaseManager.updateAdminUserInDatabase(user);
                    List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
                    updateTableData(allAdminUsers);
                }
            }
        });

        // Delete button with its action listener
        JButton deleteButton = new JButton(deleteIcon);
        deleteButton.addActionListener(new ActionListener() {
            List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Remove the user based on the selected row
                    User user = allAdminUsers.remove(selectedRow);
                    // Delete user from the AdminPage database table
                    DatabaseManager.deleteAdminUserFromDatabase(user);
                    List<User> allAdminUsers = DatabaseManager.getAdminUsersFromDatabase();
                    updateTableData(allAdminUsers);
                }
            }
        });

        // Button to view customer details
        JButton viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fetch all users from the database
                List<User> allUsers = DatabaseManager.getUsersFromDatabase();
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

                for (User user : allUsers) {
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

                for (User user : allUsers) {
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
        JButton dashboardButton = new JButton("View Dashboard");
        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDashboard();
            }
        });

        // Add all buttons to the button panel
        //buttonPanel.add(AnalyticsButton);
        buttonPanel.add(dashboardButton);  // Add the dashboard button
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewCustomersButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);

        // Create a table to display user details
        String[] columnNames = {"User Code","Name", "Email", "Phone", "Incentives"};
        String[][] data = new String[adminUserList.size()][5];
        for (int i = 0; i < adminUserList.size(); i++) {
            User user = adminUserList.get(i);
            data[i][0] = user.getUserCode();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();
            data[i][3] = user.getPhone();
            data[i][4] = user.getComment();
        }
        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        // Fetch and display admin users from the database
        fetchAndDisplayAdminUsers();

        // Add the main panel to the JFrame
        add(panel);
        setTitle("Admin Page");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fetchAndDisplayAdminUsers() {
        // Fetch admin users from the database
        List<User> adminUsersFromDB = DatabaseManager.getAdminUsersFromDatabase();

        // Display admin users in the table
        updateTableData(adminUsersFromDB);
    }

    private void showDashboard() {
        List<User> allUsers = DatabaseManager.getUsersFromDatabase();
        JFrame dashboardFrame = new JFrame("Dashboard");

        // Create a pie chart
        DefaultPieDataset dataset = createDataset(allUsers);
        JFreeChart chart = ChartFactory.createPieChart(
                "Expense Distribution",
                dataset,
                true,
                true,
                false
        );

        // Customize the chart colors
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Regular Customer", Color.BLUE);
        plot.setSectionPaint("Potential Lead", Color.GREEN);
        plot.setSectionPaint("Opportunity", Color.RED);

        // Create a panel to display the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        dashboardFrame.getContentPane().add(chartPanel);

        // Set up your dashboard frame properties
        dashboardFrame.setSize(600, 400);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the dashboard on close
        dashboardFrame.setVisible(true);

        DefaultPieDataset productDataset = createProductDataset(allUsers);
        JFreeChart productChart = ChartFactory.createPieChart(
                "Product Purchase Distribution",
                productDataset,
                true,
                true,
                false
        );

        // Customize the product chart colors and setup
        PiePlot productPlot = (PiePlot) productChart.getPlot();
        // Customize your plot...

        // Create a panel for the product chart
        ChartPanel productChartPanel = new ChartPanel(productChart);

        // Add the panel to your dashboard layout

        dashboardFrame.setLayout(new GridLayout(1, 2)); // 1 row, 2 columns
        dashboardFrame.add(chartPanel);
        dashboardFrame.add(productChartPanel);

        // Set up the dashboard frame properties
        dashboardFrame.setSize(1200, 600); // Adjusted for two charts
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setVisible(true);
        dashboardFrame.add(productChartPanel);
    }

    private DefaultPieDataset createProductDataset(List<User> allUsers) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> productCounts = new HashMap<>();

        for (User user : allUsers) {
            List<String> products = user.getPurchasedProducts();
            for (String product : products) {
                productCounts.put(product, productCounts.getOrDefault(product, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    private DefaultPieDataset createDataset(List<User> allUsers) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        int countRegular = 0;
        int countLead = 0;
        int countOpportunity = 0;

        for (User user : allUsers) {
            String customerType = user.getCustomerType();
            if ("Regular Customer".equals(customerType)) {
                countRegular++;
            } else if ("Potential Lead".equals(customerType)) {
                countLead++;
            } else if ("Opportunity".equals(customerType)) {
                countOpportunity++;
            }
        }

        dataset.setValue("Regular Customer", countRegular);
        dataset.setValue("Potential Lead", countLead);
        dataset.setValue("Opportunity", countOpportunity);

        return dataset;
    }

    // Method to perform the search based on the entered user code
    private void performSearch(List<User> allAdminUsers) {
        String searchUserCode = searchField.getText().trim().toLowerCase();

        // Filter admin users based on the entered user code
        List<User> matchingUsers = allAdminUsers.stream()
                .filter(user -> user.getUserCode().toLowerCase().contains(searchUserCode))
                .collect(Collectors.toList());

        // Update the table data with matching users
        updateTableData(matchingUsers);
    }

    // Method to update the table data
    private void updateTableData(List<User> userList) {
        String[] columnNames = {"User Code","Name", "Email", "Phone", "Comment"};
        String[][] data = new String[userList.size()][5];
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            data[i][0] = user.getUserCode();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();
            data[i][3] = user.getPhone();
            data[i][4] = user.getComment();
        }
        table = new JTable(data, columnNames);
        scrollPane.setViewportView(table);
    }
}
