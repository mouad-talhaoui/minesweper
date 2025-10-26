import javax.swing.*;
import java.awt.*;

public class CloseAllWindowUtil {
    public static void closeAllWindowsAndOpenNew(JFrame newWindow) {
        // Get all open windows
        Window[] windows = Window.getWindows();

        // Close all windows
        for (Window window : windows) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }

        // Open new window
        newWindow.setVisible(true);
    }
}