package Model.soundsystem;

import javax.swing.*;
import java.awt.*;

public class MainGamePanel extends JPanel implements ChessBoardListener {
    private ChessBoardPanel[] boards = new ChessBoardPanel[9];
    private PieceColor[][] boardWinners = new PieceColor[3][3]; // Hvem som vant hvert brett
    private PieceColor currentPlayer = PieceColor.WHITE;

    // Player-specific timer labels
    private JLabel whiteTimerLabel = new JLabel("60", SwingConstants.CENTER);
    private JLabel blackTimerLabel = new JLabel("60", SwingConstants.CENTER);
    private JLabel turnLabel = new JLabel("WHITE", SwingConstants.CENTER); // Label for current turn

    private Timer whiteTimer;
    private Timer blackTimer;
    private int whiteTimeRemaining = 60; // 60 seconds for White
    private int blackTimeRemaining = 60; // 60 seconds for Black

    public MainGamePanel() {
        setLayout(new BorderLayout());

        JPanel boardGrid = new JPanel(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            boards[i] = new ChessBoardPanel();
            boards[i].setListener(this, i); // viktig!
            boardGrid.add(boards[i]);
        }

        // Wrap the boardGrid in a container panel
        JPanel container = new JPanel(new BorderLayout());
        container.add(boardGrid, BorderLayout.CENTER);

        // Create the left padding panel
        JPanel leftPadding = new JPanel();
        leftPadding.setLayout(new BoxLayout(leftPadding, BoxLayout.Y_AXIS)); // Use vertical BoxLayout
        leftPadding.setPreferredSize(new Dimension(200, 0)); // Adjust width as needed

        // Add timer labels and turn label to the padding panel
        whiteTimerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        blackTimerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 20)); // Larger font for the turn label
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.LIGHT_GRAY); // Background color for the turn label
        turnLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the label

        // Add components to the leftPadding panel with spacing
        leftPadding.add(Box.createVerticalGlue()); // Push everything to the center vertically
        leftPadding.add(whiteTimerLabel);
        leftPadding.add(Box.createVerticalStrut(20)); // Add space between the white timer and turn label
        leftPadding.add(turnLabel);
        leftPadding.add(Box.createVerticalStrut(20)); // Add space between the turn label and black timer
        leftPadding.add(blackTimerLabel);
        leftPadding.add(Box.createVerticalGlue()); // Push everything to the center vertically

        // Wrap the leftPadding panel in a container to center it horizontally and vertically
        JPanel leftContainer = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        leftContainer.add(leftPadding); // Add the leftPadding panel to the center

        // Add the leftContainer to the container panel
        container.add(leftContainer, BorderLayout.WEST);

        // Add the container to the main panel
        add(container, BorderLayout.CENTER);


        // Init alle som "ingen vinner enda"
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boardWinners[row][col] = PieceColor.NONE;
            }
        }

        initializeTimers();

    }

    @Override
    public void onBoardWon(PieceColor winner, int boardIndex) {
        int row = boardIndex / 3;
        int col = boardIndex % 3;
        boardWinners[row][col] = winner;

        boards[boardIndex].showOverlaySymbol(winner == PieceColor.WHITE ? 'X' : 'O');


        // Farg ruten for å vise vinner
        boards[boardIndex].setBackground(winner == PieceColor.WHITE ? Color.GREEN : Color.RED);

        // Sjekk om noen har vunnet Tic Tac Toe
        if (checkTicTacToeWin(winner)) {
            disableAllBoards();
            JOptionPane.showMessageDialog(this, winner + " wins the entire game!");
        } else {
            // Fortsett spillet
            currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

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

    private void initializeTimers() {
        // Timer for White
        whiteTimer = new Timer(1000, e -> {
            whiteTimeRemaining--;
            whiteTimerLabel.setText("" + whiteTimeRemaining);
            if (whiteTimeRemaining <= 0) {
                whiteTimer.stop();
                JOptionPane.showMessageDialog(this, "White's time is up! Black wins!");
                disableAllBoards();
            }
        });

        // Timer for Black
        blackTimer = new Timer(1000, e -> {
            blackTimeRemaining--;
            blackTimerLabel.setText("" + blackTimeRemaining);
            if (blackTimeRemaining <= 0) {
                blackTimer.stop();
                JOptionPane.showMessageDialog(this, "Black's time is up! White wins!");
                disableAllBoards();
            }
        });

        // Start the timer for the first player (White)
        whiteTimer.start();
    }

    public void swapTurnOnAllBoards(PieceColor newPlayer){
        for (int i = 0; i < 9; i++) {
            boards[i].setCurrentPlayer(newPlayer);
        }
    }

    public void onMoveMade(int boardIndex) {
        // Switch turns globally
        currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        swapTurnOnAllBoards(currentPlayer);

        // Update the turn label
        turnLabel.setText(currentPlayer == PieceColor.WHITE ? "WHITE" : "BLACK");

        // Stop the current player's timer and start the next player's timer
        if (currentPlayer == PieceColor.WHITE) {
            blackTimer.stop();
            whiteTimer.start();
        } else {
            whiteTimer.stop();
            blackTimer.start();
        }

    }

}
