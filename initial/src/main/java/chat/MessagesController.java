package chat;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chat.model.Message;
import chat.model.User;
import chat.service.MessageService;

public class MessagesController {

    @Autowired
    private MessageService messageService;

    /* List of messages for the authenticated user */
    @RequestMapping(value = "/messages/user/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Message> getMessages(@PathVariable(value = "id") String id,
            Principal user, Pageable p) {
        User currentUser = (User) user;
        return messageService.getMyMessages(currentUser, p);
    }

    @RequestMapping(value = "/messages/user/{id}", method = RequestMethod.POST)
    public @ResponseBody
    void sendMessages(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "userLogin", required = false) String userLogin,
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(value = "message", required = false) String message,
            Principal user) {

        if (userLogin == null && groupName == null) {
            // return Http error status code and retrieve message from resource
            // bundle
        }
        User currentUser = (User) user;
        if (groupName != null) {
            messageService.sendMessages(message, currentUser, groupName, true);
        } else {
            messageService.sendMessages(message, currentUser, userLogin, false);

        }

    }

    /* retrieve a conversation instead */

    @RequestMapping(value = "/messages/{msgid}", method = RequestMethod.GET)
    public @ResponseBody
    Message getMessagesWithId(@PathVariable(value = "msgid") Long msgId,
            Principal user) {
        User currentUser = (User) user;
        return messageService.getMessage(currentUser, msgId);
    }

    @RequestMapping(value = "/messages/user/{id}, method = RequestMethod.GET")
    public @ResponseBody
    List<Message> getMessages(@PathVariable(value = "id") String id,
            @RequestParam(value = "from", required = true) Date from,
            @RequestParam(value = "to", required = false) Date to,
            Principal user, Pageable p) {
        User currentUser = (User) user;

        if (to == null) {
            to = new Date();
        } else if (from.compareTo(to) > 0) {
            // return httpError Response
        }
        return messageService.getMyMessagesWithinDateRange(currentUser, from,
                to, p);

    }

}
