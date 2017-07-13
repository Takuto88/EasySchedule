import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        tabPane.addTab("Wahlpflicht", wahlpflichtTab());
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
        Utilities.centerList(subjectList);
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

    private JPanel wahlpflichtTab() {
        JPanel wahlpflichtPan = new JPanel(new BorderLayout());
        JPanel buttonPan = new JPanel();
        JButton add = new JButton("Neues Fach");
        JButton delete = new JButton("Löschen");
        Utilities.add(buttonPan, add, delete);

        add.addActionListener(e -> {
            JFrame frame = new JFrame("Neues Wahlpflichtfach");
            JPanel mainPan = Utilities.newBoxLayout();
            JComboBox<Integer> gradeSelector = new JComboBox<>(new Integer[]{5,6,7,8,9,10,11,12});

            JTextField field = new JTextField();
            Utilities.format(field);

            JComboBox<String> days = new JComboBox<>(Utilities.weekdays);
            days.addActionListener(e2 -> newPanel(mainPan, frame));
            JComboBox<String> periodsFrom = new JComboBox<>(Utilities.periodsList);
            periodsFrom.addActionListener(e2 -> newPanel(mainPan, frame));
            JComboBox<String> periodsTo = new JComboBox<>(Utilities.periodsList);
            periodsTo.addActionListener(e2 -> newPanel(mainPan, frame));

            JButton addSubject = new JButton("Hinzufügen");
            addSubject.addActionListener(e2 -> {
                String grade = gradeSelector.getSelectedItem().toString();
                String name = field.getText();
                HashMap<String, String> map = Utilities.getDuration(mainPan);
                map.put("grade", grade);
                map.put("name", name);
                Utilities.wahlpflicht.add(map);
            });

            Utilities.add(mainPan, Utilities.add(new JPanel(), new JLabel("Klassenstufe:"), gradeSelector));
            Utilities.add(mainPan, Utilities.add(new JPanel(), new JLabel("Name des Fachs:"), field));
            Utilities.add(mainPan, Utilities.add(new JPanel(), days, new JLabel("von"), periodsFrom, new JLabel("bis"), periodsTo));
            Utilities.add(mainPan, Utilities.add(new JPanel(), addSubject));

            frame.add(mainPan);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });

        JList<String> subjectList = new JList<>();
        Utilities.centerList(subjectList);
        JScrollPane pane = new JScrollPane(subjectList);
        pane.setPreferredSize(Utilities.panelSize);

        wahlpflichtPan.add(pane, BorderLayout.NORTH);
        wahlpflichtPan.add(buttonPan, BorderLayout.CENTER);

        return wahlpflichtPan;
    }

    private void newPanel(JPanel panel, JFrame frame) {
        ArrayList<Component> components = Arrays.stream(panel.getComponents()).collect(Collectors.toCollection(ArrayList::new));
        if (Utilities.readyForNewSubject(panel)) {
            JComboBox<String> days = new JComboBox<>(Utilities.weekdays);
            days.addActionListener(e2 -> newPanel(panel, frame));

            JComboBox<String> periodsFrom = new JComboBox<>(Utilities.periodsList);
            periodsFrom.addActionListener(e2 -> newPanel(panel, frame));

            JComboBox<String> periodsTo = new JComboBox<>(Utilities.periodsList);
            periodsTo.addActionListener(e2 -> newPanel(panel, frame));

            components.add(components.size()-1, Utilities.add(new JPanel(), days, new JLabel("von"), periodsFrom, new JLabel("bis"), periodsTo));
            panel.removeAll();
            components.forEach(panel::add);
            panel.repaint();
            frame.pack();
        }
    }
}
