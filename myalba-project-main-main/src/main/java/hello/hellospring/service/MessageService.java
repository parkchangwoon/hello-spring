package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.domain.Message;
import hello.hellospring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //메시지 대화목록 서비스
    public List<Message> getConversation(Long user1Id, Long user2Id) {
        List<Message> conversation = messageRepository.findConversation(user1Id, user2Id);
        markMessagesAsRead(conversation, user1Id);
        return conversation;
    }

    //메시지 보내는 서비스
    public void sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSender(new Member(senderId));
        message.setReceiver(new Member(receiverId));
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);//읽음의 유무 확인
        messageRepository.save(message);
    }

    //받은 사람 ID를 통해 찾는 서비스
    public List<Message> getMessagesForReceiver(Long receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }

    //사용자의 모든 대화 목록을 불러오는 서비스
    public List<Map<String, Object>> getAllConversations(Long userId) {
        List<Message> messages = messageRepository.findAllBySenderIdOrReceiverId(userId);
        Map<Long, List<Message>> groupedMessages = messages.stream()
                .collect(Collectors.groupingBy(message -> {
                    if (message.getSender().getId().equals(userId)) {
                        return message.getReceiver().getId();
                    } else {
                        return message.getSender().getId();
                    }
                }));

        return groupedMessages.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", entry.getKey());
                    map.put("latestMessage", entry.getValue().stream()
                            .max(Comparator.comparing(Message::getSentAt))
                            .orElse(null));
                    return map;
                }).collect(Collectors.toList());
    }

    //메시지의 읽음 유무 표시 서비스
    private void markMessagesAsRead(List<Message> messages, Long userId) {
        for (Message message : messages) {
            if (message.getReceiver().getId().equals(userId) && !message.isRead()) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
    }

    //읽지 않은 메시지 표시 서비스
    public List<Message> getUnreadMessagesForReceiver(Long receiverId) {
        return messageRepository.findAllByReceiverIdAndIsReadFalse(receiverId);
    }
}