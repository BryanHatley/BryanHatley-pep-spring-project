package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> 
{
    //Fimd a message using its ID
    Message findMessageByMessageId(int messageId);

    //Delete a message using its ID
    int deleteMessageByMessageId(int messageId);

    //Find messages posted by a specific user
    List<Message> findMessagesByPostedBy(int postedBy);
}
