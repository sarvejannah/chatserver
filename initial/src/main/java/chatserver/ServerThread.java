package chatserver;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Should move to use ExecutorService Threadpool
 */
public class ServerThread implements Runnable {

    private static Logger logger = Logger.getLogger(ServerThread.class
            .getName());

    private ChatServer chatServer;
    private Socket socket;

    public ServerThread(ChatServer cs, Socket s) {

        this.chatServer = cs;
        this.socket = s;
    }

    @Override
    public void run() {

        DataInputStream dis;
        try {
            dis = new DataInputStream(socket.getInputStream());

            while (true) {
                String message = dis.readUTF();
                logger.log(Level.FINER, "sending message " + message);

                chatServer.sendToAll(message);
            }
        } catch (EOFException e) {

            // ignore
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            chatServer.removeConnection(socket);
        }

    }

}
