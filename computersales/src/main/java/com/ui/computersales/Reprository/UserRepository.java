package com.ui.computersales.Reprository;

import com.ui.computersales.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndPhone(String email, String phone);

    List<User> findByUserCodeIsNull();
}
