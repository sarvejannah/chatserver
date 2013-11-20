package chat.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UndeliveredMessageRepository extends
        MongoRepository<Message, String> {

    public UndeliveredMessage findById(Long id);

    // Find by receiver And collate By Sender
    // Once succesful change status to delivered
    // cleanup can be done by seperate process
    public List<UndeliveredMessage> findByReceiver(User receiver);

}
