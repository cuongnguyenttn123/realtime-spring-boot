package cuongnguyen.demo.repository;

import cuongnguyen.demo.entity.Conversation;
import cuongnguyen.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Integer countConversationsBySenderAndReceiver(User sender, User receiver);

    @Query(value = "select * from realtime.conversations where receiver_id = ?1 or sender_id = ?1 order by update_at desc ", nativeQuery = true)
    List<Conversation> findAllConversationByUser(Integer userId);
}
