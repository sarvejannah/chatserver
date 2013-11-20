package chat.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.domain.Pageable;

import chat.model.Group;
import chat.model.GroupRepository;
import chat.model.Message;
import chat.model.MessageRepository;
import chat.model.User;
import chat.model.UserRepository;
import chatserver.ClientDetails;

public class MessageServiceImpl implements MessageService {
    AbstractApplicationContext context = new AnnotationConfigApplicationContext(
            MessageServiceImpl.class);

    public static final String SERVER_HOSTNAME = "chatserver";

    public static final int SERVER_PORT = 2002;

    // @Autowired
    MessageRepository repository = context.getBean(MessageRepository.class);

    UserRepository userRepo = context.getBean(UserRepository.class);

    GroupRepository groupRepo = context.getBean(GroupRepository.class);

    @Autowired
    UserService userService;

    public List<Message> getMyMessages(User u, Pageable p) {

        return repository.findByUser(u, p);
    }

    @Override
    public Message getMessage(User user, Long msgId) {
        // TODO How to ensure msgId belongs to user
        return repository.findById(msgId);
    }

    public void sendMessages(String message, User currentUser, String receiver,
            boolean isGroup) {
        if (!isGroup) {
            String msgToDeliver = createMessage(message, currentUser,
                    userRepo.findByUserLogin(receiver));

            dispatchMessage(msgToDeliver);

        } else {

            String msgToDeliver = createMessage(message, currentUser,
                    groupRepo.findByAlias(receiver));

        }

    }

    private void dispatchMessage(String msgToDeliver) {
        Socket socket;
        try {
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            PrintWriter mOut = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            mOut.println(msgToDeliver);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String createMessage(String message, User currentUser, User receiver) {

        ClientDetails senderDetails = userService.getClientDetails(currentUser
                .getLogin());
        ClientDetails receiverDetails = userService
                .getClientDetails(currentUser.getLogin());
        // sample message but could be done in different ways
        return "<sender:" + currentUser.getLogin() + "><Sender host:"
                + senderDetails.getHost() + "><receiver:" + receiver.getLogin()
                + "><Receiver host:" + receiverDetails.getHost()
                + "><Receiver port:" + receiverDetails.getPort() +
                // ...other details such as userAgent so we can detect mobile
                "<Message: " + message + ">";

    }

    private String createMessage(String message, User currentUser,
            Group receiver) {

        ClientDetails senderDetails = userService.getClientDetails(currentUser
                .getLogin());

        StringBuffer receiverData = new StringBuffer();

        // not ideal for a large group
        // better to use multicast
        for (User u : groupRepo.findUsers(receiver)) {
            ClientDetails receiverDetails = userService.getClientDetails(u
                    .getLogin());

            receiverData.append("receiver:" + u.getLogin() + "Receiver host:"
                    + receiverDetails.getHost() + "Receiver port:"
                    + receiverDetails.getPort());
        }
        // sample message but could be done in different ways
        return "sender:" + currentUser.getLogin() + "Sender host:"
                + senderDetails.getHost() + receiverData +

                // ...other details such as userAgent so we can detect mobile
                "Message: " + message;

    }

    public List<Message> getMyMessagesWithinDateRange(User user, Date from,
            Date to, Pageable p) {

        return repository.findByUserAndDateRange(user, from, to, p);
    }
}
