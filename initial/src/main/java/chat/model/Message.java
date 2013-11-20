package chat.model;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * Mong DB stores the creation timestamp
 * 
 * 
 */
public class Message {
    @Id
    private String id;

    User sender;
    List<User> receiver; // will handle the group case too
    String message;

    public Message() {
    }

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Message[id=%s, sender='%s', receiver='%s']", id,
                sender, receiver);
    }

}
