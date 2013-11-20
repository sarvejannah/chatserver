package chatserver;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/*
 * Should move to use ExecutorService Thread Pool
 */
public class MessageDispatcher implements Runnable {

    private BlockingQueue<String> messageQueue; // synchronized data structure
    private List<ClientDetails> clients; // not synchronized data structure
                                         // infrequent operation

    public synchronized void addClient(ClientDetails client) {
        clients.add(client);

    }

    public synchronized boolean deleteClient(ClientDetails client) {

        return clients.remove(client);

    }

    public synchronized void dispatchMessage(ClientDetails client,
            String message) throws InterruptedException {
        // Socket socket = client.socket;
        // String senderIP = socket.getInetAddress().getHostAddress();
        // String senderPort = "" + socket.getPort();
        String newMessage = client.getUser().getLogin() + " : " + message;
        messageQueue.put(newMessage);
        notify();

    }

    public synchronized String getNextMessage() throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = (String) messageQueue.take();
        return message;
    }

    private synchronized void sendMessageToAllClients(String aMessage) {
        for (int i = 0; i < clients.size(); i++) {
            ClientDetails clientDetails = (ClientDetails) clients.get(i);
            clientDetails.getMessageHandler().sendMessage(aMessage);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = getNextMessage();
                sendMessageToAllClients(message);
            }
        } catch (InterruptedException ie) {
            // Thread interrupted. Stop its execution
        }

    }

}
