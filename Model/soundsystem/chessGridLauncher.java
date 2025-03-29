package Model.soundsystem;

import javax.swing.*;
import java.awt.*;

public class chessGridLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createChessGrid();
            }
        });
    }
    
    private static void createChessGrid() {
        JFrame frame = new JFrame("3x3 Chess Games");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set a 3x3 grid layout for the frame.
        frame.setLayout(new GridLayout(3, 3, 5, 5)); // 5-pixel gaps
        
        // Create and add 9 independent chess boards.
        for (int i = 0; i < 9; i++) {
            ChessBoardPanel boardPanel = new ChessBoardPanel();
            frame.add(boardPanel);
        }
        
        frame.setSize(1200, 1200); // Adjust size as needed.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

