public class TicTacToeGame {

    private static final char PLAYERX = 'X';     // Helper constant for X player 1
    private static final char PLAYERO = 'O';     // Helper constant for O player 2
    private static final char SPACE = ' ';       // Helper constant for spaces

    private String player1;
    private String player2;
    private static String board[][];
    private boolean won;
    private String lastPlayed;

    /*
    Sample TicTacToe Board
      0 | 1 | 2
     -----------
      3 | 4 | 5
     -----------
      6 | 7 | 8
     */

    // TODO 4: Implement necessary methods to manage the games of Tic Tac Toe

    public TicTacToeGame(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new String[3][3];
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0; j < board.length ; j++) {
                board[i][j] = " ";
            }
        }
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public boolean gameOver() {
        if (board[0][0] == "X" && board[0][1] == "X" && board[0][2] == "X") {
            return true;
        } else if (board[1][0] == "X" && board[1][1] == "X" && board[1][2] == "X") {
            return true;
        } else if (board[2][0] == "X" && board[2][1] == "X" && board[2][2] == "X") {
            return true;
        } else if (board[0][0] == "X" && board[1][0] == "X" && board[2][0] == "X") {
            return true;
        } else if (board[0][1] == "X" && board[1][1] == "X" && board[2][1] == "X") {
            return true;
        } else if (board[0][2] == "X" && board[1][2] == "X" && board[2][2] == "X") {
            return true;
        } else if (board[0][0] == "X" && board[1][1] == "X" && board[2][2] == "X") {
            return true;
        } else if (board[2][0] == "X" && board[1][1] == "X" && board[0][2] == "X") {
            return true;
        }

        if (board[0][0] == "O" && board[0][1] == "O" && board[0][2] == "O") {
            return true;
        } else if (board[1][0] == "O" && board[1][1] == "O" && board[1][2] == "O") {
            return true;
        } else if (board[2][0] == "O" && board[2][1] == "O" && board[2][2] == "O") {
            return true;
        } else if (board[0][0] == "O" && board[1][0] == "O" && board[2][0] == "O") {
            return true;
        } else if (board[0][1] == "O" && board[1][1] == "O" && board[2][1] == "O") {
            return true;
        } else if (board[0][2] == "O" && board[1][2] == "O" && board[2][2] == "O") {
            return true;
        } else if (board[0][0] == "O" && board[1][1] == "O" && board[2][2] == "O") {
            return true;
        } else if (board[2][0] == "O" && board[1][1] == "O" && board[0][2] == "O") {
            return true;
        }

        for (int i = 0; i < board.length ; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void takeTurn(int index,String username) {
        if (username == player1) {
            getXMove ( index );
        }
        if (username == player2) {
            getOMove ( index );
        }
        lastPlayed = username;
    }
    public static String formattedBoard() {
        String b = "";
        for (int i = 0 ; i < 3 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                if ( j != 2) {
                    b = b + board[i][j] + "|";
                } else {
                    b = b + board[i][j];
                }
            }
            if (i != 2) {
                b = b + "\n";
                b = b + "------";
                b = b + "\n";
            } else if (i == 2) {}
        }
        return b;
    }



    public void getOMove(int move) {
        if (move == 0) {
            board[0][0] = 'O' + "";
        } else if (move == 1) {
            board[0][1] = 'O' + "";
        } else if (move == 2) {
            board[0][2] = 'O' + "";
        } else if (move == 3) {
            board[1][0] = 'O' + "";
        } else if (move == 4) {
            board[1][1] = 'O' + "";
        } else if (move == 5) {
            board[1][2] = 'O' + "";
        } else if (move == 6) {
            board[2][0] = 'O' + "";
        } else if (move == 7) {
            board[2][1] = 'O' + "";
        } else if (move == 8) {
            board[2][2] = 'O' + "";
        }
    }

    public void getXMove(int move) {
        if (move == 0) {
            board[0][0] = 'X' + "";
        } else if (move == 1) {
            board[0][1] = 'X' + "";
        } else if (move == 2) {
            board[0][2] = 'X' + "";
        } else if (move == 3) {
            board[1][0] = 'X' + "";
        } else if (move == 4) {
            board[1][1] = 'X' + "";
        } else if (move == 5) {
            board[1][2] = 'X' + "";
        } else if (move == 6) {
            board[2][0] = 'X' + "";
        } else if (move == 7) {
            board[2][1] = 'X' + "";
        } else if (move == 8) {
            board[2][2] = 'X' + "";
        }
    }

}