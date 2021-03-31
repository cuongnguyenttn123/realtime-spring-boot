package cuongnguyen.demo.dto;

import cuongnguyen.demo.entity.MessageStatus;
import lombok.Data;

@Data
public class MessageDTO {
    private Integer id;

    private String sender;

    private String receiver;

    private Integer conversationId;

    private String content;

    private MessageStatus status;

    private Integer senderId;

    private Integer receiverId;
}
