package s3.fontys.babysita.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import s3.fontys.babysita.business.NotificationService;
import s3.fontys.babysita.domain.NotificationMessage;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class NotificationController {
    private final NotificationService notificationService;

    @MessageMapping("/send") //Will react only to the SEND messages to the destination
    public void sendMessage(@Payload NotificationMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // Extract the username of the sender
        String username = Objects.requireNonNull(headerAccessor.getUser()).getName();

        // Broadcast the message to the intended recipient(s)
        notificationService.broadcastMessage(message);

        // Also send the message back to the sender
        notificationService.sendMessageToUser(username, message);
    }
}
