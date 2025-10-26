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

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private static final String FILE_PATH = "players.json";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Login() {
        setTitle("Player Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
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

        JButton LoginButton = new JButton("Login");
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        add(LoginButton, gbc);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        add(messageLabel, gbc);

        LoginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        PlayersData data = readPlayersFile();
        boolean isExist = false;
        String exisinghashedpassword = "";
        // Check if username already exists
        for (Player p : data.players) {
            if (p.name.equalsIgnoreCase(username)) {
                isExist = true;
                exisinghashedpassword = p.hashedpassowrd;
            }
        }

        if(!isExist){
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(" username does not exist");
            return;
        }



        String hashedPassword = md5(password);
        if(!compareMD5Passwords(exisinghashedpassword,hashedPassword)){
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(" Password does not match");
        }else{
            GameBoard loginWindow = new GameBoard(username);
            CloseAllWindowUtil.closeAllWindowsAndOpenNew(loginWindow);
        }


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
            data.players = gson.fromJson(root.get("players"), listType);
            if (data.players == null) data.players = new ArrayList<>();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayersData();
        }
    }

    private boolean compareMD5Passwords(String inputPassword, String storedHashedPassword) {
        return inputPassword.equals(storedHashedPassword);
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
