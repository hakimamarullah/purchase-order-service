package com.starline.purchase.order.repository;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 9:43 PM
@Last Modified 10/15/2024 9:43 PM
Version 1.0
*/

import com.starline.purchase.order.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findFirstByEmail(String email);

    int deleteUserById(Integer id);
}
