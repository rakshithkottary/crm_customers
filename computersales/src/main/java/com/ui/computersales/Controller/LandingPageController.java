package com.ui.computersales.Controller;

import com.ui.computersales.Entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LandingPageController {
    private final List<User> userList = new ArrayList<>();
    private final List<User> adminList = new ArrayList<>();

    @GetMapping("/")
    public String landingPage(Model model) {
        model.addAttribute("userList", userList);
        model.addAttribute("adminList", adminList);
        return "landing";
    }

    @GetMapping("/customer")
    public String customerForm() {
        return "redirect:/customerForm";
    }

    @GetMapping("/admin")
    public String adminLogin() {
        return "adminLogin";
    }

    @PostMapping("/admin")
    public String adminSubmit(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "redirect:/adminPage";
        } else {
            return "redirect:/?error";
        }
    }
}
