import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;
    private int type;
    private String username;
    private String message;

    // Types of messages
    static final int MESSAGE = 0, LOGOUT = 1, DM = 2, LIST = 3, TICTACTOE = 4;

    // Here is where you should implement the chat message object.
    // Variables, Constructors, Methods, etc.
    public ChatMessage(int type, String username, String message) {
        this.type = type;
        this.username = username;
        this.message = message;
    }
    public int getType() {
        return type;
    }

    public String getMessage() {
        if (getType (  ) == 0 ) {
            return message;
        } else if (getType (  ) == 1 ) {
            return "";
        } else if (getType (  ) == 2 ) {
            return message;
        } else if (getType (  ) == 3) {
            return "";
        } else if (getType (  ) == 4) {
            String[] m = message.split ( " " );
            String last = m[m.length - 1];
            return last;
        }
        return message;
    }
    //Need to change this
    public String getRecipient() {
        String recipient = "";
        if (getType (  ) == 0 ) {
            recipient = username;
            return recipient;
        } else if (getType (  ) == 1 ) {
            return username;
        } else if (getType (  ) == 2 ) {
            recipient = message.split(" ")[1];
            return recipient;
        } else if (getType (  ) == 3) {
            recipient = message.split(" ")[1];
            return recipient;
        } else if (getType (  ) == 4) {
            recipient = message.split(" ")[1];
            return recipient;
        }
        return recipient;
    }
}
