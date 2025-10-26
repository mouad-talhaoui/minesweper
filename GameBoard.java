import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GameBoard extends JFrame {
    // Inner class for mine tiles
    private class MineTile extends JButton {
        int r, c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 10;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    // Game frame components
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList = new ArrayList<>();
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;
    String username = "";
    // Constructor
    public GameBoard(String username) {
        // Set frame properties

        this.username = username;
        setTitle(username + " is playing ....");
        setSize(boardWidth, boardHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Label setup
        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        add(textPanel, BorderLayout.NORTH);

        // Board panel setup
        boardPanel.setLayout(new GridLayout(numRows, numCols));
        add(boardPanel, BorderLayout.CENTER);

        // Add MineTiles to board and add MouseListener for game logic
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            GameDataUpdater.updatePlayerStats(username, false);
                            SwingUtilities.invokeLater(() -> showDialog(username));
                        }
                        MineTile clickedTile = (MineTile) e.getSource();

                        // Left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (clickedTile.getText().isEmpty()) {
                                if (mineList.contains(clickedTile)) {
                                    revealMines();
                                } else {
                                    checkMine(clickedTile.r, clickedTile.c);
                                }
                            }
                        }
                        // Right click (flagging the tile)
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (clickedTile.getText().isEmpty() && clickedTile.isEnabled()) {
                                clickedTile.setText("\u2691");  // Set flag symbol
                            } else if (clickedTile.getText().equals("\u2691")) {
                                clickedTile.setText("");  // Remove flag
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }

        setMines();  // Set mines at random positions

        setVisible(true);  // Make frame visible
    }

    // Method to set mines at random positions
    void setMines() {
        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    // Method to reveal all mines when game ends
    void revealMines() {
        for (MineTile tile : mineList) {
            tile.setText("\u2622");  // Set mine symbol
        }

        gameOver = true;
        textLabel.setText("Game Over!");

    }

    // Method to check for neighboring mines recursively
    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) return;

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) return;

        tile.setEnabled(false);  // Disable tile after click
        tilesClicked++;

        int minesFound = 0;

        // Check 8 neighboring tiles
        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        } else {
            tile.setText("");
            checkMine(r - 1, c - 1);
            checkMine(r - 1, c);
            checkMine(r - 1, c + 1);
            checkMine(r, c - 1);
            checkMine(r, c + 1);
            checkMine(r + 1, c - 1);
            checkMine(r + 1, c);
            checkMine(r + 1, c + 1);
        }

        // Check for win condition
        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
            JOptionPane.showMessageDialog(
                    this, "\u2728 Congratulations! You cleared the board! \u2728", "You Win!", JOptionPane.INFORMATION_MESSAGE
            );
            GameDataUpdater.updatePlayerStats(username, true);
            SwingUtilities.invokeLater(() -> showDialog(username));
        }
    }

    // Method to count mines around a specific tile
    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) return 0;
        if (mineList.contains(board[r][c])) return 1;
        return 0;
    }

    // Method to show a dialog when the game ends
    public static void showDialog(String username) {
        // Create the dialog window with "Replay", "Quit", and "Main Menu" options
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(null);

        // Label
        JLabel messageLabel = new JLabel("Would you like to replay, quit, or return to main menu?", SwingConstants.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton replayButton = new JButton("Replay");
        JButton quitButton = new JButton("Quit");
        JButton mainMenuButton = new JButton("Main Menu");

        // Replay button action
        replayButton.addActionListener(e -> {
            dialog.dispose();
            new GameBoard(username);  // Restart the game
        });

        // Quit button action
        quitButton.addActionListener(e -> {
            dialog.dispose();
            System.exit(0);  // Exit the program
        });

        // Main Menu button action
        mainMenuButton.addActionListener(e -> {
            Minesweeper minesweeper = new Minesweeper();
            CloseAllWindowUtil.closeAllWindowsAndOpenNew(minesweeper);

        });

        // Add buttons to the panel
        buttonPanel.add(replayButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(mainMenuButton);

        // Add the button panel to the dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Display the dialog
        dialog.setVisible(true);
    }
}
