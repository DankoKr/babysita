package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import s3.fontys.babysita.business.NotificationService;
import s3.fontys.babysita.domain.NotificationMessage;

@Service
@AllArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void broadcastMessage(NotificationMessage message) {
        // Send to the recipient
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/inboxmessages", message);
        // Send back to the sender
        messagingTemplate.convertAndSendToUser(message.getFrom(), "/queue/inboxmessages", message);
    }

    @Override         // Message specific user
    public void sendMessageToUser(String username, NotificationMessage message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/inboxmessages", message);
    }
}
