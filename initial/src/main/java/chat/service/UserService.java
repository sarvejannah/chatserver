package chat.service;

import chatserver.ClientDetails;

public interface UserService {

    public Long authenticate(String userLogin);

    // may need to find a better place for this method.
    public ClientDetails getClientDetails(String userLogin);

}
