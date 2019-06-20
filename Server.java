
// A Java program for a Server 
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Server {

    // initialize socket and input stream
    private static int counter = 1;
    private Socket socket;
    private ServerSocket server;
    private ExecutorService ex;

    // constructor with port
    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        System.out.println("LOG : Server Created on Port " + port);

        ex = Executors.newCachedThreadPool();
    }

    public void starter() throws IOException {
        System.out.println("Waiting for first connection.\n");
        while (true) {

            socket = server.accept();
            ex.submit(new Handeler(socket));
            System.out.println("Waiting for a client " + counter + " ...\n");
        }
    }

    //this inner class prepare response for each client in seperate thread - (uppercase and return)
    private class Handeler implements Runnable {
        private Socket client;
        private int clientID;
        private DataInputStream in;
        private DataOutputStream out;

        public Handeler(Socket client) {
            this.client = client;
            clientID = counter++;
            ;
        }

        public void run() {
            try {
                System.out.println(" >> Client " + clientID + " accepted.");
                out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                out.writeUTF("Hi, Your Client ID is:" + clientID + "\n");
                out.flush();

                while (true) {
                    String msg = in.readUTF();
                    if (msg.equals("close"))
                        break;
                    msg = msg.toUpperCase();
                    out.writeUTF("Client " + clientID + "'s Answer: " + msg);
                    out.flush();
                }

                in.close();
                out.close();
                System.out.println("I/O Stremas Closed For Client " + clientID + ".");
            } catch (Exception e) {
            }
        }
    }

    public static void main(String args[]) {
        try {
            Server s = new Server(1377);
            s.starter();
        } catch (Exception e) {
            System.exit(-1);
        }
    }
}