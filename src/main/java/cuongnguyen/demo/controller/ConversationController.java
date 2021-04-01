package cuongnguyen.demo.controller;

import cuongnguyen.demo.dto.ConversationDTO;
import cuongnguyen.demo.dto.MessageDTO;
import cuongnguyen.demo.entity.Conversation;
import cuongnguyen.demo.entity.Message;
import cuongnguyen.demo.entity.User;
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

import java.security.Principal;
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
    public String findAll(ModelMap modelMap, Principal principal){
        User user = userRepository.findByUserName(principal.getName());
        List<ConversationDTO> conversationDTOS  =  converterConversation(conversationRepository.
                findAllConversationByUser(user.getId()), user.getId());
        modelMap.addAttribute("conversations",conversationDTOS);
        modelMap.addAttribute("senderId",user.getId());
        modelMap.addAttribute("senderName",user.getFullName());
        modelMap.addAttribute("receiverId",0);
        modelMap.addAttribute("userId",user.getId());
        modelMap.addAttribute("receiverName","");
        modelMap.addAttribute("conId",0);
        if (!conversationDTOS.isEmpty()){
            List<Message> messageList = messageRepository.findAllByConversation(conversationDTOS.get(0).getId());
            modelMap.addAttribute("messages", converterMessage(messageList));
            modelMap.addAttribute("conId",conversationDTOS.get(0).getId());
            modelMap.addAttribute("receiverId",conversationDTOS.get(0).getReceiverId());
            modelMap.addAttribute("receiverName",conversationDTOS.get(0).getReceiver());
        }

        return "conversation/conversation";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id")Integer id, ModelMap map, Principal principal){
        User user = userRepository.findByUserName(principal.getName());
        Conversation conversation = conversationRepository.findById(id).get();
        List<Message> messageList = messageRepository.findAllByConversation(id);
        map.addAttribute("messages", converterMessage(messageList));
        map.addAttribute("senderId",user.getId());
        map.addAttribute("userId",user.getId());
        map.addAttribute("receiverId",getIdReceiver(user, conversation));
        return "conversation/message-detail";
    }

    private Integer getIdReceiver(User user, Conversation conversation) {
        Integer receiverId = user.getId();
        if (conversation.getSender().getId() == user.getId()){
            receiverId = conversation.getReceiver().getId();
        }

        return receiverId;
    }

    private List<ConversationDTO> converterConversation(List<Conversation> allBySenderOrReceiver, Integer idUser) {

        List<ConversationDTO> conversationDTOS = new ArrayList<>();
        for (Conversation conversation: allBySenderOrReceiver
             ) {
            ConversationDTO conversationDTO = new ConversationDTO();
            conversationDTO.setId(conversation.getId());
            conversationDTO.setName(conversation.getName());
            if (idUser == conversation.getSender().getId()){
                conversationDTO.setReceiver(conversation.getReceiver().getFullName());
                conversationDTO.setReceiverId(conversation.getReceiver().getId());
                conversationDTO.setSenderId(conversation.getSender().getId());
                conversationDTO.setSender(conversation.getSender().getFullName());
            }else {
                conversationDTO.setSender(conversation.getReceiver().getFullName());
                conversationDTO.setSenderId(conversation.getReceiver().getId());
                conversationDTO.setReceiverId(conversation.getSender().getId());
                conversationDTO.setReceiver(conversation.getSender().getFullName());
            }

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
            messageDTO.setReceiver(message.getReceiver().getFullName());
            messageDTO.setReceiverId(message.getReceiver().getId());
            messageDTO.setSenderId(message.getSender().getId());
            messageDTO.setContent(message.getContent());
            messageDTO.setSender(message.getSender().getFullName());
            messageDTOS.add(messageDTO);
        }
        return messageDTOS;
    }
}
