import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/uic_computer_sales";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to create the necessary tables if they don't exist
    public static void createTables() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Create users table
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(15) NOT NULL," +
                    "status VARCHAR(255)," +
                    "comment TEXT," +
                    "customer_type VARCHAR(255)," +
                    "purchasedProducts TEXT" +
                    ")";

            statement.executeUpdate(createUserTable);

            // Create admin_users table
            String createAdminUserTable = "CREATE TABLE IF NOT EXISTS admin_users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(15) NOT NULL," +
                    "status VARCHAR(255)," +
                    "comment TEXT," +
                    "customer_type VARCHAR(255)," +
                    "user_code VARCHAR(255) NOT NULL UNIQUE" + // Assuming user_code should be unique
                    ")";
            statement.executeUpdate(createAdminUserTable);

            // Create feedback_answers table
            String createFeedbackTable = "CREATE TABLE IF NOT EXISTS feedback_answers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "question VARCHAR(255) NOT NULL," +
                    "answer TEXT," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")";
            statement.executeUpdate(createFeedbackTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save user data to MySQL database
    public static void saveUserToDatabase(User user) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            createTables();
            // Insert into users table
            String insertUserSql = "INSERT INTO users (name, email, phone, status, comment, customer_type, purchasedProducts) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getName());
                userStatement.setString(2, user.getEmail());
                userStatement.setString(3, user.getPhone());
                userStatement.setString(4, user.getStatus());
                userStatement.setString(5, user.getComment());
                userStatement.setString(6, user.getCustomerType());
                userStatement.setString(7, String.join(",", user.getPurchasedProducts()));

                userStatement.executeUpdate();

                // Get the generated user ID
                int userId;
                try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt(1);
                    } else {
                        throw new SQLException("Failed to get generated user ID.");
                    }
                }

                // Insert into feedback_answers table
                String insertFeedbackSql = "INSERT INTO feedback_answers (user_id, question, answer) VALUES (?, ?, ?)";
                try (PreparedStatement feedbackStatement = connection.prepareStatement(insertFeedbackSql)) {
                    for (int i = 0; i < user.getFeedbackAnswers().size(); i++) {
                        feedbackStatement.setInt(1, userId);
                        feedbackStatement.setString(2, "Question " + (i + 1)); // Modify accordingly
                        feedbackStatement.setString(3, user.getFeedbackAnswers().get(i));

                        feedbackStatement.addBatch();
                    }

                    feedbackStatement.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve user data from MySQL database
    public static List<User> getUsersFromDatabase() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String selectUsersSql = "SELECT * FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectUsersSql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        String status = resultSet.getString("status");
                        String comment = resultSet.getString("comment");
                        String customerType = resultSet.getString("customer_type");
                        String purchasedProductsString = resultSet.getString("purchasedProducts");
                        List<String> purchasedProducts = Arrays.asList(purchasedProductsString.split(","));

                        // Retrieve feedback answers for the user
                        List<String> feedbackAnswers = getFeedbackAnswers(userId);

                        User user = new User(name, email, phone, status, comment);
                        user.setCustomerType(customerType);
                        user.setPurchasedProducts(purchasedProducts);
                        user.setFeedbackAnswers(feedbackAnswers);

                        userList.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    // Method to retrieve feedback answers for a specific user
    private static List<String> getFeedbackAnswers(int userId) {
        List<String> feedbackAnswers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String selectFeedbackSql = "SELECT * FROM feedback_answers WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectFeedbackSql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String answer = resultSet.getString("answer");
                        feedbackAnswers.add(answer);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackAnswers;
    }

    // Method to save admin user data to MySQL database
    public static void saveAdminUserToDatabase(User user) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            createTables();
            // Insert into admin_users table
            String insertAdminUserSql = "INSERT INTO admin_users (name, email, phone, status, comment, customer_type, user_code) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStatement = connection.prepareStatement(insertAdminUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getName());
                userStatement.setString(2, user.getEmail());
                userStatement.setString(3, user.getPhone());
                userStatement.setString(4, user.getStatus());
                userStatement.setString(5, user.getComment());
                userStatement.setString(6, user.getCustomerType());
                userStatement.setString(7, user.getUserCode());

                userStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update admin user data in MySQL database
    public static void updateAdminUserInDatabase(User user) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // Update admin_users table
            String updateAdminUserSql = "UPDATE admin_users SET name=?, email=?, phone=?, status=?, comment=?, customer_type=? WHERE user_code=?";
            try (PreparedStatement userStatement = connection.prepareStatement(updateAdminUserSql)) {
                userStatement.setString(1, user.getName());
                userStatement.setString(2, user.getEmail());
                userStatement.setString(3, user.getPhone());
                userStatement.setString(4, user.getStatus());
                userStatement.setString(5, user.getComment());
                userStatement.setString(6, user.getCustomerType());
                userStatement.setString(7, user.getUserCode());

                userStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete admin user data from MySQL database
    public static void deleteAdminUserFromDatabase(User user) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // Delete from admin_users table
            String deleteAdminUserSql = "DELETE FROM admin_users WHERE user_code=?";
            try (PreparedStatement userStatement = connection.prepareStatement(deleteAdminUserSql)) {
                userStatement.setString(1, user.getUserCode());

                userStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve admin user data from MySQL database
    public static List<User> getAdminUsersFromDatabase() {
        List<User> adminUserList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String selectAdminUsersSql = "SELECT * FROM admin_users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectAdminUsersSql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String userCode = resultSet.getString("user_code");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        String status = resultSet.getString("status");
                        String comment = resultSet.getString("comment");
                        String customerType = resultSet.getString("customer_type");

                        User adminUser = new User(name, email, phone, status, comment);
                        adminUser.setCustomerType(customerType);
                        adminUser.setUserCode(userCode);

                        adminUserList.add(adminUser);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adminUserList;
    }
}
