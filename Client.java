import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket;
    private Scanner input;
    private DataOutputStream out;
    private DataInputStream in;

    // constructor to put ip address and port
    public Client(String address, int port) throws Exception {
        socket = new Socket(address, port);
        System.out.println("Connected");
        input = new Scanner(System.in);
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        while (true) {
            System.out.println(">> RESPONSE: " + in.readUTF() + "\n");
            String msg = input.nextLine();
            if (msg.equals("close")) {

                out.writeUTF(msg);
                out.flush();
                break;
            }

            out.writeUTF(msg);
            out.flush();
        }

        socket.close();
        out.close();
        input.close();
    }

    
}
