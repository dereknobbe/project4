import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

final class ChatClient {
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private Socket socket;//TODO close

    private final String server;
    private final String username;
    private final int port;

    private boolean open = true;

    /* ChatClient constructor
     * @param server - the ip address of the server as a string
     * @param port - the port number the server is hosted on
     * @param username - the username of the user connecting
     */
    private ChatClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    /**
     * Attempts to establish a connection with the server
     * @return boolean - false if any errors occur in startup, true if successful
     */
    private boolean start() {
        while (true) {
            // Create a socket
            try {
                socket = new Socket(server, port);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            // Attempt to create output stream
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            // Attempt to create input stream
            try {
                sInput = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            // Create client thread to listen from the server for incoming messages
            Runnable r = new ListenFromServer();
            Thread t = new Thread(r);
            t.start();

            // After starting, send the clients username to the server.
            try {
                sOutput.writeObject(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println ( "Connection accepted " + server + "/" + InetAddress.getLocalHost () + ":" + port );
            } catch (UnknownHostException e) {}

            return true;
        }
    }

    public void stopListening() {
        open = false;
    }

    /*
     * Sends a string to the server
     * @param msg - the message to be sent
     */
    private void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            getsOutput ().close ();
            getsInput ().close ();
            getSocket ().close ();
        } catch (IOException e ) {}
    }

    private ObjectInputStream getsInput() {
        return sInput;
    }

    private ObjectOutputStream getsOutput() {
        return sOutput;
    }

    private Socket getSocket() {
        return socket;
    }


    /*
     * To start the Client use one of the following command
     * > java ChatClient
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     *
     * If the portNumber is not specified 1500 should be used
     * If the serverAddress is not specified "localHost" should be used
     * If the username is not specified "Anonymous" should be used
     */
    public static void main(String[] args) {
        // Get proper arguments and override defaults
        ChatClient client = null;
        if (args.length == 0) {
            client = new ChatClient("localhost", 1500, "180student");
        }
        if (args.length == 1) {
            client = new ChatClient("localhost", 1500, args[0]);
        }
        if (args.length == 2) {
            int port = Integer.parseInt ( args[1] );
            client = new ChatClient("localhost", port, args[0]);
        }
        if (args.length == 3) {
            int port = Integer.parseInt ( args[1] );
            client = new ChatClient(args[2], port, args[0]);
        }
        // Create your client and start it
        client.start();

        Scanner userinput = new Scanner(System.in);
        while (true) {
            String message = userinput.nextLine ();
            if (message.equals ( "/logout" )) {
                ChatMessage msg = new ChatMessage ( 1, "", "" );
                client.sendMessage ( msg );
                client.close();
                break;
            } else if (message.split ( " " )[0].equals ( "/msg" )) {
                ChatMessage msg = new ChatMessage ( 2, message.split (" " )[1], message );
                DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
                String time = sdf.format(new Date ());
                String[] totalmessage = message.split(" ");
                String messager = "";
                for (int i = 2; i < totalmessage.length; i++) {
                    if (i == totalmessage.length - 1) {
                        messager = messager + totalmessage[i];
                        continue;
                    }
                    messager = messager + totalmessage[i] + " ";
                }
                System.out.println(time + " " + client.username + " -> " + message.split (" " )[1] + ": " +  messager);
                client.sendMessage ( msg );
            } else if (message.equals ( "/list" )) {
                ChatMessage msg = new ChatMessage ( 3, "", "" );
                client.sendMessage ( msg );
            } else if (message.split ( " " )[0].equals ( "/ttt" )) {
                ChatMessage msg = new ChatMessage ( 4, client.username, message );
                client.sendMessage ( msg );
            } else {
                ChatMessage msg = new ChatMessage ( 0, "", message );
                client.sendMessage ( msg );
            }
        }

        // Send an empty message to the server
    }
    /*
     * This is a private class inside of the ChatClient
     * It will be responsible for listening for messages from the ChatServer.
     * ie: When other clients send messages, the server will relay it to the client.
     */
    private final class ListenFromServer implements Runnable {
        public void run() {
            while (open) {
                try {
                    String msg = (String) sInput.readObject ();
                    System.out.print ( msg );
                } catch (IOException | ClassNotFoundException e) {
                    System.exit(1);
                    e.printStackTrace ();
                }
            }
        }
    }
}