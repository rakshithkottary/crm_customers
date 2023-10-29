import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminPage extends JFrame {
    private List<User> adminUserList;
    private JTable table;
    private JScrollPane scrollPane;
    private JTextField nameField, emailField, phoneField, expenseField;

    public AdminPage(List<User> userList) {
        this.adminUserList = new ArrayList<>(userList); // Create a separate list for the admin page

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        phoneField = new JTextField(15);
        expenseField = new JTextField(15);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Expense:"));
        inputPanel.add(expenseField);
        panel.add(inputPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Customer");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String expenseStr = expenseField.getText();
                double expense = Double.parseDouble(expenseStr);
                String comment = (expense > 10000) ? "User eligible" : "User not eligible";
                User user = new User(name, email, phone, "Regular Customer", comment);
                adminUserList.add(user);
                updateTableData();
            }
        });

        JButton updateButton = new JButton("Update Customer");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    User user = adminUserList.get(selectedRow);
                    user.setName(nameField.getText());
                    user.setEmail(emailField.getText());
                    user.setPhone(phoneField.getText());
                    updateTableData();
                }
            }
        });

        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    adminUserList.remove(selectedRow);
                    updateTableData();
                }
            }
        });

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

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewCustomersButton);

        panel.add(buttonPanel);

        String[] columnNames = {"Name", "Email", "Phone", "Comment"};
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

        add(panel);
        setTitle("Admin Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void updateTableData() {
        String[] columnNames = {"Name", "Email", "Phone", "Comment"};
        String[][] data = new String[adminUserList.size()][4];
        for (int i = 0; i < adminUserList.size(); i++) {
            User user = adminUserList.get(i);
            data[i][0] = user.getName();
            data[i][1] = user.getEmail();
            data[i][2] = user.getPhone();
            data[i][3] = user.getComment();
        }
        table = new JTable(data, columnNames);
        scrollPane.setViewportView(table);
    }

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

