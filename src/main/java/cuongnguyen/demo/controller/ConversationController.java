package cuongnguyen.demo.controller;

import cuongnguyen.demo.dto.ConversationDTO;
import cuongnguyen.demo.dto.MessageDTO;
import cuongnguyen.demo.entity.Conversation;
import cuongnguyen.demo.entity.Message;
import cuongnguyen.demo.repository.ConversationRepository;
import cuongnguyen.demo.repository.MessageRepository;
import cuongnguyen.demo.repository.UserRepository;
import cuongnguyen.demo.utils.RandomStringExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/conversations")
@Controller
public class ConversationController {
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping
    public String findAll(ModelMap modelMap){
        List<ConversationDTO> conversationDTOS  =  converterConversation(conversationRepository.
                findAllConversationByUser(2));
        modelMap.addAttribute("conversations",conversationDTOS);
        modelMap.addAttribute("senderId",0);
        modelMap.addAttribute("receiverId",0);
        modelMap.addAttribute("conId",0);
        if (!conversationDTOS.isEmpty()){
            List<Message> messageList = messageRepository.findAllByConversation(conversationDTOS.get(0).getId());
            modelMap.addAttribute("messages", converterMessage(messageList));
            modelMap.addAttribute("conId",conversationDTOS.get(0).getId());
            modelMap.addAttribute("senderId",conversationDTOS.get(0).getSenderId());
            modelMap.addAttribute("receiverId",conversationDTOS.get(0).getReceiverId());
        }

        return "conversation/conversation";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id")Integer id, ModelMap map){
        Conversation conversation = conversationRepository.findById(id).get();
        List<Message> messageList = messageRepository.findAllByConversation(id);
        map.addAttribute("messages", converterMessage(messageList));
        map.addAttribute("senderId",conversation.getSender().getId());
        map.addAttribute("receiverId",conversation.getReceiver().getId());
        return "conversation/message-detail";
    }

    private List<ConversationDTO> converterConversation(List<Conversation> allBySenderOrReceiver) {
        List<ConversationDTO> conversationDTOS = new ArrayList<>();
        for (Conversation conversation: allBySenderOrReceiver
             ) {
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setId(conversation.getId());
            conversationDTO.setName(conversation.getName());
            conversationDTO.setReceiver(conversation.getReceiver().fullName());
            conversationDTO.setReceiverId(conversation.getReceiver().getId());
            conversationDTO.setSenderId(conversation.getSender().getId());
            conversationDTO.setSender(conversation.getSender().fullName());
            conversationDTOS.add(conversationDTO);
        }
        return conversationDTOS;
    }

    private List<MessageDTO> converterMessage(List<Message> messageList) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        for (Message message: messageList
        ) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setReceiver(message.getReceiver().fullName());
            messageDTO.setReceiverId(message.getReceiver().getId());
            messageDTO.setSenderId(message.getSender().getId());
            messageDTO.setContent(message.getContent());
            messageDTO.setSender(message.getSender().fullName());
            messageDTOS.add(messageDTO);
        }
        return messageDTOS;
    }
}
