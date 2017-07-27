import oracle.jvm.hotspot.jfr.JFR;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

class TeacherGUI extends JFrame {

    private ArrayList<String> teachesSubjects = new ArrayList<>();

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
        frame.add(Utilities.add(new JPanel(), Utilities.closeButton(frame, "Abbrechen"), save), BorderLayout.SOUTH);
        Utilities.setDefault(save);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void notAvailable() {
        JFrame frame = new JFrame("Lehrkraft nicht einsetzbar");
    }
}
