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

    public Message persistMessage(Message message)
    {
        if (!message.getMessageText().isBlank() && message.getMessageText().length() < 256)
        {
            return messageRepository.save(message);
        }
        else
        {
            return null;
        }
    }

    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId)
    {
        return messageRepository.findMessageByMessageId(messageId);
    }

    @Transactional(rollbackFor = SQLException.class)
    public int deleteMessageById(int messageId)
    {
        return messageRepository.deleteMessageByMessageId(messageId);
    }

    public int updateMessageById(Message message)
    {
        Optional<Message> optionalMessage = messageRepository.findById(message.getMessageId());

        if (message.getMessageText().length() < 256 && 
            !message.getMessageText().isBlank() && optionalMessage.isPresent())
        {
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessageText(message.getMessageText());
            messageRepository.save(message);
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public List<Message> getMessagesByAccountId(int accountId)
    {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
