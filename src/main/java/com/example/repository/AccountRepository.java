package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> 
{
    //Finds an account using its username and password
    Optional<Account> findAccountByUsernameAndPassword(String username, String Password);

    //Finds an account using username only
    Account findAccountByUsername(String username);
}
