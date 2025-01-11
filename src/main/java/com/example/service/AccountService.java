package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.UsernameAlreadyExistsException;
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

    public Account persistAccount(Account account) throws UsernameAlreadyExistsException
    {
        if (account.getPassword().length() >= 4 && !account.getUsername().isBlank())
        {
            if (accountRepository.findAccountByUsername(account.getUsername()) == null)
            {
                return accountRepository.save(account);
            }
            else
            {
                throw new UsernameAlreadyExistsException();
            }
        }
        else
        {
            return null;
        }
        
    }

    public Account loginAccount(Account account)
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
