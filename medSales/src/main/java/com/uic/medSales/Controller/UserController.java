package com.uic.medSales.Controller;

import com.uic.medSales.Entity.User;
import com.uic.medSales.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            // Check if the user already exists by email
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                // Update existing user
                User updatedUser = existingUser.get();
                updatedUser.setName(user.getName());
                updatedUser.setPhone(user.getPhone());

                userRepository.save(updatedUser);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                // Add new user
                userRepository.save(user);
                return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding or updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}