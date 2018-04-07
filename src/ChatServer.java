import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.util.Date;


final class ChatServer {
    private static int uniqueId = 0;
    // Data structure to hold all of the connected clients
    private final List<ClientThread> clients = new ArrayList<>();
    private final int port;          // port the server is hosted on
    private final List<TicTacToeGame> games = new ArrayList<>();


    /**
     * ChatServer constructor
     * @param port - the port the server is being hosted on
     */
    private ChatServer(int port) {
        this.port = port;
    }

    public void serverPrint(String message) {
        Date date = new Date();
        DateFormat dateformat = new SimpleDateFormat ("HH:mm:ss" );
        System.out.println(date + " " + message);
    }

    private boolean clientExists(int id) {
        String user = "";
        int tempid = -1;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get ( i ).id == id ) {
                user = clients.get(i).username;
                tempid = clients.get(i).id;
            }
        }
        for (int i = 0; i < clients.size(); i++) {
            if (user.equals(clients.get(i).username) && tempid != clients.get(i).id) {
                return true;
            }
        }
        return false;
    }

    private String clientUser(int id) {
        String client = "";
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get ( i ).id == id ) {
                client = clients.get(i).username;
            }
        }
        return client;
    }

    /*
     * This is what starts the ChatServer.
     * Right now it just creates the socketServer and adds a new ClientThread to a list to be handled
     */
    private void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        DateFormat dateformat = new SimpleDateFormat ("HH:mm:ss" );

        while (true) {
            System.out.println(dateformat.format(date) + " Server waiting for Clients on port " + port);
            try {
                Socket socket = serverSocket.accept();
                Runnable r = new ClientThread(socket, uniqueId++);
                Thread t = new Thread(r);
                //TODO
                // Check if they are there before adding. ClientThread.writemsg USER already has this. Client thread close
                boolean exists = false;
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).username.equals(clientUser(uniqueId - 1))) {
                        System.out.println("> Sorry, a user with username : " + clientUser(uniqueId - 1) + " already exists.");
                        exists = true;
                    }
                }
                if (exists) {
                    return;
                }
                else {
                    t.start();
                    clients.add((ClientThread) r);
                }
                //END TODO
               /* if (clientExists ( uniqueId -1 )) {
                    idDirectMessage (" > Sorry, a user with username: " + clientUser (uniqueId -1  ) + " already exists",uniqueId - 1);
                    clients.remove(clients.size() -1 );
                    uniqueId--;
                    continue;
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < clients.size() ; i ++) {
                if (clients.get(i).id == uniqueId - 1) {
                    System.out.println(dateformat.format(date) + " " + clients.get(i).username + " just connected");

                }
            }
        }
    }



    /*
     *  > java ChatServer
     *  > java ChatServer portNumber
     *  If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        ChatServer server = null;
        if (args.length == 0) {
            server = new ChatServer(1500);
        }
        if (args.length == 1) {
            int port = Integer.parseInt ( args[0] );
            server = new ChatServer(port);
        }
        server.start();

    }
    private synchronized void broadcast(String message,String sender) {
        Date date = new Date();
        DateFormat dateformat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss" );
        for (int i = 0; i < clients.size() ; i++) {
            clients.get(i).writeMsg( dateformat.format(date) + " " + sender + ": " + message + "\n");
        }
        System.out.println (dateformat.format(date) + " " + sender + ": " + message + "\n");
    }
    /**
     * Sample code to use as a reference for Tic Tac Toe
     *
     * directMessage - sends a message to a specific username, if connected
     * @param  message - the string to be sent
     * @param  username - the user the message will be sent to
     */
    private synchronized void directMessage(String message, String username) {
        DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        String time = sdf.format(new Date());
        String formattedMessage = time + " " + message + "\n";

        for (ClientThread clientThread : clients) {
            if (clientThread.username.equalsIgnoreCase(username)) {
                clientThread.writeMsg(formattedMessage);
            }
        }
        System.out.print (formattedMessage);
    }

    private synchronized void idDirectMessage(String message, int id) {
        DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        String time = sdf.format(new Date());
        String formattedMessage = time + " " + message + "\n";

        for (ClientThread clientThread : clients) {
            if (clientThread.id == id) {
                clientThread.writeMsg(formattedMessage);
            }
        }
        System.out.print(formattedMessage);
    }

    private synchronized void tttStartMessage(String message, String username) {
        String formattedMessage = message + "\n";

        for (ClientThread clientThread : clients) {
            if (clientThread.username.equalsIgnoreCase(username)) {
                clientThread.writeMsg(formattedMessage);
            }
        }
    }

    private synchronized void privateMessage(String message, String username, String recipient) {
        DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        String time = sdf.format(new Date());
        String[] totalmessage = message.split(" ");
        String messager = "";
        for (int i = 2; i < totalmessage.length; i++) {
            if (i == totalmessage.length - 1) {
                messager = messager + totalmessage[i];
                continue;
            }
            messager = messager + totalmessage[i] + " ";
        }
        String formattedMessage = time + " " + recipient + " -> " + username + ": " + messager + "\n";
        for (ClientThread clientThread : clients) {
            if (clientThread.username.equalsIgnoreCase(username)) {
                clientThread.writeMsg(formattedMessage);
            }
        }
        System.out.print (formattedMessage);
    }

    public synchronized void createGame(String player1, String player2) {
        if (player1.equals(player2)) {
            return;
        }
        games.add ( new TicTacToeGame ( player1,player2 ) );
        TicTacToeGame tempGame = new TicTacToeGame ( "temp1","temp2" );
        //directMessage (tempGame.formattedBoard (),player1);
        //directMessage (tempGame.formattedBoard (),player2);
    }

    public synchronized void makeMove(int move, TicTacToeGame game, String username) {
       // if (game.getLastPlayed ().equals( username )) {
        //    return;
        //}
        game.takeTurn ( move,username);
    }

    public synchronized boolean gameExists(String player1,String player2) {
        for (int i = 0; i < games.size (); i++) {
            if ((player1.equals(games.get(i).getPlayer1 ()) && player2.equals(games.get(i).getPlayer2 ())) || (player2.equals(games.get(i).getPlayer1()) && player1.equals(games.get(i).getPlayer2 ()))) {
                return true;
            }
        }
        return false;
    }

    public String list(String username) {
        String list = "";
        for (int i = 0; i < clients.size() ; i++) {
            if (clients.get(i).username.equals(username)) {
                continue;
            } else {
                if (i != clients.size() -1) {
                    list = list + clients.get ( i ).username + ", ";
                } else {
                    list = list + clients.get ( i ).username;
                }
            }
        }
        return list;
    }

    /*
     * This is a private class inside of the ChatServer
     * A new thread will be created to run this every time a new client connects.
     */
    private final class ClientThread implements Runnable {
        Socket socket;                  // The socket the client is connected to
        ObjectInputStream sInput;       // Input stream to the server from the client
        ObjectOutputStream sOutput;     // Output stream to the client from the server
        String username;                // Username of the connected client
        ChatMessage cm;                 // Helper variable to manage messages
        int id;


        //TODO close sinput and soutput inside threada
        //move close inside tread
        /*
         * socket - the socket the client is connected to
         * id - id of the connection
         */
        private ClientThread(Socket socket, int id) {
            this.id = id;
            this.socket = socket;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*
         * This is what the client thread actually runs.
         */
        @Override
        public void run() {
            // Read the username sent to you by client
            while (true) {
                try {
                    cm = (ChatMessage) sInput.readObject ();
                } catch (IOException | ClassNotFoundException e) {
                    //I had to erase the printed stack trace lol oops
                }
                if (cm.getType () == 0) {
                    broadcast ( cm.getMessage (), username );
                } else if (cm.getType () == 1) {
                    try {
                        DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
                        String time = sdf.format(new Date());
                        System.out.println(time + " " + username + " disconnected with a LOGOUT message.\n");
                        sInput.close ();
                        sOutput.close ();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                    remove(id);
                    return;
                } else if (cm.getType () == 2) {
                    privateMessage ( cm.getMessage (), cm.getRecipient (), username);
                } else if (cm.getType () == 3) {
                    DateFormat sdf = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
                    String time = sdf.format(new Date());
                    writeMsg ( time + " List of users connected right now: " + list(username) + "\n");
                } else if (cm.getType () == 4) {
                    int counter = 0;
                    for (int i = 0; i < clients.size (); i++) {
                        //checking for disconnected players
                        if (clients.get(i).username.equals(cm.getRecipient () )) {
                            if (!clients.get ( i ).socket.isConnected ()) {
                                directMessage ( "This user is not currently connected.",username);
                            }
                        }
                    }
                    //checking if game exists
                    if (!gameExists ( username,cm.getRecipient ()) && counter == 0) {
                        createGame ( username,cm.getRecipient () );
                        serverPrint("TicTacToe started between " + username + " and " + cm.getRecipient());
                        tttStartMessage("Started TicTacToe with " + cm.getRecipient(),username);
                        tttStartMessage("Started TicTacToe with " + username,cm.getRecipient());
                    } else if (gameExists ( username,cm.getRecipient ())) {
                        //game exists, checking if move or board request
                        if (cm.getMessage ().matches ( "-?\\d+" )) {
                            for (int j = 0; j < games.size() ; j++) {
                                //makemove
                                if ((username.equals(games.get(j).getPlayer1 ()) && cm.getRecipient ().equals(games.get(j).getPlayer2 ())) || (username.equals(games.get(j).getPlayer2()) && cm.getRecipient ().equals(games.get(j).getPlayer1 ()))) {
                                    int move = Integer.parseInt ( cm.getMessage () );
                                    makeMove ( move,games.get ( j ),username );
                                    directMessage (games.get ( j ).formattedBoard (),games.get ( j ).getPlayer1 ());
                                    directMessage (games.get ( j ).formattedBoard (),games.get ( j ).getPlayer2 ());
                                }
                            }
                            // view board
                        } else {
                            for (int j = 0; j < games.size (); j++) {
                                if (username.equals(games.get(j).getPlayer1 ()) && cm.getRecipient ().equals(games.get(j).getPlayer2 ())) {
                                    directMessage (games.get ( j ).formattedBoard (),username);
                                }
                            }
                        }
                    }
                    for (int i = 0; i < games.size() ; i++) {
                        if (games.get(i).gameOverX ()) {
                            directMessage ( "Game over", games.get ( i ).getPlayer1 ( ));
                            directMessage ( "Game over", games.get ( i ).getPlayer2 ( ));
                            games.remove ( i );
                        }
                    }
                }
            }
        }

        private synchronized boolean writeMsg(String msg) {
            //ClientThread client = new ClientThread ( socket,id );
            if (!socket.isConnected ()) {
                return false;
            }
            if (socket.isConnected ()) {
                try {
                    sOutput.writeObject ( msg );
                } catch (IOException e) {}
                return true;
            }
            return false;
        }

        private synchronized void remove(int id) {
            for (int i  = 0 ; i < clients.size() ; i++ ) {
                if (clients.get(i).id == id) {
                    clients.remove(i);
                }
            }
        }
    }
}