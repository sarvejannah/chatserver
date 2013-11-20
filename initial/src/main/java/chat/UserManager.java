package chat;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import chat.model.User;
import chatserver.ClientDetails;

public class UserManager {

    private final AtomicLong nextSeq = new AtomicLong(0L);
    private static UserManager um = new UserManager();
    private static Random rnd = new Random(1000);

    private final LinkedHashMap<String, ClientDetails> loginDetails = new LinkedHashMap<String, ClientDetails>(
            100, .75f, true);

    public static UserManager getInstance() {
        return um;
    }

    public long createUser(String userLogin, String name) {
        // TODO replace with real sequence generator
        return nextSeq.incrementAndGet();
    }

    public User getUser(String login) {
        // TODO Load from DB/cache
        return null;
    }

    public void createContact(User user1, User user2) {
        // TODO persist to DB
        user1.addContact(user2);
        user2.addContact(user1);

    }

    public User getUser(long id) {
        // TODO Load from DB/cache
        return null;
    }

    public User removeUser(String id) {
        // TODO remove from DB
        return null;
    }

    public synchronized long loginUser(String userLogin) {

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setUser(getUser(userLogin));
        clientDetails.setSessionId(generateId());
        loginDetails.put(userLogin, clientDetails);

        return clientDetails.getSessionId();

    }

    private Long generateId() {

        return rnd.nextLong();
    }

    public long disconnect(String userLogin) {
        // TODO use this to remove stale socket connections
        return 0;
    }

}
