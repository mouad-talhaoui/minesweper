import com.google.gson.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerScore extends JFrame {

    // Constructor
    public PlayerScore() {
        // Initialize the player data
        List<Player> players = loadPlayersFromFile();

        // Set up the JFrame for displaying the table
        setTitle("Player Score");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model and add data
        String[] columnNames = {"Name", "Total Plays", "Wins", "Losses"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Player player : players) {
            Object[] row = {
                    player.getName(),
                    player.getTotalPlays(),
                    player.getTotalWins(),
                    player.getTotalLose()
            };
            tableModel.addRow(row);
        }

        // Create JTable with the table model and add to scroll pane
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        // Center the window and make it visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Load player data from players.json using Gson
    private List<Player> loadPlayersFromFile() {
        List<Player> players = new ArrayList<>();

        try (Reader reader = new FileReader("players.json")) {
            // Parse the JSON content using Gson
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray playersArray = jsonObject.getAsJsonArray("players");

            // Loop through the players and create Player objects
            for (JsonElement element : playersArray) {
                JsonObject playerObject = element.getAsJsonObject();

                String name = playerObject.get("name").getAsString();
                int totalPlays = playerObject.get("totalPlays").getAsInt();
                int totalWins = playerObject.get("totalWins").getAsInt();
                int totalLose = playerObject.get("totalLose").getAsInt();

                Player player = new Player(name, totalPlays, totalWins, totalLose);
                players.add(player);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return players;
    }

}
