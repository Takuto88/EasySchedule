import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class Settings extends JFrame {
    public Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        JPanel timeTable = Utilities.newBoxLayout();
        for (int i = 0; i < 16; i++) {
            JComboBox<String> beginHours = new JComboBox<>(Utilities.hours);
            JComboBox<String> beginMinutes = new JComboBox<>(Utilities.minutes);
            JComboBox<String> endHours = new JComboBox<>(Utilities.hours);
            JComboBox<String> endMinutes = new JComboBox<>(Utilities.minutes);
            if(!Utilities.periods.isEmpty()) {
                beginHours.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i + "begin").getHour()));
                endHours.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i + "end").getHour()));
                beginMinutes.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i + "begin").getMinute()));
                endMinutes.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i + "end").getMinute()));
            }
            timeTable.add(Utilities.add(new JPanel(), new JLabel(i + ". Stunde: von "), beginHours,
                    new JLabel(":"), beginMinutes, new JLabel(" bis "), endHours,
                    new JLabel(":"), endMinutes));
        }
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> Utilities.savePeriods(timeTable));
        tabPane.addTab("Zeitplan", Utilities.add(Utilities.newBoxLayout(), timeTable,
                Utilities.add(new JPanel(), Utilities.closeButton(this, null), save)));
        tabPane.addTab("Fächer", subjectTab());
        tabPane.addTab("Wahlpflicht", Utilities.add(Utilities.newBoxLayout(), new JButton("Wahlpflicht")));
        this.add(tabPane);
        Utilities.setDefault(save);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel subjectTab() {
        JPanel subjectPan = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JList<String> subjectList = new JList<>();
        ((DefaultListCellRenderer) subjectList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        subjectList.setFont(Utilities.defaultFont);
        JScrollPane pane = new JScrollPane(subjectList);
        pane.setPreferredSize(Utilities.panelSize);
        Utilities.updateList(Utilities.subjects, subjectList);

        JPanel inputPan = new JPanel(new BorderLayout());
        inputPan.setMaximumSize(new Dimension(150, 50));
        JTextField subjectIn = new JTextField();
        JButton add = new JButton("Hinzufügen");
        Utilities.format(subjectIn, add);
        add.addActionListener(e -> {
            if (Utilities.isInList(subjectIn.getText(), Utilities.subjects))
                Utilities.alert("Dieses Unterrichtsfach ist schon vorhanden.", null, null);
            else {
                Utilities.subjects.add(subjectIn.getText());
                Collections.sort(Utilities.subjects);
                Utilities.updateList(Utilities.subjects, subjectList);
                subjectIn.setText("");
                subjectIn.requestFocus();
            }
        });
        inputPan.setSize(new Dimension(300, 200));
        inputPan.add(subjectIn, BorderLayout.NORTH);
        inputPan.add(add, BorderLayout.SOUTH);

        JButton delete = new JButton("Löschen");
        delete.setAlignmentX(SwingConstants.LEFT);
        delete.addActionListener(e -> {
            int index = subjectList.getSelectedIndex();
            if (index != -1) {
                Utilities.subjects.remove(index);
                Collections.sort(Utilities.subjects);
                Utilities.updateList(Utilities.subjects, subjectList);
            } else Utilities.alert("Bitte wähle ein Element aus", null, null);
        });
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 10, 5, 10);
        subjectPan.add(pane, c);
        c.gridy = 2;
        subjectPan.add(delete, c);
        c.gridx = 1;
        c.gridy = 1;
        subjectPan.add(inputPan);
        return subjectPan;
    }
}
