package cuongnguyen.demo.controller;

import cuongnguyen.demo.entity.Conversation;
import cuongnguyen.demo.entity.Message;
import cuongnguyen.demo.entity.MessageStatus;
import cuongnguyen.demo.entity.User;
import cuongnguyen.demo.repository.ConversationRepository;
import cuongnguyen.demo.repository.MessageRepository;
import cuongnguyen.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;


@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String getLogin(){


        return "login";
    }

    private boolean checkConversation(User sender, User receiver) {
        boolean check = false;
        if (conversationRepository.countConversationsBySenderAndReceiver(sender, receiver) != 1 && conversationRepository.countConversationsBySenderAndReceiver(receiver, sender) != 1){
            check = true;
        }
        return check;
    }
}
