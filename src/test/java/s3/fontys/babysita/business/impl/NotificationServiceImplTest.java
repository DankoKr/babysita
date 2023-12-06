package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import s3.fontys.babysita.domain.NotificationMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    public void testBroadcastMessage() {
        NotificationMessage message = new NotificationMessage();
        message.setFrom("sender");
        message.setTo("recipient");
        message.setText("Hello, world!");

        notificationService.broadcastMessage(message);

        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSendToUser(eq("recipient"), eq("/queue/inboxmessages"), any(NotificationMessage.class));
        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSendToUser(eq("sender"), eq("/queue/inboxmessages"), any(NotificationMessage.class));
    }

    @Test
    public void testSendMessageToUser() {
        NotificationMessage message = new NotificationMessage();
        message.setFrom("sender");
        message.setTo("recipient");
        message.setText("Hello, world!");

        notificationService.sendMessageToUser("recipient", message);

        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSendToUser(eq("recipient"), eq("/queue/inboxmessages"), any(NotificationMessage.class));
    }
}
