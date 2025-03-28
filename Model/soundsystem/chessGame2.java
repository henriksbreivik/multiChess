package Model.soundsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

enum PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, NONE }
enum PieceColor { WHITE, BLACK, NONE }

// Represents a chess piece.
class Piece {
    PieceType type;
    PieceColor color;
    
    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }
    
    public Piece() {
        this.type = PieceType.NONE;
        this.color = PieceColor.NONE;
    }
    
    // Returns a Unicode symbol for the piece.
    public String getSymbol() {
        if (color == PieceColor.WHITE) {
            switch(type) {
                case KING:   return "\u2654";
                case QUEEN:  return "\u2655";
                case ROOK:   return "\u2656";
                case BISHOP: return "\u2657";
                case KNIGHT: return "\u2658";
                case PAWN:   return "\u2659";
                default:     return "";
            }
        } else if (color == PieceColor.BLACK) {
            switch(type) {
                case KING:   return "\u265A";
                case QUEEN:  return "\u265B";
                case ROOK:   return "\u265C";
                case BISHOP: return "\u265D";
                case KNIGHT: return "\u265E";
                case PAWN:   return "\u265F";
                default:     return "";
            }
        }
        return "";
    }
}

@SuppressWarnings("serial")
class ChessBoardPanel extends JPanel implements ActionListener {
    private JButton[][] squares = new JButton[8][8];
    private Piece[][] board = new Piece[8][8];
    private boolean pieceSelected = false;
    private int selectedRow, selectedCol;
    private PieceColor currentPlayer = PieceColor.WHITE;  // White always starts

    public ChessBoardPanel() {
        setLayout(new GridLayout(8, 8));
        initializeBoardState();
        initializeGUI();
    }
    
    // Set up the starting positions for all pieces.
    private void initializeBoardState() {
        // Fill all squares with an empty piece.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece();
            }
        }
        // Black pieces (top of board)
        board[0][0] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        board[0][1] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        board[0][2] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        board[0][3] = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        board[0][4] = new Piece(PieceType.KING, PieceColor.BLACK);
        board[0][5] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        board[0][6] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        board[0][7] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        for (int j = 0; j < 8; j++) {
            board[1][j] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        }
        // White pieces (bottom of board)
        board[7][0] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        board[7][1] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        board[7][2] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        board[7][3] = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        board[7][4] = new Piece(PieceType.KING, PieceColor.WHITE);
        board[7][5] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        board[7][6] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        board[7][7] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        for (int j = 0; j < 8; j++) {
            board[6][j] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        }
    }
    
    // Set up the board GUI.
    private void initializeGUI() {
        Font font = new Font("SansSerif", Font.PLAIN, 36);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton(board[row][col].getSymbol());
                squares[row][col].setFont(font);
                squares[row][col].setFocusPainted(false);
                squares[row][col].setOpaque(true);
                squares[row][col].setBorderPainted(false);
                // Alternate square colors
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(Color.WHITE);
                } else {
                    squares[row][col].setBackground(Color.GRAY);
                }
                squares[row][col].addActionListener(this);
                add(squares[row][col]);
            }
        }
    }
    
    // Handle a square click.
    public void actionPerformed(ActionEvent e) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (e.getSource() == squares[row][col]) {
                    handleSquareClick(row, col);
                    return;
                }
            }
        }
    }
    
    // Process a click on square (row, col)
    private void handleSquareClick(int row, int col) {
        if (!pieceSelected) {
            // Select a piece if it belongs to the current player.
            if (!board[row][col].getSymbol().equals("") && board[row][col].color == currentPlayer) {
                pieceSelected = true;
                selectedRow = row;
                selectedCol = col;
                squares[row][col].setBackground(Color.YELLOW);
            }
        } else {
            // Attempt to move the selected piece.
            if (isValidMove(board[selectedRow][selectedCol], selectedRow, selectedCol, row, col)) {
                // Move piece and clear the original square.
                board[row][col] = board[selectedRow][selectedCol];
                board[selectedRow][selectedCol] = new Piece();
                updateSquare(selectedRow, selectedCol);
                updateSquare(row, col);
                // Switch turns.
                currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            }
            // Reset colors.
            resetSquareColor(selectedRow, selectedCol);
            pieceSelected = false;
        }
    }
    
    // Update a single square’s display.
    private void updateSquare(int row, int col) {
        squares[row][col].setText(board[row][col].getSymbol());
    }
    
    // Reset a square's background to its default.
    private void resetSquareColor(int row, int col) {
        if ((row + col) % 2 == 0)
            squares[row][col].setBackground(Color.WHITE);
        else
            squares[row][col].setBackground(Color.GRAY);
    }
    
    // Checks if moving the piece from (fromRow, fromCol) to (toRow, toCol) is legal.
    private boolean isValidMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        // Cannot capture own piece.
        if (board[toRow][toCol].color == piece.color) {
            return false;
        }
        int dRow = toRow - fromRow;
        int dCol = toCol - fromCol;
        
        switch(piece.type) {
            case PAWN:
                return isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, dRow, dCol);
            case KNIGHT:
                // L-shaped move: two in one direction and one in perpendicular.
                if ((Math.abs(dRow) == 2 && Math.abs(dCol) == 1) ||
                    (Math.abs(dRow) == 1 && Math.abs(dCol) == 2)) {
                    return true;
                }
                break;
            case BISHOP:
                if (Math.abs(dRow) == Math.abs(dCol))
                    return clearDiagonal(fromRow, fromCol, toRow, toCol);
                break;
            case ROOK:
                if (dRow == 0 || dCol == 0)
                    return clearStraight(fromRow, fromCol, toRow, toCol);
                break;
            case QUEEN:
                if (dRow == 0 || dCol == 0)
                    return clearStraight(fromRow, fromCol, toRow, toCol);
                if (Math.abs(dRow) == Math.abs(dCol))
                    return clearDiagonal(fromRow, fromCol, toRow, toCol);
                break;
            case KING:
                // King moves one square in any direction.
                if (Math.abs(dRow) <= 1 && Math.abs(dCol) <= 1)
                    return true;
                break;
            default:
                break;
        }
        return false;
    }
    
    // Pawn movement validation.
    private boolean isValidPawnMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol, int dRow, int dCol) {
        int direction = (piece.color == PieceColor.WHITE) ? -1 : 1;
        // Standard one-square move forward.
        if (dCol == 0 && dRow == direction && board[toRow][toCol].type == PieceType.NONE) {
            return true;
        }
        // Double move from starting row.
        if (dCol == 0 && dRow == 2 * direction && board[toRow][toCol].type == PieceType.NONE) {
            if (piece.color == PieceColor.WHITE && fromRow == 6 || piece.color == PieceColor.BLACK && fromRow == 1) {
                // Check intermediate square is clear.
                if (board[fromRow + direction][fromCol].type == PieceType.NONE) {
                    return true;
                }
            }
        }
        // Diagonal capture.
        if (Math.abs(dCol) == 1 && dRow == direction && board[toRow][toCol].type != PieceType.NONE) {
            return true;
        }
        return false;
    }
    
    // Check if all squares between (fromRow,fromCol) and (toRow,toCol) are empty for straight moves.
    private boolean clearStraight(int fromRow, int fromCol, int toRow, int toCol) {
        int stepRow = Integer.compare(toRow, fromRow);
        int stepCol = Integer.compare(toCol, fromCol);
        int currentRow = fromRow + stepRow, currentCol = fromCol + stepCol;
        while (currentRow != toRow || currentCol != toCol) {
            if (board[currentRow][currentCol].type != PieceType.NONE) {
                return false;
            }
            currentRow += stepRow;
            currentCol += stepCol;
        }
        return true;
    }
    
    // Check if all squares between (fromRow,fromCol) and (toRow,toCol) are empty for diagonal moves.
    private boolean clearDiagonal(int fromRow, int fromCol, int toRow, int toCol) {
        int stepRow = (toRow - fromRow) > 0 ? 1 : -1;
        int stepCol = (toCol - fromCol) > 0 ? 1 : -1;
        int currentRow = fromRow + stepRow, currentCol = fromCol + stepCol;
        while (currentRow != toRow && currentCol != toCol) {
            if (board[currentRow][currentCol].type != PieceType.NONE) {
                return false;
            }
            currentRow += stepRow;
            currentCol += stepCol;
        }
        return true;
    }
}

// Main class that provides a window to spawn multiple chess board games.
public class chessGame2 {
    public static void main(String[] args) {
        // Main menu with a "New Board" button.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame menuFrame = new JFrame("Chess Game - Main Menu");
                menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.setLayout(new FlowLayout());
                JButton newBoardButton = new JButton("New Board");
                newBoardButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        createNewBoard();
                    }
                });
                menuFrame.add(newBoardButton);
                menuFrame.setSize(300, 100);
                menuFrame.setLocationRelativeTo(null);
                menuFrame.setVisible(true);
            }
        });
    }
    
    // Creates a new window with an independent chess board.
    public static void createNewBoard() {
        JFrame boardFrame = new JFrame("Chess Board");
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ChessBoardPanel boardPanel = new ChessBoardPanel();
        boardFrame.add(boardPanel);
        boardFrame.setSize(600, 600);
        boardFrame.setLocationByPlatform(true);
        boardFrame.setVisible(true);
    }
}


