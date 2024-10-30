package hello.hellospring.controller;

import hello.hellospring.domain.Message;
import hello.hellospring.domain.Member;
import hello.hellospring.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages/send/{receiverId}")//상대방에게 메시지 보내는 메서드(receiverId=상대방 아이디)
    public ResponseEntity<String> sendMessage(@PathVariable("receiverId") Long receiverId, @RequestBody Map<String, String> request, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String content = request.get("content");

        messageService.sendMessage(loggedInMember.getId(), receiverId, content);
        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }

    @GetMapping("/messages")//메시지 목록 호출 메서드
    public ResponseEntity<List<Message>> getMessages(HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Message> messages = messageService.getMessagesForReceiver(loggedInMember.getId());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/conversation/{otherUserId}")//다른 회원과의 메시지 대화 내용 출력 메서드
    public ResponseEntity<List<Message>> getConversation(@PathVariable("otherUserId") Long otherUserId, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Message> conversation = messageService.getConversation(loggedInMember.getId(), otherUserId);
        return ResponseEntity.ok(conversation);
    }


    @GetMapping("/messages/conversations")//자신이 진행한 메시지 대화 목록 출력 메서드
    public ResponseEntity<List<Map<String, Object>>> getAllConversations(HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Map<String, Object>> conversations = messageService.getAllConversations(loggedInMember.getId());
        return ResponseEntity.ok(conversations);
    }
}
