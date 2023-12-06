package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.NotificationMessage;

public interface NotificationService {
    void broadcastMessage(NotificationMessage message);
    void sendMessageToUser(String username, NotificationMessage message);
}
