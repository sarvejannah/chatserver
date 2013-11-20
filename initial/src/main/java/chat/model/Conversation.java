package chat.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Mong DB stores the creation timestamp
 * 
 * 
 */
public class Conversation {
    @Id
    private String id;

    @DBRef
    List<Message> messages;

    public Conversation() {
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return String.format("Conversation[id=%s, message='%s']", id, messages);
    }

}
