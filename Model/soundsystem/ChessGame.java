package Model.soundsystem;

    import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessGame extends JFrame {
    private JButton[][] squares = new JButton[8][8];
    private String[][] board = new String[8][8];
    private boolean pieceSelected = false;
    private int selectedRow, selectedCol;
    private char currentPlayer = 'w'; // 'w' for white, 'b' for black

    public ChessGame() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        initializeGUI();
        setSize(600, 600);
        setVisible(true);
    }

    // Initializes the board with standard chess starting positions using Unicode characters.
    private void initializeBoard() {
        // Black pieces
        board[0][0] = "♜"; board[0][1] = "♞"; board[0][2] = "♝"; board[0][3] = "♛";
        board[0][4] = "♚"; board[0][5] = "♝"; board[0][6] = "♞"; board[0][7] = "♜";
        for (int j = 0; j < 8; j++) {
            board[1][j] = "♟";
        }
        // Empty squares
        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = "";
            }
        }
        // White pieces
        for (int j = 0; j < 8; j++) {
            board[6][j] = "♙";
        }
        board[7][0] = "♖"; board[7][1] = "♘"; board[7][2] = "♗"; board[7][3] = "♕";
        board[7][4] = "♔"; board[7][5] = "♗"; board[7][6] = "♘"; board[7][7] = "♖";
    }

    // Builds the GUI: creates an 8x8 grid of buttons representing chess squares.
    private void initializeGUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton(board[row][col]);
                squares[row][col].setFont(new Font("SansSerif", Font.PLAIN, 32));
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(Color.WHITE);
                } else {
                    squares[row][col].setBackground(Color.GRAY);
                }
                final int r = row, c = col;
                squares[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(r, c);
                    }
                });
                add(squares[row][col]);
            }
        }
    }

    // Handles clicks on squares. First click selects a piece; second click moves it.
    private void handleSquareClick(int row, int col) {
        if (!pieceSelected) {
            // Select a piece only if one exists on the clicked square
            if (!board[row][col].equals("")) {
                // Check if the piece belongs to the current player
                if ((currentPlayer == 'w' && "♙♖♘♗♕♔".contains(board[row][col])) ||
                    (currentPlayer == 'b' && "♟♜♞♝♛♚".contains(board[row][col]))) {
                    pieceSelected = true;
                    selectedRow = row;
                    selectedCol = col;
                    squares[row][col].setBackground(Color.YELLOW);
                }
            }
        } else {
            // Move the selected piece to the clicked square (no move validation here)
            board[row][col] = board[selectedRow][selectedCol];
            board[selectedRow][selectedCol] = "";
            updateButton(selectedRow, selectedCol);
            updateButton(row, col);
            // Reset the background of the originally selected square
            resetSquareColor(selectedRow, selectedCol);
            pieceSelected = false;
            // Switch player turns
            currentPlayer = (currentPlayer == 'w') ? 'b' : 'w';
        }
    }

    // Updates the text on the button to reflect the board state.
    private void updateButton(int row, int col) {
        squares[row][col].setText(board[row][col]);
    }

    // Resets the square's color based on its board position.
    private void resetSquareColor(int row, int col) {
        if ((row + col) % 2 == 0) {
            squares[row][col].setBackground(Color.WHITE);
        } else {
            squares[row][col].setBackground(Color.GRAY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChessGame();
            }
        });
    }
}


