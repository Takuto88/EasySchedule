import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

class Settings extends JFrame {
    Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Zeitplan", timetableTab());
        tabPane.addTab("Fächer", subjectGradeLevelPan(Utilities.subjects, true, Comparator.naturalOrder()));
        tabPane.addTab("Wahlpflicht", wahlpflichtTab());
        tabPane.addTab("Klassenstufen", subjectGradeLevelPan(Utilities.gradeLevels, false, Comparator.naturalOrder()));
        this.add(tabPane);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Utilities.save();
            }
        });
    }

    private JPanel timetableTab() {
        JPanel timeTable = Utilities.newBoxLayout();
        timeTable.setLayout(new BoxLayout(timeTable, BoxLayout.Y_AXIS));
        if (!Utilities.periods.isEmpty() && Utilities.periods.size() > 0) {
            for (int i = 0; i < Utilities.periods.size(); i++) {
                JComboBox<String> beginHours = new JComboBox<>(Utilities.hours);
                JComboBox<String> beginMinutes = new JComboBox<>(Utilities.minutes);
                JComboBox<String> endHours = new JComboBox<>(Utilities.hours);
                JComboBox<String> endMinutes = new JComboBox<>(Utilities.minutes);
                predictPeriodLength(beginHours, beginMinutes, endHours, endMinutes);
                try {
                    beginHours.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i).getBegin().getHour()));
                    endHours.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i).getEnd().getHour()));
                    beginMinutes.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i).getBegin().getMinute()));
                    endMinutes.setSelectedItem(Utilities.formatTime(Utilities.periods.get(i).getEnd().getMinute()));
                } catch (NullPointerException e) {
                    beginHours.setSelectedItem(0);
                    endHours.setSelectedItem(0);
                    beginMinutes.setSelectedItem(0);
                    endMinutes.setSelectedItem(0);
                    System.out.println("NullPointerException at timetable");
                }
                timeTable.add(Utilities.add(new JPanel(), new JLabel(i + ". Stunde: von "), beginHours,
                        new JLabel(":"), beginMinutes, new JLabel(" bis "), endHours,
                        new JLabel(":"), endMinutes));
            }
        } else {
            JComboBox<String> beginHours = new JComboBox<>(Utilities.hours);
            JComboBox<String> beginMinutes = new JComboBox<>(Utilities.minutes);
            JComboBox<String> endHours = new JComboBox<>(Utilities.hours);
            JComboBox<String> endMinutes = new JComboBox<>(Utilities.minutes);

            beginHours.addActionListener(e -> Utilities.enableAdvancedSelection(beginHours, endHours, Utilities.hours));
            predictPeriodLength(beginHours, beginMinutes, endHours, endMinutes);

            timeTable.add(Utilities.add(new JPanel(), new JLabel("0. Stunde: von "), beginHours, new JLabel(":"),
                    beginMinutes, new JLabel(" bis "), endHours, new JLabel(":"), endMinutes));
        }
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> Utilities.savePeriods(timeTable));

        JButton newPeriod = new JButton("Neue Stundenreihe");
        newPeriod.addActionListener(e -> newPeriodPanel(timeTable, this));

        JPanel buttonPan = new JPanel(new BorderLayout());
        buttonPan.add(Utilities.add(new JPanel(), newPeriod, Utilities.closeButton(this, null), save), BorderLayout.NORTH);
        return (JPanel) Utilities.add(timeTable, buttonPan);
    }

    private JPanel wahlpflichtTab() {
        JPanel wahlpflichtPan = new JPanel(new BorderLayout());
        JPanel buttonPan = new JPanel();
        JButton add = new JButton("Neues Fach");
        JButton delete = new JButton("Löschen");
        Utilities.add(buttonPan, add, delete);

        JList<Elective> electiveList = new JList<>();
        Utilities.defaultList(electiveList);
        Utilities.enableEdit(electiveList, Utilities.electives, Comparator.comparing(Elective::getName));
        JScrollPane pane = new JScrollPane(electiveList);
        pane.setPreferredSize(Utilities.panelSize);
        Utilities.updateList(Utilities.electives, electiveList, Comparator.comparing(Elective::getName));

        delete.addActionListener(e -> {
            int index = electiveList.getSelectedIndex();
            if (index != -1) {
                Utilities.electives.remove(index);
                Utilities.updateList(Utilities.electives, electiveList, Comparator.comparing(Elective::getName));
            }
        });

        add.addActionListener(e -> {
            JFrame frame = new JFrame("Neues Wahlpflichtfach");
            JPanel mainPan = Utilities.newBoxLayout();
            JComboBox<String> gradeSelector = new JComboBox<>(Utilities.gradeLevels.toArray(new String[0]));

            JTextField field = new JTextField();
            Utilities.format(field);

            JComboBox<String> days = new JComboBox<>(Utilities.weekdays);
            days.addActionListener(e2 -> newPanel(mainPan, frame));

            JComboBox<Period> periodsFrom = new JComboBox<>(Utilities.getPeriodsArray());
            periodsFrom.addActionListener(e2 -> newPanel(mainPan, frame));

            JComboBox<Period> periodsTo = new JComboBox<>(Utilities.getPeriodsArray());
            periodsTo.addActionListener(e2 -> newPanel(mainPan, frame));

            periodsFrom.addActionListener(e2 -> Utilities.enableAdvancedSelection(periodsFrom, periodsTo, Utilities.getPeriodsArray()));

            JButton addSubject = new JButton("Hinzufügen");
            addSubject.addActionListener(e2 -> {
                Elective elective = Utilities.getDuration(gradeSelector, field, mainPan);
                if (!elective.isIncomplete()) {
                    Utilities.electives.add(elective);
                    Utilities.updateList(Utilities.electives, electiveList, Comparator.comparing(Elective::getName));
                }
                frame.dispose();
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

        wahlpflichtPan.add(pane, BorderLayout.NORTH);
        wahlpflichtPan.add(buttonPan, BorderLayout.CENTER);

        return wahlpflichtPan;
    }

    private void newPanel(JPanel panel, JFrame frame) {
        ArrayList<Component> components = Arrays.stream(panel.getComponents()).collect(Collectors.toCollection(ArrayList::new));
        if (Utilities.readyForNewSubject(panel)) {
            JComboBox<String> days = new JComboBox<>(Utilities.weekdays);
            days.addActionListener(e2 -> newPanel(panel, frame));

            JComboBox<Period> periodsFrom = new JComboBox<>(Utilities.getPeriodsArray());
            periodsFrom.addActionListener(e2 -> newPanel(panel, frame));

            JComboBox<Period> periodsTo = new JComboBox<>(Utilities.getPeriodsArray());
            periodsTo.addActionListener(e -> newPanel(panel, frame));

            periodsFrom.addActionListener(e2 -> Utilities.enableAdvancedSelection(periodsFrom, periodsTo, Utilities.getPeriodsArray()));

            components.add(components.size()-1, Utilities.add(new JPanel(), days, new JLabel("von"), periodsFrom, new JLabel("bis"), periodsTo));
            panel.removeAll();
            components.forEach(panel::add);
            panel.repaint();
            frame.pack();
        }
    }

    private void newPeriodPanel(JPanel panel, JFrame frame) {
        ArrayList<Component> components = Arrays.stream(panel.getComponents()).collect(Collectors.toCollection(ArrayList::new));
        JComboBox<String> beginHours = new JComboBox<>(Utilities.hours);
        JComboBox<String> beginMinutes = new JComboBox<>(Utilities.minutes);
        JComboBox<String> endHours = new JComboBox<>(Utilities.hours);
        JComboBox<String> endMinutes = new JComboBox<>(Utilities.minutes);

        beginHours.addActionListener(e -> Utilities.enableAdvancedSelection(beginHours, endHours, Utilities.hours));
        predictPeriodLength(beginHours, beginMinutes, endHours, endMinutes);

        JPanel lastPan = ((JPanel) components.get(components.size() - 2));
        JComboBox<String> endHour = ((JComboBox<String>) lastPan.getComponent(5));
        JComboBox<String> endMinute = ((JComboBox<String>) lastPan.getComponent(7));

        LocalTime newBegin = LocalTime.of(Integer.parseInt(((String) endHour.getSelectedItem())), Integer.parseInt((String) endMinute.getSelectedItem()));
        LocalTime newEnd = newBegin.plusMinutes(45);

        beginHours.setSelectedItem(Utilities.formatTime(newBegin.getHour()));
        beginMinutes.setSelectedItem(Utilities.formatTime(newBegin.getMinute()));
        endHours.setSelectedItem(Utilities.formatTime(newEnd.getHour()));
        endMinutes.setSelectedItem(Utilities.formatTime(newEnd.getMinute()));

        components.add(components.size() - 1, Utilities.add(new JPanel(), new JLabel(components.size()-1 + ". Stunde: von "), beginHours,
                new JLabel(":"), beginMinutes, new JLabel(" bis "), endHours,
                new JLabel(":"), endMinutes));
        panel.removeAll();
        components.forEach(panel::add);
        panel.repaint();
        frame.pack();
    }

    private JPanel subjectGradeLevelPan(ArrayList<String> data, boolean isSubject, Comparator<String> sortBy) {
        JPanel subjectPan = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JList<String> subjectList = new JList<>();
        Utilities.enableEdit(subjectList, data, sortBy);
        subjectList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    deleteListener(subjectList, data, sortBy);
            }
        });
        Utilities.defaultList(subjectList);
        JScrollPane pane = new JScrollPane(subjectList);
        pane.setPreferredSize(Utilities.panelSize);
        Utilities.updateList(data, subjectList, sortBy);

        JPanel inputPan = new JPanel(new BorderLayout());
        inputPan.setMaximumSize(new Dimension(150, 50));
        JTextField subjectIn = new JTextField();
        subjectIn.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addListener(subjectList, subjectIn, data, sortBy, isSubject);
            }
        });
        JButton add = new JButton("Hinzufügen");
        Utilities.format(subjectIn, add);
        add.addActionListener(e -> addListener(subjectList, subjectIn, data, sortBy, isSubject));
        inputPan.setSize(new Dimension(300, 200));
        inputPan.add(subjectIn, BorderLayout.NORTH);
        inputPan.add(add, BorderLayout.SOUTH);

        JButton delete = new JButton("Löschen");
        delete.setAlignmentX(SwingConstants.LEFT);
        delete.addActionListener(e -> deleteListener(subjectList, data, sortBy));
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

    private void addListener(JList<String> subjectList, JTextField subjectIn, ArrayList<String> data, Comparator<String> sortBy, boolean isSubject) {
        if (Utilities.isInList(subjectIn.getText(), data))
            Utilities.alert((isSubject ? "Dieses Unterrichtsfach" : "Diese Klassenstufe") + " ist schon vorhanden.", null, null);
        else if (subjectIn.getText().equals(""))
            Utilities.alert("Bitte geben Sie ein Unterrichtsfach ein.",  null, null);
        else {
            data.add(subjectIn.getText());
            Utilities.updateList(data, subjectList, sortBy);
            subjectIn.setText("");
            subjectIn.requestFocus();
        }
    }

    private <T> void deleteListener(JList<T> subjectList, ArrayList<T> data, Comparator<T> sortBy) {
        int index = subjectList.getSelectedIndex();
        if (index != -1) {
            data.remove(index);
            Utilities.updateList(data, subjectList, sortBy);
        } else Utilities.alert("Bitte wählen Sie ein Element aus", null, null);
    }

    private void predictPeriodLength(JComboBox<String> beginHours, JComboBox<String> beginMinutes, JComboBox<String> endHours, JComboBox<String> endMinutes) {
        beginHours.addActionListener(e -> setPredicted(beginHours, beginMinutes, endHours, endMinutes));
        beginMinutes.addActionListener(e -> setPredicted(beginHours, beginMinutes, endHours, endMinutes));
    }

    private void setPredicted(JComboBox<String> beginHours, JComboBox<String> beginMinutes, JComboBox<String> endHours, JComboBox<String> endMinutes) {
        LocalTime begin = LocalTime.of(Integer.parseInt(((String) beginHours.getSelectedItem())), Integer.parseInt((String) beginMinutes.getSelectedItem()));
        LocalTime end = begin.plusMinutes(45);
        endHours.setSelectedItem(Utilities.formatTime(end.getHour()));
        endMinutes.setSelectedItem(Utilities.formatTime(end.getMinute()));
    }
}