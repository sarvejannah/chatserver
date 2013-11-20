package chat.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

    public Message findById(Long id);

    // User could be sender or receiver
    public List<Message> findByUser(User user);

    // User could be sender or receiver
    public List<Message> findByUserAndDateRange(User user, Date from, Date to,
            Pageable p);

    public List<Message> findByUser(User u, Pageable p);

}
