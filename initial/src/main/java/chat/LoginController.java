package chat;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import chat.exception.UnknownContactException;

/*
 * Can be used to authenticate users using an IDP provider such as LDAP Provider
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public @ResponseBody
    boolean login(
            @RequestParam(value = "userLogin", required = false) String userLogin) {
        // TODO need to use spring authentication provider
        long id = UserManager.getInstance().loginUser(userLogin);
        return true;
    }

    @RequestMapping(value = "/users/disconnect", method = RequestMethod.GET)
    public @ResponseBody
    boolean disconnect(
            @RequestParam(value = "userLogin", required = false) String userLogin) {

        // TODO need to use spring authentication provider
        long id = UserManager.getInstance().disconnect(userLogin);
        return true;
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
