package com.ui.computersales.Controller;

import com.ui.computersales.Entity.User;
import com.ui.computersales.Reprository.UserRepository;
import com.ui.computersales.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomerFormController {

    private final UserRepository userRepository;

    @Autowired
    public CustomerFormController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/customerForm")
    public String showCustomerForm(Model model) {
        User user = new User();
        user.setRatings(Arrays.asList(0, 0, 0, 0, 0));
        user.setPurchasedProducts(new ArrayList<>());
        model.addAttribute("user", user);
        model.addAttribute("questions", getQuestions());
        return "customerForm";
    }

    @PostMapping("/customerForm")
    public String submitCustomerForm(User user) {
        // Validate email and phone number formats
        if (!Utility.isValidEmail(user.getEmail()) || !Utility.isValidPhoneNumber(user.getPhone())) {
            return "redirect:/customerForm?error";
        }

        // Handle the rest of the form submission logic
        user.setStatus("status");
        user.setComment("comment");
        user.setCustomerType(calculateCustomerType(user.getRatings())); // Set the customer type

        // Save the user to the database using the repository
        userRepository.save(user);

        // Print the user list to the console (for debugging purposes)
        Iterable<User> allUsers = userRepository.findAll();
        allUsers.forEach(System.out::println);
        System.out.println("*************************************");

        return "redirect:/"; // Redirect to the landing page after submission
    }

    private List<String> getQuestions() {
        // Define your list of questions dynamically
        return Arrays.asList(
                "How satisfied are you with the computer hardware products you purchased from us?",
                "How likely are you to recommend our computer hardware products to others?",
                "How likely are you to recommend our computer hardware products to others1?",
                "How likely are you to recommend our computer hardware products to others2?",
                "Are there any specific brands or types of computer hardware you would like us to offer in the future?"
        );
    }

    private String calculateCustomerType(List<Integer> ratings) {
        if (ratings != null && ratings.size() == 5) {
            int ratingsSum = ratings.stream().mapToInt(Integer::intValue).sum();
            float ratio = (float) ratingsSum / 25; // Assuming each question can have a maximum rating of 5
            if (ratio >= 0.8) return "Opportunity";
            if (ratio >= 0.5) return "Potential Lead";
        }
        return "Regular Customer";
    }
}
