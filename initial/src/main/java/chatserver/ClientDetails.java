package chatserver;

import java.net.Socket;

import chat.model.User;
import chatclient.ClientPushMessageHandler;

public class ClientDetails {

    private Socket socket;

    private String host;
    private int port;
    private User user;

    private Long sessionId;

    private ClientPushMessageHandler messageHandler;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        // TODO Auto-generated method stub
        return user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSessionId(Long generatedId) {
        this.sessionId = generatedId;

    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setMessageHandler(Object messageHandler) {
        // TODO Auto-generated method stub
        this.messageHandler = (ClientPushMessageHandler) messageHandler;
    }

    public ClientPushMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;

    }

}
