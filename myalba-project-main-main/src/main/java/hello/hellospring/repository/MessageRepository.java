package hello.hellospring.repository;

import hello.hellospring.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId ORDER BY m.sentAt DESC")
    List<Message> findAllBySenderIdOrReceiverId(@Param("userId") Long userId);//메시지 모두 불러오는 메서드

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR (m.sender.id = :user2Id AND m.receiver.id = :user1Id) ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);//메시지 대화 목록 찾는 메서드

    List<Message> findByReceiverId(Long receiverId);//메시지 받는 회원 ID를 통해 찾는 메서드

    List<Message> findAllByReceiverIdAndIsReadFalse(Long receiverId);//메시지의 읽음 유무를 찾는 메서드
}
