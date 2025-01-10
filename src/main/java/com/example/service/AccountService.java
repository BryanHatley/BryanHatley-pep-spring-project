package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService 
{
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account)
    {
        return accountRepository.save(account);
    }

    public Account getAccountByUsername(Account account)
    {
        Optional<Account> optionalAccount = accountRepository.findAccountByUsernameAndPassword(
                                            account.getUsername(), account.getPassword());
        
        if(optionalAccount.isPresent())
        {
            return optionalAccount.get();
        }
        else
        {
            return null;
        }
            
    }
}
