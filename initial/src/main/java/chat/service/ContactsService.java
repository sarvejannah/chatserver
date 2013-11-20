package chat.service;

import chat.model.User;

public interface ContactsService {

    public void requestAddContacts(User recepient, User requester);

    public void rejectAddContacts(User recepient, User requester);
}
