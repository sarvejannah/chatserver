package chat.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String id;

    // ideally we may want to keep User attributes such as login, name
    // separate but for ease of query and since we use mongoDB it is
    // stored with the contacts
    private String login;
    private String name;

    private List<User> myContacts;
    private List<Group> myGroupContacts;

    public User() {
    }

    public User(String name, String userLogin) {
        this.name = name;
        this.login = userLogin;
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, name='%s', userLogin='%s']", id,
                name, login);
    }

    public List<User> getMyContacts() {
        return myContacts;
    }

    public void setMyContacts(List<User> myContacts) {
        this.myContacts = myContacts;
    }

    public List<Group> getMyGroupContacts() {
        return myGroupContacts;
    }

    public void setMyGroupContacts(List<Group> myGroupContacts) {
        this.myGroupContacts = myGroupContacts;
    }

    public void addGroupContacts(Group group) {
        myGroupContacts.add(group);

    }

    public void addContact(User user2) {
        myContacts.add(user2);

    }

    public boolean isContact(User u) {

        return myContacts.contains(u);
    }

    public void removeContact(User u) {
        myContacts.remove(u);

    }

    public String getLogin() {
        // TODO Auto-generated method stub
        return login;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

}
