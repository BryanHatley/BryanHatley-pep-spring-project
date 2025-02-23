package com.example.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService 
{
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    //Create a message in the database
    public Message persistMessage(Message message)
    {
        //If the message text fits parameters create message and return it, else return null
        if (!message.getMessageText().isBlank() && message.getMessageText().length() < 256)
        {
            return messageRepository.save(message);
        }
        else
        {
            return null;
        }
    }

    //Get all messages in Database
    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    //Get a message using its ID
    public Message getMessageById(int messageId)
    {
        return messageRepository.findMessageByMessageId(messageId);
    }

    //Delete a message using its ID, returns the number of Rows deleted
    @Transactional(rollbackFor = SQLException.class)
    public int deleteMessageById(int messageId)
    {
        return messageRepository.deleteMessageByMessageId(messageId);
    }

    //Update a message by its ID
    public int updateMessageById(Message message)
    {
        //Get message
        Optional<Message> optionalMessage = messageRepository.findById(message.getMessageId());

        //If updated messageText fits parameters update message and return 1, else return 0
        if (message.getMessageText().length() < 256 && 
            !message.getMessageText().isBlank() && optionalMessage.isPresent())
        {
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessageText(message.getMessageText());
            messageRepository.save(message);
            //1 represents that 1 row of data has changed, 0 means no rows changed
            return 1;
        }
        else
        {
            return 0;
        }
    }

    //Get a all messages posted by user with accountID
    public List<Message> getMessagesByAccountId(int accountId)
    {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
