package com.ui.computersales.Controller;

import com.ui.computersales.Entity.User;
import com.ui.computersales.Reprository.UserRepository;
import com.ui.computersales.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adminPage")
public class AdminPageController {

    private final UserRepository userRepository;

    @Autowired
    public AdminPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showAdminPage(Model model) {
        Iterable<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("newUser", new User());
        return "adminPage";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User newUser) {
        newUser.setUserCode(Utility.generateUniqueCode());
        newUser.setComment(newUser.getExpense()<10000?"Not Eligible":"Eligible");
        userRepository.save(newUser);
        return "redirect:/adminPage";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/adminPage";
    }

    @PostMapping("/updateUserField")
    @ResponseBody
    public String updateUserField(@RequestParam Long userId, @RequestParam String field, @RequestParam String value) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            switch (field) {
                case "name":
                    user.setName(value);
                    break;
                case "email":
                    user.setEmail(value);
                    break;
                case "phone":
                    user.setPhone(value);
                    break;
                case "expense":
                    user.setExpense(Double.parseDouble(value));
                    user.setComment(Double.parseDouble(value)<10000?"Not Eligible":"Eligible");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + field);
            }
            userRepository.save(user);
            return "Field updated successfully";
        } catch (Exception e) {
            return "Error updating field: " + e.getMessage();
        }
    }

    @GetMapping("/viewCustomers")
    public String viewCustomers(Model model) {
        List<User> customersWithoutUserCode = userRepository.findByUserCodeIsNull();
        model.addAttribute("customersWithoutUserCode", customersWithoutUserCode);

        Map<String, Long> customerTypeCounts = getCustomerTypeCounts();
        model.addAttribute("customerTypeCounts", customerTypeCounts);

        return "viewCustomers";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Map<String, Long> customerTypeCounts = getCustomerTypeCounts();
        Map<String, Long> productCounts = getProductCounts();

        model.addAttribute("customerTypeCounts", customerTypeCounts);
        model.addAttribute("productCounts", productCounts);

        return "dashboard";
    }

    private Map<String, Long> getCustomerTypeCounts() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Long> customerTypeCounts = new HashMap<>();
        customerTypeCounts.put("Opportunity", allUsers.stream().filter(u -> "Opportunity".equals(u.getCustomerType())).count());
        customerTypeCounts.put("Potential Lead", allUsers.stream().filter(u -> "Potential Lead".equals(u.getCustomerType())).count());
        customerTypeCounts.put("Regular Customer", allUsers.stream().filter(u -> "Regular Customer".equals(u.getCustomerType())).count());
        return customerTypeCounts;
    }

    private Map<String, Long> getProductCounts() {
        List<User> allUsers = userRepository.findAll();

        // Flatten the purchasedProducts lists from all users into a single list
        List<String> allProducts = allUsers.stream()
                .flatMap(user -> user.getPurchasedProducts().stream())
                .collect(Collectors.toList());

        // Count occurrences of each product
        Map<String, Long> productCounts = new HashMap<>();
        for (String product : allProducts) {
            productCounts.merge(product, 1L, Long::sum);
        }

        return productCounts;
    }
}
