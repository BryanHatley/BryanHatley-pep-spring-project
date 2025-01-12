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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
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

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message)
    {
        Message postedMessage = messageService.persistMessage(message);

        if (postedMessage == null)
        {
            return ResponseEntity.status(400).body(postedMessage);
        }
        else
        {
            return ResponseEntity.status(200).body(postedMessage);
        }
    }

    @GetMapping("messages")
    public List<Message> getAllMessages()
    {
        return messageService.getAllMessages();
    }

    @GetMapping("messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId)
    {
        return messageService.getMessageById(messageId);
    }

    @DeleteMapping("messages/{messageId}")
    public Integer deleteMessageById(@PathVariable int messageId)
    {
        int rowsRemoved = messageService.deleteMessageById(messageId);
        if (rowsRemoved < 1)
        {
            return null;
        }
        else
        {
            return rowsRemoved;
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByMessageId(@PathVariable int messageId, @RequestBody Message message)
    {
        message.setMessageId(messageId);
        int rowsUpdated = messageService.updateMessageById(message);

        if (rowsUpdated > 0)
        {
            return ResponseEntity.status(200).body(rowsUpdated);
        }
        else
        {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int accountId)
    {
        return messageService.getMessagesByAccountId(accountId);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody String handleUsernameAlradyExists(UsernameAlreadyExistsException ex)
    {
        return ex.getMessage();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleSQLException(SQLException ex)
    {
        return ex.getMessage();
    }

}
