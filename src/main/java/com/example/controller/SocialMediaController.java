package com.example.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;

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

    //Create an account
    @PostMapping("register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) throws UsernameAlreadyExistsException
    {
        Account newAccount = accountService.persistAccount(account);

        //If account created return 200 and account else return 400 and null
        if (newAccount == null)
        {
            return ResponseEntity.status(400).body(newAccount);
        }
        
        else
        {
            return ResponseEntity.status(200).body(newAccount);
        }
    }

    //Login to account
    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account)
    {
        Account loginAccount = accountService.loginAccount(account);

        //If account logged in return 200 and account else return 401 and null
        if (loginAccount == null)
        {
            return ResponseEntity.status(401).body(loginAccount);
        }

        else
        {
            return ResponseEntity.status(200).body(loginAccount);
        }
    }

    //Create a message
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message)
    {
        Message postedMessage = messageService.persistMessage(message);

        //If message not created for any reason return 400 and null, else return 200 and created message
        if (postedMessage == null)
        {
            return ResponseEntity.status(400).body(postedMessage);
        }
        else
        {
            return ResponseEntity.status(200).body(postedMessage);
        }
    }

    //Get all messages
    @GetMapping("messages")
    public List<Message> getAllMessages()
    {
        return messageService.getAllMessages();
    }

    //Get a message by its ID
    @GetMapping("messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId)
    {
        return messageService.getMessageById(messageId);
    }

    //Delete a message by its ID
    @DeleteMapping("messages/{messageId}")
    public Integer deleteMessageById(@PathVariable int messageId)
    {
        int rowsRemoved = messageService.deleteMessageById(messageId);
        //If something is deleted return the number deleted, else return null
        if (rowsRemoved < 1)
        {
            return null;
        }
        else
        {
            return rowsRemoved;
        }
    }

    //Update a message by its ID
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByMessageId(@PathVariable int messageId, @RequestBody Message message)
    {
        //Set up updated message then attemp to update message
        message.setMessageId(messageId);
        int rowsUpdated = messageService.updateMessageById(message);
        //If something changed return 200 and the number changed, else return code 400 and null
        if (rowsUpdated > 0)
        {
            return ResponseEntity.status(200).body(rowsUpdated);
        }
        else
        {
            return ResponseEntity.status(400).body(null);
        }
    }

    //Get all accounts posted by accountId
    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int accountId)
    {
        return messageService.getMessagesByAccountId(accountId);
    }

    //Handle a UsernameAlreadyExistsException and return code 409
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody String handleUsernameAlradyExists(UsernameAlreadyExistsException ex)
    {
        return ex.getMessage();
    }

    //Handle any SQLExceptions and return a code 400
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleSQLException(SQLException ex)
    {
        return ex.getMessage();
    }

}
