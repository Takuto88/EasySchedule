import oracle.jvm.hotspot.jfr.JFR;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

class TeacherGUI extends JFrame {

    private ArrayList<String> teachesSubjects = new ArrayList<>();
    private HashMap<DayOfWeek, ArrayList<PeriodRange>> notAvailable = new HashMap<>();

    TeacherGUI(boolean edit, JList<Teacher> list) {
        this.setLayout(new BorderLayout());

        //top
        JTextField firstnameIn = new JTextField();
        JTextField lastnameIn = new JTextField();
        JPanel firstnamePan = new JPanel();
        JPanel lastnamePan = new JPanel();
        Utilities.add(firstnamePan, new JLabel("Vorname: "), firstnameIn);
        Utilities.add(lastnamePan, new JLabel("Nachname: "), lastnameIn);
        JPanel namePan = Utilities.newBoxLayout();

        //left
        JPanel leftPan = Utilities.newBoxLayout();
        leftPan.setBorder(new EmptyBorder(20, 10, 20, 0));
        leftPan.setAlignmentY(Component.CENTER_ALIGNMENT);
        leftPan.add(new JLabel("Unterrichtet in Klassenstufen:"));
        for (String grade: Utilities.gradeLevels)
            leftPan.add(new JCheckBox(grade));

        //bottom
        JPanel bottomPan = Utilities.newBoxLayout();
        JPanel buttonPan = new JPanel();
        JButton cancel = new JButton("Abbrechen");
        cancel.addActionListener(e -> this.dispose());
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> save());
        Utilities.add(buttonPan, cancel, save);

        //right
        JPanel rightPan = new JPanel(new GridBagLayout());
        JButton subjectButton = new JButton("Unterrichtsfächer");
        subjectButton.addActionListener(e -> configureSubjects());

        JButton notAvailableButton = new JButton("Nicht einsetzbar");
        notAvailableButton.addActionListener(e -> notAvailable());

        JPanel testPan = Utilities.newBoxLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        testPan.setAlignmentY(Component.CENTER_ALIGNMENT);
        subjectButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        notAvailableButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        Utilities.add(testPan, subjectButton, notAvailableButton);

        rightPan.add(testPan, gbc);

        Utilities.format(firstnameIn, lastnameIn);

        this.add(Utilities.add(namePan, firstnamePan, lastnamePan), BorderLayout.NORTH);
        this.add(leftPan, BorderLayout.WEST);
        this.add(Utilities.add(bottomPan, buttonPan), BorderLayout.SOUTH);
        this.add(rightPan, BorderLayout.EAST);
        Utilities.setDefault(save);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void save() {

    }

    private void configureSubjects() {
        JFrame frame = new JFrame("Unterrichtsfächer");
        frame.setLayout(new BorderLayout());
        JPanel boxPanel = Utilities.newBoxLayout();
        for (String subject: Utilities.subjects) {
            JCheckBox box = new JCheckBox(subject);
            if (Utilities.isInList(subject, teachesSubjects))
                box.setSelected(true);
            boxPanel.add(box);
        }
        frame.add(boxPanel, BorderLayout.CENTER);
        JButton save = new JButton("Speichern");
        save.addActionListener(k -> {
            teachesSubjects = Arrays.stream(boxPanel.getComponents()).map(e -> (JCheckBox)e).filter(JCheckBox::isSelected).
                    map(JCheckBox::getText).collect(Collectors.toCollection(ArrayList::new));
            frame.dispose();
        });
        frame.add(Utilities.add(new JPanel(), Utilities.closeButton(frame, null), save), BorderLayout.SOUTH);
        Utilities.setDefault(save);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void notAvailable() {
        JFrame frame = new JFrame("Lehrkraft nicht einsetzbar");
        frame.setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Montag", weekdays(frame, DayOfWeek.MONDAY));
        tabs.addTab("Dienstag", weekdays(frame, DayOfWeek.TUESDAY));
        tabs.addTab("Mittwoch", weekdays(frame, DayOfWeek.WEDNESDAY));
        tabs.addTab("Donnerstag", weekdays(frame, DayOfWeek.THURSDAY));
        tabs.addTab("Freitag", weekdays(frame, DayOfWeek.FRIDAY));
        frame.add(tabs, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> saveNotAvailable(tabs));
        frame.add(Utilities.add(panel, Utilities.closeButton(frame, null), save), BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel weekdays(JFrame frame, DayOfWeek day) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JComboBox<Period> from = new JComboBox<>(Utilities.getPeriodsArray());
        JComboBox<Period> to = new JComboBox<>(Utilities.getPeriodsArray());
        from.addActionListener(e -> Utilities.enableAdvancedSelection(from, to, Utilities.getPeriodsArray()));

        JButton newRow = new JButton("Neue Reihe");
        newRow.addActionListener(e -> newRow(panel, frame, null, null));

        panel.add(Utilities.add(new JPanel(), from, new JLabel(" bis "), to));
        panel.add(Utilities.add(new JPanel(), newRow));
        try {
            notAvailable.get(day).forEach(r -> newRow(panel, frame, r.getFrom(), r.getTo()));
        } catch (NullPointerException e) {
            System.out.println("No presaved data");
        }
        panel.setBorder(new EmptyBorder(0, 100, 0, 100));
        return panel;
    }

    private void newRow(JPanel panel, JFrame frame, Period begin, Period end) {
        ArrayList<Component> components = Arrays.stream(panel.getComponents()).collect(Collectors.toCollection(ArrayList::new));

        JComboBox<Period> from = new JComboBox<>(Utilities.getPeriodsArray());
        if (begin != null) from.setSelectedItem(begin);
        JComboBox<Period> to = new JComboBox<>(Utilities.getPeriodsArray());
        if (end != null) to.setSelectedItem(end);
        from.addActionListener(e -> Utilities.enableAdvancedSelection(from, to, Utilities.getPeriodsArray()));

        components.add(components.size() - 1, Utilities.add(new JPanel(), from, new JLabel(" bis "), to));
        panel.removeAll();
        components.forEach(panel::add);
        panel.repaint();
        panel.revalidate();
        frame.pack();
    }

    private void saveNotAvailable(JTabbedPane tabs) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            DayOfWeek day = DayOfWeek.of(i + 1);
            JPanel tempPan = ((JPanel) tabs.getTabComponentAt(i));
            ArrayList<JPanel> panels = Arrays.stream(tempPan.getComponents()).map(e -> (JPanel)e).collect(Collectors.toCollection(ArrayList::new));
            panels.remove(panels.size()-1);
            ArrayList<PeriodRange> ranges = new ArrayList<>();
            for (int j = 0; j < panels.size(); j++) {
                JComboBox<Period> fromBox = ((JComboBox<Period>) panels.get(i).getComponent(0));
                JComboBox<Period> toBox = ((JComboBox<Period>) panels.get(i).getComponent(2));
                Period from = (Period)fromBox.getSelectedItem();
                Period to = (Period)toBox.getSelectedItem();
                if (!from.isDefaultPeriod() && !to.isDefaultPeriod())
                    ranges.add(new PeriodRange(from, to));
            }
            notAvailable.clear();
            notAvailable.put(day, ranges);
        }
    }
}