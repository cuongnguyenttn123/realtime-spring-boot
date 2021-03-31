package cuongnguyen.demo.dto;

import cuongnguyen.demo.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class ConversationDTO {
    private Integer id;
    private String name;
    private String sender;
    private Integer senderId;
    private String receiver;
    private Integer receiverId;

}
