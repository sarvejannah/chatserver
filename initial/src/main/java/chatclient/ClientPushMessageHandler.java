package chatclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chatserver.ClientDetails;
import chatserver.MessageDispatcher;

/*
 * Should move these classes to use Executor Service Thread Pool
 */
public class ClientPushMessageHandler implements Runnable {
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

    private MessageDispatcher serverDispatcher;
    private ClientDetails clientDetails;
    private DataOutputStream dos;

    public ClientPushMessageHandler(ClientDetails client,
            MessageDispatcher aServerDispatcher) throws IOException {
        clientDetails = client;
        serverDispatcher = aServerDispatcher;
        Socket socket = clientDetails.getSocket();
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(String aMessage) {
        try {
            messageQueue.put(aMessage);
            notify();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     *         is empty, falls in sleep until notified for message arrival by
     *         sendMessage method.
     */
    private synchronized String getNextMessage() throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = (String) messageQueue.take();
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(String aMessage) {
        try {
            dos.writeUTF(aMessage);
        } catch (IOException e) {
            // TODO Create UndelivereMessage and store in DB
            // Next time the user logs in check for undelivered messags

            e.printStackTrace();
        }

    }

    /**
     * Until interrupted, reads messages from the message queue and sends them
     * to the client's socket.
     */
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = getNextMessage();
                sendMessageToClient(message);
            }
        } catch (Exception e) {
            // Communication problem
        }

        // Communication is broken. Interrupt sender threads
        serverDispatcher.deleteClient(clientDetails);
    }

}
