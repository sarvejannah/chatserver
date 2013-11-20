package chat.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    public User findByUserLogin(String login);

    public User findByName(String name);

    public List<User> findContacts(String login);

    public List<Group> findGroupContacts(String login);
}
