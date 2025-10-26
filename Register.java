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



class PlayersData {
    List<Player> players = new ArrayList<>();

    @Override
    public String toString() {
        return "PlayersData{" +
                "players=" + players +
                '}';
    }
}

public class Register extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private static final String FILE_PATH = "players.json";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Register() {
        setTitle("Player Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);

        usernameField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(passLabel, gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        add(registerButton, gbc);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        add(messageLabel, gbc);

        registerButton.addActionListener(e -> handleRegistration());
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        PlayersData data = readPlayersFile();

        // Check if username already exists
        for (Player p : data.players) {
            if (p.name.equalsIgnoreCase(username)) {
                messageLabel.setText(" Username already exists!");
                return;
            }
        }
        System.out.println(data);
        String hashedPassword = md5(password);
        Player newPlayer = new Player(username, hashedPassword);
        data.players.add(newPlayer);
        System.out.println(data);
        savePlayersFile(data);

        messageLabel.setForeground(Color.GREEN);
        messageLabel.setText(" Registration successful!");
        usernameField.setText("");
        passwordField.setText("");
        // Create the window first, then pass it to the method
        Login loginWindow = new Login();
        CloseAllWindowUtil.closeAllWindowsAndOpenNew(loginWindow);



    }



    private PlayersData readPlayersFile() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.write(Paths.get(FILE_PATH), "{\"players\": []}".getBytes());
            }
            String json = Files.readString(Paths.get(FILE_PATH));
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            java.lang.reflect.Type listType = new TypeToken<List<Player>>(){}.getType();
            PlayersData data = new PlayersData();
            System.out.println(data);
            data.players = gson.fromJson(root.get("players"), listType);
            System.out.println(data);
            if (data.players == null) data.players = new ArrayList<>();
            System.out.println(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayersData();
        }
    }

    private void savePlayersFile(PlayersData data) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
