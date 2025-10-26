import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

public class GameDataUpdater {

    private static final String FILE_PATH = "players.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void updatePlayerStats(String username, boolean isWin) {
        PlayersData data = readPlayersFile();

        // Find the player by username
        Player playerToUpdate = null;
        for (Player p : data.players) {
            if (p.name.equalsIgnoreCase(username)) {
                playerToUpdate = p;
                break;
            }
        }

        if (playerToUpdate == null) {
            System.out.println("Player not found.");
            return;  // Player not found, no updates made
        }

        // Update stats based on the game result (win or lose)
        playerToUpdate.totalPlays++;  // Increment the number of games played

        if (isWin) {
            playerToUpdate.totalWins++;  // Increase wins if the player won
        } else {
            playerToUpdate.totalLose++;  // Increase losses if the player lost
        }

        // Save updated data back to the file
        savePlayersFile(data);

        System.out.println("Player stats updated successfully.");
    }

    private static PlayersData readPlayersFile() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.write(Paths.get(FILE_PATH), "{\"players\": []}".getBytes());
            }
            String json = Files.readString(Paths.get(FILE_PATH));
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            java.lang.reflect.Type listType = new TypeToken<List<Player>>() {}.getType();
            PlayersData data = new PlayersData();
            data.players = gson.fromJson(root.get("players"), listType);
            if (data.players == null) data.players = new ArrayList<>();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayersData();
        }
    }

    private static void savePlayersFile(PlayersData data) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
