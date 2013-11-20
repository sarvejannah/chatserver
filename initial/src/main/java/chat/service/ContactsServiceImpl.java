package chat.service;

import chat.model.User;

public class ContactsServiceImpl implements ContactsService {

    @Override
    public void requestAddContacts(User recipient, User requester) {
        // system message from requester to recipient to add as contact

    }

    @Override
    public void rejectAddContacts(User recipient, User requester) {
        // system message from recipient to requester to reject
        // add as contact

    }

}
