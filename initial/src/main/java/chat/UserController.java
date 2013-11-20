package chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chat.model.User;

@Controller
public class UserController {

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public @ResponseBody
    long createUser(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "userLogin", required = false) String userLogin) {
        long id = UserManager.getInstance().createUser(userLogin, name);
        return id;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@RequestParam(value = "id", required = false) String id) {
        User u = UserManager.getInstance().getUser(id);
        return u;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    User removeUser(@RequestParam(value = "id", required = false) String id) {
        User u = UserManager.getInstance().removeUser(id);
        return u;
    }

}
