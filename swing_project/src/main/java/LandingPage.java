import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LandingPage extends JFrame {
    private List<User> userList;
    private List<User> adminList;

    public LandingPage(List<User> userList, List<User> adminList) {
        this.userList = userList;
        this.adminList = adminList;

        // Create a custom panel for the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        // Create a top panel for the heading
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headingLabel = new JLabel("WELCOME TO UIC MED SALES");
        headingLabel.setFont(new Font("Serif", Font.BOLD, 40));
        headingLabel.setForeground(Color.white); // Set the text color to red
        topPanel.add(headingLabel);


        // Create a center panel for the buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Add spacing between components

        ImageIcon userIcon = new ImageIcon("C:\\Users\\sambr\\Downloads\\icons8-user-icon-70.png");
        ImageIcon adminIcon = new ImageIcon("C:\\Users\\sambr\\Downloads\\icons8-administrator-male-70.png");

        JButton customerButton = new JButton("I am a customer", userIcon);
        JButton adminButton = new JButton("I am an admin", adminIcon);

        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LandingPage.this.setVisible(false);
                CRMForm crmForm = new CRMForm(userList, LandingPage.this);
            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputName = JOptionPane.showInputDialog(LandingPage.this, "Enter Username:");
                String inputPassword = JOptionPane.showInputDialog(LandingPage.this, "Enter Password:");
                if (inputName != null && inputPassword != null && inputName.equals("admin") && inputPassword.equals("admin")) {
                    LandingPage.this.setVisible(false);
                    AdminPage adminPage = new AdminPage(adminList, userList, LandingPage.this);
                } else {
                    JOptionPane.showMessageDialog(LandingPage.this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        centerPanel.add(customerButton);
        centerPanel.add(adminButton);

        // Add the top and center panels to the background panel
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        add(backgroundPanel);
        setTitle("Landing Page");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Custom JPanel for background image
    class BackgroundPanel extends JPanel {
        private BufferedImage background;

        public BackgroundPanel() {
            try {
                background = ImageIO.read(new File("C:\\Users\\sambr\\Downloads\\UI background.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
