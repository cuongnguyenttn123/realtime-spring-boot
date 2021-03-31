package cuongnguyen.demo.repository;

import cuongnguyen.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "select * from messages where conversation_id = ?1", nativeQuery = true)
    List<Message> findAllByConversation(Integer conversationId);
}
