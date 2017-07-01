import javax.swing.*;
import java.awt.*;

public class Settings extends JFrame {
    public Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        JPanel timeTable = Utilities.newBoxLayout();
        for (int i = 0; i < 16; i++)
            timeTable.add(Utilities.add(new JPanel(), new JLabel(i + ". Stunde: von "), new JComboBox<>(Utilities.hours),
                    new JLabel(":"), new JComboBox<>(Utilities.minutes), new JLabel(" bis "), new JComboBox<>(Utilities.hours),
                    new JLabel(":"), new JComboBox<>(Utilities.minutes)));
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
        JPanel subjectPan = new JPanel(new BorderLayout());

        JList<String> subjectList = new JList<>();
        ((DefaultListCellRenderer) subjectList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane pane = new JScrollPane(subjectList);

        JPanel inputPan = new JPanel(new BorderLayout());
        JTextField subjectIn = new JTextField();
        JButton add = new JButton("Hinzufügen");
        Utilities.format(subjectIn, add);
        add.addActionListener(e -> {
            Utilities.subjects.add(subjectIn.getText());
            subjectIn.setText("");
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
                Utilities.updateList(Utilities.subjects, subjectList);
            } else Utilities.alert("Bitte wähle ein Element aus", null, null);
        });
        subjectPan.add(pane, BorderLayout.WEST);
        subjectPan.add(inputPan, BorderLayout.EAST);
        subjectPan.add(Utilities.add(new JPanel(), delete), BorderLayout.SOUTH);
        return subjectPan;
    }
}
