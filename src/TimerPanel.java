import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class TimerPanel extends JPanel {
    private JLabel nameLabel, timeLabel;
    private JButton startButton, stopButton, resetButton, deleteButton;
    private Timer timer;
    private int originalSeconds, remainingSeconds;
    private Consumer<TimerPanel> onDelete;
    private final Color originalBg;
    private Timer flashTimer;
    private int flashCount = 0;

    public TimerPanel(String name, int seconds, Consumer<TimerPanel> onDelete) {
        this.originalBg = getBackground();
        this.nameLabel = new JLabel(name);
        this.timeLabel = new JLabel(formatTime(seconds));
        this.startButton = new JButton("Start");
        startButton.setMargin(new Insets(1,1,1,1));
        this.stopButton = new JButton("Stop");
        stopButton.setMargin(new Insets(1,1,1,1));
        this.resetButton = new JButton("Reset");
        resetButton.setMargin(new Insets(1,1,1,1));
        this.onDelete = onDelete;
        this.deleteButton = new JButton("Delete");
        deleteButton.setMargin(new Insets(1,1,1,1));
        this.originalSeconds = seconds;
        this.remainingSeconds = seconds;

        flashTimer = new Timer(500, (ActionEvent e) -> {
            if ((flashCount % 2) == 0) {
                setBackground(Color.GREEN);
            } else {
                setBackground(originalBg);
            }
            flashCount++;
            if (flashCount >= 20) {
                flashTimer.stop();
                flashCount = 0;
                setBackground(originalBg);
            }
            repaint();
        });
        flashTimer.setRepeats(true);

        // layout
        setLayout(new GridLayout(2, 1));
        JPanel displayRow = new JPanel(new GridLayout(2,1,0,2));
        displayRow.add(nameLabel);
        displayRow.add(timeLabel);
        add(displayRow);

        Border line = BorderFactory.createLineBorder(Color.black,1);
        Border padding = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder(BorderFactory.createCompoundBorder(line,padding));

        JPanel controlRow = new JPanel(new FlowLayout());
        controlRow.add(startButton);
        controlRow.add(stopButton);
        controlRow.add(resetButton);
        controlRow.add(deleteButton);
        add(controlRow);

        // button wiring
        this.resetButton.addActionListener(e -> {reset();});
        this.stopButton.addActionListener(e -> {stop();});
        this.startButton.addActionListener(e -> {start();});
        this.deleteButton.addActionListener(e -> {
            timer.stop();
            onDelete.accept(this);});

        // timer logic
        timer = new Timer(1000, e -> {
            remainingSeconds--;
            updateLabel();
            if (remainingSeconds <= 0) {
                timer.stop();
                flashTimer.start();
            }
        });
    }


    void start() {timer.start();}

    void stop() {timer.stop();}

    void reset() {
        remainingSeconds = originalSeconds;
        updateLabel();
    }
    void updateLabel() {
        timeLabel.setText(formatTime(remainingSeconds));
    }
    String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int mins = (totalSeconds % 3600) / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }
}
