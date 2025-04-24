import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MultiTimerFrame extends JFrame {
    private JPanel contentPane;
    private List<TimerPanel> timers = new ArrayList<>();
    public MultiTimerFrame() {
        setTitle("Multi-Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5,5));

        // controls bar
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        JButton addTimerButton = new JButton("Add Timer");
        JButton resetAllButton = new JButton("Reset All");
        controlBar.add(addTimerButton);
        controlBar.add(resetAllButton);
        add(controlBar, BorderLayout.NORTH);

        // timer grid
        contentPane = new JPanel(new GridLayout(1, 0,1,1));
        add(new JScrollPane(contentPane), BorderLayout.CENTER);

        // listeners

        addTimerButton.addActionListener(e -> {
            JTextField nameField = new JTextField(10);
            SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.SECOND);
            JSpinner timeSpinner = new JSpinner(model);
            JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
            editor.getFormat().setLenient(false);
            timeSpinner.setEditor(editor);

            Object[] form = {
                    "Timer Name", nameField,
                    "Duration (hh:mm:ss)", timeSpinner
            };

            int choice = JOptionPane.showConfirmDialog(this, form, "Add New Timer", JOptionPane.OK_CANCEL_OPTION);
            if (choice != JOptionPane.OK_OPTION) {return;}

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty");
                return;
            }

            Date timeValue = (Date) timeSpinner.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeValue);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            int s = calendar.get(Calendar.SECOND);

            int totalSeconds = h * 3600 + m * 60 + s;

            TimerPanel tp = new TimerPanel(name, totalSeconds, this::removeTimer);

            timers.add(tp);
            contentPane.add(tp);
            contentPane.revalidate();
            contentPane.repaint();
            pack();
        });

        resetAllButton.addActionListener(e -> {
            for (TimerPanel timer : timers) {
                timer.reset();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void removeTimer(TimerPanel tp) {
        timers.remove(tp);
        contentPane.remove(tp);
        contentPane.revalidate();
        contentPane.repaint();
        pack();
    }
}
