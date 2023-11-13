import java.util.ArrayList;
import java.util.List;

// Class representing a User entity
class User {
    // Private member variables for user details
    private String name;
    private String email;
    private String phone;
    private String status;
    private String comment;
    private List<String> purchasedProducts;
    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public List<String> getFeedbackAnswers() {
        return feedbackAnswers;
    }

    public void setFeedbackAnswers(List<String> feedbackAnswers) {
        this.feedbackAnswers = feedbackAnswers;
    }

    private String customerType;
    private List<String> feedbackAnswers;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    // Constructor including the purchased products
    public User(String name, String email, String phone, String status, String comment) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.comment = comment;
        this.purchasedProducts = new ArrayList<>();
    }

    // Getter for purchasedProducts
    public List<String> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(List<String> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    // Getter method for the comment attribute
    public String getComment() {
        return comment;
    }

    // Setter method for the comment attribute
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Override the toString method to provide a custom string representation of the User object
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", customerType='" + customerType + '\'' +
                '}';
    }

    // Getter method for the name attribute
    public String getName() {
        return name;
    }

    // Setter method for the name attribute
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for the email attribute
    public String getEmail() {
        return email;
    }

    // Setter method for the email attribute
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for the phone attribute
    public String getPhone() {
        return phone;
    }

    // Setter method for the phone attribute
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter method for the status attribute
    public String getStatus() {
        return status;
    }

    // Setter method for the status attribute
    public void setStatus(String status) {
        this.status = status;
    }
}
