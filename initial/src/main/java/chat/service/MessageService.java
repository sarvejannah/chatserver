package chat.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import chat.model.Message;
import chat.model.User;

public interface MessageService {

    List<Message> getMyMessages(User u, Pageable p);

    Message getMessage(User user, Long msgId);

    void sendMessages(String message, User currentUser, String groupName,
            boolean isGroup);

    List<Message> getMyMessagesWithinDateRange(User currentUser, Date from,
            Date to, Pageable p);

}
