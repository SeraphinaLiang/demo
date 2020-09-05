package com.example.strangeclass.dao;

import com.example.strangeclass.entity.TicAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicAccountRepository extends JpaRepository<TicAccount, Long> {
    List<TicAccount> findAllByWhoUsedIsNull();

    TicAccount findByWhoUsed(String whoUsed);
}
