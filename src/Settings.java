import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

    class Settings extends JFrame {
    Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Zeitplan", timetableTab());
        tabPane.addTab("Fächer", subjectGradeLevelPan(Utilities.subjects, true));
        tabPane.addTab("Wahlpflicht", wahlpflichtTab());
        tabPane.addTab("Klassenstufen", subjectGradeLevelPan(Utilities.gradeLevels, false));
        this.add(tabPane);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel timetableTab() {
        JPanel timeTable = Utilities.newBoxLayout();
        timeTable.setLayout(new BoxLayout(timeTable, BoxLayout.Y_AXIS));
        System.out.println();
        if (!Utilities.periods.isEmpty() && Utilities.periods.size() > 0) {
            for (int i = 0; i < Utilities.periods.size(); i++) {
                JComboBox<String> beginHours = new JComboBox<>(Utilities.hours);
                JComboBox<String> beginMinutes = new JComboBox<>(Utilities.minutes);
                JComboBox<String> endHours = new JComboBox<>(Utilities.hours);
                JComboBox<String> endMinutes = new JComboBox<>(Utilities.minutes);
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

        JList<String> subjectList = new JList<>();
        Utilities.defaultList(subjectList);
        JScrollPane pane = new JScrollPane(subjectList);
        pane.setPreferredSize(Utilities.panelSize);
        updateList(subjectList);

        delete.addActionListener(e -> {
            int index = subjectList.getSelectedIndex();
            if (index != -1) {
                Utilities.wahlpflicht.remove(index);
                updateList(subjectList);
            }
        });

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

            periodsFrom.addActionListener(e2 -> Utilities.enableAdvancedSelection(periodsFrom, periodsTo, Utilities.periodsList));

            JButton addSubject = new JButton("Hinzufügen");
            addSubject.addActionListener(e2 -> {
                String grade = gradeSelector.getSelectedItem().toString();
                String name = field.getText();
                HashMap<String, String> map = Utilities.getDuration(mainPan);
                if (!map.get("days").equals("") && !map.get("froms").equals("") && !map.get("tos").equals("")) {
                    map.put("grade", grade);
                    map.put("name", name);
                    Utilities.wahlpflicht.add(map);
                    updateList(subjectList);
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

            JComboBox<String> periodsFrom = new JComboBox<>(Utilities.periodsList);
            periodsFrom.addActionListener(e2 -> newPanel(panel, frame));

            JComboBox<String> periodsTo = new JComboBox<>(Utilities.periodsList);
            periodsTo.addActionListener(e -> newPanel(panel, frame));

            periodsFrom.addActionListener(e2 -> Utilities.enableAdvancedSelection(periodsFrom, periodsTo, Utilities.periodsList));

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

        components.add(components.size() - 1, Utilities.add(new JPanel(), new JLabel(components.size()-1 + ". Stunde: von "), beginHours,
                new JLabel(":"), beginMinutes, new JLabel(" bis "), endHours,
                new JLabel(":"), endMinutes));
        panel.removeAll();
        components.forEach(panel::add);
        panel.repaint();
        frame.pack();
    }

    private void updateList(JList<String> list) {
        DefaultListModel<String> model = new DefaultListModel<>();
        Utilities.wahlpflicht.sort(Comparator.comparing(e -> e.get("name")));
        Utilities.wahlpflicht.forEach(e -> {
            String name = e.get("name");
            String grade = e.get("grade");

            String[] days = e.get("days").split(", ");
            String[] from = e.get("froms").split(", ");
            String[] to = e.get("tos").split(", ");
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < days.length; i++)
                b.append(days[i]).append(", ").append(from[i]).append(" - ").append(to[i]).append("; ");
            model.addElement(name + ", " + grade + ".Klasse: " + b.toString());
        });
        list.setModel(model);
    }

    private JPanel subjectGradeLevelPan(ArrayList<String> data, boolean isSubject) {
        JPanel subjectPan = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JList<String> subjectList = new JList<>();
        subjectList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    deleteListener(subjectList, data);
            }
        });
        Utilities.defaultList(subjectList);
        JScrollPane pane = new JScrollPane(subjectList);
        pane.setPreferredSize(Utilities.panelSize);
        Utilities.updateList(data, subjectList);

        JPanel inputPan = new JPanel(new BorderLayout());
        inputPan.setMaximumSize(new Dimension(150, 50));
        JTextField subjectIn = new JTextField();
        subjectIn.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addListener(subjectList, subjectIn, data, isSubject);
            }
        });
        JButton add = new JButton("Hinzufügen");
        Utilities.format(subjectIn, add);
        add.addActionListener(e -> addListener(subjectList, subjectIn, data, isSubject));
        inputPan.setSize(new Dimension(300, 200));
        inputPan.add(subjectIn, BorderLayout.NORTH);
        inputPan.add(add, BorderLayout.SOUTH);

        JButton delete = new JButton("Löschen");
        delete.setAlignmentX(SwingConstants.LEFT);
        delete.addActionListener(e -> deleteListener(subjectList, data));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 10, 5, 10);
        subjectPan.add(pane, c);
        c.gridy = 2;
        subjectPan.add(delete, c);
        c.gridx = 1;
        c.gridy = 1;
        subjectPan.add(inputPan);
        data.sort(Utilities.numberComparator);
        return subjectPan;
    }

    private void addListener(JList<String> subjectList, JTextField subjectIn, ArrayList<String> data, boolean isSubject) {
        if (Utilities.isInList(subjectIn.getText(), data))
            Utilities.alert((isSubject ? "Dieses Unterrichtsfach" : "Diese Klassenstufe") + " ist schon vorhanden.", null, null);
        else if (subjectIn.getText().equals(""))
            Utilities.alert("Bitte geben Sie ein Unterrichtsfach ein.",  null, null);
        else {
            data.add(subjectIn.getText());
            data.sort(Utilities.numberComparator);
            Utilities.updateList(data, subjectList);
            subjectIn.setText("");
            subjectIn.requestFocus();
        }
    }

    private void deleteListener(JList<String> subjectList, ArrayList<String> data) {
        int index = subjectList.getSelectedIndex();
        if (index != -1) {
            data.remove(index);
            data.sort(Utilities.numberComparator);
            Utilities.updateList(data, subjectList);
        } else Utilities.alert("Bitte wähle ein Element aus", null, null);
    }
}