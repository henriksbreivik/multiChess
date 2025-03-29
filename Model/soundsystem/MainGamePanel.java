package Model.soundsystem;

import javax.swing.*;
import java.awt.*;

public class MainGamePanel extends JPanel implements ChessBoardListener {
    private ChessBoardPanel[] boards = new ChessBoardPanel[9];
    private PieceColor[][] boardWinners = new PieceColor[3][3]; // Hvem som vant hvert brett
    private PieceColor currentPlayer = PieceColor.WHITE;
    private JLabel statusLabel = new JLabel("White's turn");

    public MainGamePanel() {
        setLayout(new BorderLayout());

        JPanel boardGrid = new JPanel(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            boards[i] = new ChessBoardPanel();
            boards[i].setListener(this, i); // viktig!
            boardGrid.add(boards[i]);
        }

        add(boardGrid, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Init alle som "ingen vinner enda"
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                boardWinners[row][col] = PieceColor.NONE;
    }

    @Override
    public void onBoardWon(PieceColor winner, int boardIndex) {
        int row = boardIndex / 3;
        int col = boardIndex % 3;
        boardWinners[row][col] = winner;

        // Farg ruten for å vise vinner
        boards[boardIndex].setBackground(winner == PieceColor.WHITE ? Color.GREEN : Color.RED);

        // Sjekk om noen har vunnet Tic Tac Toe
        if (checkTicTacToeWin(winner)) {
            statusLabel.setText(winner + " wins the game!");
            disableAllBoards();
            JOptionPane.showMessageDialog(this, winner + " wins the entire game!");
        } else {
            // Fortsett spillet
            currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            statusLabel.setText(currentPlayer + "'s turn");
        }
    }

    // Sjekker om en spiller har fått tre på rad
    private boolean checkTicTacToeWin(PieceColor color) {
        for (int i = 0; i < 3; i++) {
            // Rader
            if (boardWinners[i][0] == color && boardWinners[i][1] == color && boardWinners[i][2] == color)
                return true;
            // Kolonner
            if (boardWinners[0][i] == color && boardWinners[1][i] == color && boardWinners[2][i] == color)
                return true;
        }
        // Diagonaler
        if (boardWinners[0][0] == color && boardWinners[1][1] == color && boardWinners[2][2] == color)
            return true;
        if (boardWinners[0][2] == color && boardWinners[1][1] == color && boardWinners[2][0] == color)
            return true;

        return false;
    }

    private void disableAllBoards() {
        for (ChessBoardPanel board : boards) {
            board.disableBoard(); // finnes allerede i ChessBoardPanel
        }
    }
}
