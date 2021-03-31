package cuongnguyen.demo.controller;


import cuongnguyen.demo.dto.MessageDTO;
import cuongnguyen.demo.entity.Conversation;
import cuongnguyen.demo.entity.Message;
import cuongnguyen.demo.entity.MessageStatus;
import cuongnguyen.demo.model.ChatMessage;
import cuongnguyen.demo.repository.ConversationRepository;
import cuongnguyen.demo.repository.MessageRepository;
import cuongnguyen.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityTransaction;
import java.util.Date;

@Controller
public class ChatController {
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @MessageMapping("/chat.sendMessage/{name}")
    @SendTo("/topic/public/{name}")
    public MessageDTO sendMessage(@Payload MessageDTO chatMessage){

        Message message = new Message();
        message.setContent(chatMessage.getContent());
        message.setStatus(MessageStatus.DELIVERED);
        message.setUpdateAt(new Date());
        message.setCreateAt(new Date());
        message.setReceiver(userRepository.findById(chatMessage.getReceiverId()).get());
        message.setSender(userRepository.findById(chatMessage.getSenderId()).get());
        Conversation conversation = conversationRepository.findById(chatMessage.getConversationId()).get();
        message.setConversation(conversation);
        messageRepository.save(message);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{name}")
    @SendTo("/topic/public/{name}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}
