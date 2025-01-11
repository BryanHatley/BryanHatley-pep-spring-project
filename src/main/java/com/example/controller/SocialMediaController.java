package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController 
{
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) throws UsernameAlreadyExistsException
    {
        Account newAccount = accountService.persistAccount(account);

        if (account == null)
        {
            return ResponseEntity.status(400).body(null);
        }
        
        else
        {
            return ResponseEntity.status(200).body(newAccount);
        }
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account)
    {
        Account loginAccount = accountService.loginAccount(account);

        if (loginAccount == null)
        {
            return ResponseEntity.status(401).body(loginAccount);
        }

        else
        {
            return ResponseEntity.status(200).body(loginAccount);
        }
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody String handleUsernameAlradyExists(UsernameAlreadyExistsException ex)
    {
        return ex.getMessage();
    }

}
