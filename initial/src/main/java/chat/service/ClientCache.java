package chat.service;

import java.util.LinkedHashMap;
import java.util.Map;

import chatserver.ClientDetails;

public class ClientCache extends LinkedHashMap<String, ClientDetails> {

    private static final long serialVersionUID = 1L;
    private final int capacity;

    public ClientCache(int capacity) {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<String, ClientDetails> eldest) {
        return size() > capacity;
    }
}
