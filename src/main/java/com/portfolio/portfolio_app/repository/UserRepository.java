package com.portfolio.portfolio_app.repository;

import java.util.Optional;
import com.portfolio.portfolio_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}