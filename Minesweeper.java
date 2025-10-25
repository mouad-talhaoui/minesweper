import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Minesweeper {
    
    public Minesweeper() {
        String jsonContent = "{\n" +
            "    \"players\": [\n" +
            "        {\n" +
            "            \"name\": \"Mouad\",\n" +
            "            \"totalPlays\": 10,\n" +
            "            \"hashedpassowrd\": \"5f4dcc3b5aa765d61d8327deb882cf99\",\n" +
            "            \"toalLose\": 3,\n" +
            "            \"totalWins\": 7\n" +
            "        }\n" +
            "    ]\n" +
            "}";


        try (FileWriter file = new FileWriter("players.json")) {
            file.write(jsonContent);
            System.out.println(" File 'players.json' created successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        new Register().setVisible(true);
    }
}