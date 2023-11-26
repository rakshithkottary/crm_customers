package com.ui.computersales.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;
    private String name;
    private String email;
    private String phone;
    private String comment;
    private String status;
    private String customerType;
    private Double expense;

    @ElementCollection
    private List<Integer> ratings;

    @ElementCollection
    private List<String> purchasedProducts;

}
