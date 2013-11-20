package chat.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {

    public Group findByAlias(String alias);

    public List<User> findUsers(Group receiver);

    public List<Group> findGroups(String alias);
}
