import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Minesweeper extends JFrame {

    // Constructor
    public Minesweeper() {
        // Set up the frame properties
        setTitle("Minesweeper");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use BoxLayout to arrange buttons vertically (in a column)
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create buttons
        JButton registerButton = new JButton("Registration");
        JButton loginButton = new JButton("Login");
        JButton playerScoreButton = new JButton("Player Score");

        // Set button size and style
        Dimension buttonSize = new Dimension(200, 60);  // Common size for all buttons
        registerButton.setPreferredSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);
        playerScoreButton.setPreferredSize(buttonSize);

        // Set font and alignment for the buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);  // Font size 18
        registerButton.setFont(buttonFont);
        loginButton.setFont(buttonFont);
        playerScoreButton.setFont(buttonFont);

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add listeners for buttons
        registerButton.addActionListener(e -> {
            Register register = new Register();
            CloseAllWindowUtil.closeAllWindowsAndOpenNew(register);
        });

        loginButton.addActionListener(e -> {
            Login login = new Login();
            CloseAllWindowUtil.closeAllWindowsAndOpenNew(login);
        });

        playerScoreButton.addActionListener(e -> {
            PlayerScore playerScore = new PlayerScore();
            CloseAllWindowUtil.closeAllWindowsAndOpenNew(playerScore);
        });

        // Add buttons to the frame with spacing between them
        add(Box.createVerticalGlue()); // Adds flexible space at the top
        add(registerButton);
        add(Box.createVerticalStrut(20));  // Adds fixed space (20px) between buttons
        add(loginButton);
        add(Box.createVerticalStrut(20));  // Adds fixed space (20px) between buttons
        add(playerScoreButton);
        add(Box.createVerticalGlue()); // Adds flexible space at the bottom

        setLocationRelativeTo(null); // Center the window
    }

    // Method to create the file if it doesn't exist
    private void createFileIfNotExists() {
        File file = new File("players.json");
        if (!file.exists()) {
            String jsonContent = "{\n" +
                    "    \"players\": [\n" +
                    "        {\n" +
                    "            \"name\": \"Mouad\",\n" +
                    "            \"totalPlays\": 10,\n" +
                    "            \"hashedpassowrd\": \"5f4dcc3b5aa765d61d8327deb882cf99\",\n" +
                    "            \"totalLose\": 3,\n" +
                    "            \"totalWins\": 7\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(jsonContent);
                System.out.println("File 'players.json' created successfully!");
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("File already exists: " + file.getAbsolutePath());
        }
    }
}
