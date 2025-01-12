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

    //Create an account in the database and then return that account
    public Account persistAccount(Account account) throws UsernameAlreadyExistsException
    {
        //make sure username and password fit required parameters
        if (account.getPassword().length() >= 4 && !account.getUsername().isBlank())
        {
            //If an account with specifed username doesn't exist create it, else throw an exception
            if (accountRepository.findAccountByUsername(account.getUsername()) == null)
            {
                return accountRepository.save(account);
            }
            else
            {
                throw new UsernameAlreadyExistsException();
            }
        }
        //Return a null object if creation fails for any reason
        else
        {
            return null;
        }
        
    }

    //Login to an account and return the logged in account
    public Account loginAccount(Account account)
    {
        //Get account
        Optional<Account> optionalAccount = accountRepository.findAccountByUsernameAndPassword(
                                            account.getUsername(), account.getPassword());
        //If account exits return it, else return null
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
