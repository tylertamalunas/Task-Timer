import javax.swing.*;

public class TimerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           new MultiTimerFrame().setVisible(true);
        });
    }
}