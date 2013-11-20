package chat.service;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import chat.model.GroupRepository;
import chat.model.MessageRepository;
import chat.model.UserRepository;
import chatserver.ClientDetails;

public class UserServiceImpl implements UserService {
    AbstractApplicationContext context = new AnnotationConfigApplicationContext(
            UserServiceImpl.class);

    // @Autowired
    MessageRepository repository = context.getBean(MessageRepository.class);

    @Autowired
    ClientCache cache;

    UserRepository userRepo = context.getBean(UserRepository.class);
    GroupRepository groupRepo = context.getBean(GroupRepository.class);
    private static Random rnd = new Random(1000);
    private Lock lock = new ReentrantLock();

    public Long authenticate(String userLogin) {

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setUser(userRepo.findByUserLogin(userLogin));

        lock.lock();

        clientDetails.setSessionId(generateId());
        cache.put(userLogin, clientDetails);

        lock.unlock();

        return clientDetails.getSessionId();

    }

    public ClientDetails getClientDetails(String userLogin) {

        lock.lock();

        ClientDetails cd = cache.get(userLogin);

        lock.unlock();

        return cd;
    }

    private Long generateId() {

        return rnd.nextLong();
    }

}
