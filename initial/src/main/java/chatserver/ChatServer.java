package chatserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chatclient.ClientPushMessageHandler;

public class ChatServer {
    public static final int SERVER_PORT = 2002;

    // TODO should be WeakReferemce
    Map<Socket, DataOutputStream> outputStreams = new HashMap<Socket, DataOutputStream>();

    public ChatServer(int port) {

        listen(port);
    }

    private void listen(int port) {

        try {
            ServerSocket ssc = new ServerSocket();

            while (true) {

                Socket s = ssc.accept();
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                outputStreams.put(s, dos);

                // use Thread pool
                new ServerThread(this, s);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

    }

    public static void main(String[] args) {

        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = SERVER_PORT;
        }

        new ChatServer(port);

    }

    // real world send to dispatch system
    // use multiple threads
    // public void sendToAll(String message) {
    //
    // synchronized (outputStreams) {
    //
    // for (DataOutputStream dos : outputStreams.values()) {
    // try {
    // dos.writeUTF(message);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // }
    public void sendToAll(String message) {

        List<ClientDetails> list = parseMessageHeader(message);
        MessageDispatcher dispatcher = new MessageDispatcher();
        try {
            for (ClientDetails cd : list) {
                ClientPushMessageHandler cm;
                // TODO lookup socket for client and set it for every
                // conversation
                cm = new ClientPushMessageHandler(cd, dispatcher);
                cd.setMessageHandler(cm);

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<ClientDetails> parseMessageHeader(String message) {

        // Extract To clients
        ClientDetails cd = new ClientDetails();
        List<ClientDetails> l = new ArrayList<ClientDetails>();
        String[] receiver = message.split("><");
        for (int i = 0; i < receiver.length; i++) {
            if (receiver[i].startsWith("<Receiver host")) {
                String[] host = receiver[i].split(":");
                cd.setHost(host[1]);
                l.add(cd);
            }
        }
        return l;
    }

    public void removeConnection(Socket socket) {
        synchronized (outputStreams) {

            outputStreams.remove(socket);

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
