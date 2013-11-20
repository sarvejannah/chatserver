package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread implements Runnable {

    private String host;
    private int port;
    private DataInputStream din;
    private DataOutputStream dos;

    public ClientThread(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    public void init() {

        Socket s;
        try {
            s = new Socket(host, port);
            din = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            new Thread(this).start();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // pull message how does it work esp with multiple threads??
    private void processMessage(String message) {

        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                String message = din.readUTF();

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
