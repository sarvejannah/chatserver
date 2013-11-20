package chat.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Group {
    @Id
    private String id;

    private String name;

    private List<User> userList;

    public Group() {
    }

    public Group(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return String.format("Group[id=%s, name='%s', userList='%s']", id,
                name, userList);
    }
}
