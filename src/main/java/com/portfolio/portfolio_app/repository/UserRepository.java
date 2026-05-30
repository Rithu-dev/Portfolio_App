package com.portfolio.portfolio_app.repository;

import com.portfolio.portfolio_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}