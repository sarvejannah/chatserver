package chat;

import java.security.Principal;
import java.util.List;

import old.GroupManager;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import chat.exception.UnknownContactException;
import chat.model.Group;
import chat.model.User;
import chat.service.ContactsService;

@Controller
public class ContactsController {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private ContactsService contactsService;

    /* List of contacts for the authenticated user */
    @RequestMapping(value = "/users/{id}/contacts", method = RequestMethod.GET)
    public @ResponseBody
    List<User> getContacts(Principal user) {
        User currentUser = (User) user;
        return currentUser.getMyContacts();
    }

    @RequestMapping(value = "/users/{id}/contacts", method = RequestMethod.POST)
    public void createContact(
            @PathVariable(value = "id") long id,
            @RequestParam(value = "userLogin", required = false) String userLogin) {
        User u = UserManager.getInstance().getUser(userLogin);
        User owner = getAuthenticatedUser();
        // create message to add contact
        contactsService.requestAddContacts(u, owner);

    }

    @RequestMapping(value = "/users/{id}/contacts/group", method = RequestMethod.POST)
    public void createGroupContact(@PathVariable(value = "id") long id,
            @RequestParam(value = "groupId") long groupId) {
        Group group = GroupManager.getInstance().getGroup(groupId);
        User owner = getAuthenticatedUser();
        // Do we need approval for this
        owner.addGroupContacts(group);

    }

    @RequestMapping(value = "/users/{id}/contacts/accept", method = RequestMethod.GET)
    public void acceptContactRequest(
            @PathVariable(value = "id") String userLogin) {
        User u = UserManager.getInstance().getUser(userLogin);
        User owner = getAuthenticatedUser();
        u.addContact(owner);
        owner.addContact(u);

    }

    @RequestMapping(value = "/users/{id}/contacts/reject", method = RequestMethod.GET)
    public void rejectContactRequest(
            @PathVariable(value = "id") String userLogin) {
        User u = UserManager.getInstance().getUser(userLogin);
        User owner = getAuthenticatedUser();
        // send message to requester that the request to add contact was refused
        contactsService.rejectAddContacts(owner, u);

    }

    @RequestMapping(value = "/users/{id}/contacts/{cid}", method = RequestMethod.GET)
    public @ResponseBody
    User getContact(@PathVariable(value = "id") long id,
            @PathVariable(value = "cid") long cid)
            throws UnknownContactException {
        User u = UserManager.getInstance().getUser(cid);
        User owner = getAuthenticatedUser();
        if (owner.isContact(u)) {
            return u;
        }
        throw new UnknownContactException(u.getName());

    }

    @RequestMapping(value = "/users/{id}/contacts/{cid}", method = RequestMethod.DELETE)
    public @ResponseBody
    boolean removeContact(@PathVariable(value = "id") long id,
            @PathVariable(value = "cid") long cid)
            throws UnknownContactException {
        User u = UserManager.getInstance().getUser(cid);
        User owner = getAuthenticatedUser();
        if (owner.isContact(u)) {
            owner.removeContact(u);
            u.removeContact(owner);
            return true;
        }

        throw new UnknownContactException(u.getName());

    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        String name = auth.getName();
        User owner = UserManager.getInstance().getUser(name);
        return owner;
    }

    @ExceptionHandler(UnknownContactException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(UnknownContactException e) {

        // return getMessage("key", null, Locale.getDefault());

        // contact.unkown loaded from resource bundle
        return e.getMessage() + "contact.unknown";

    }
}
