package chat.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConverstationRepository extends
        MongoRepository<Conversation, String> {

    public Conversation findAllById(Long id);

}
