import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TeacherGUI extends JFrame {
    public TeacherGUI(boolean edit, JList<Teacher> list) {
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
        JCheckBox fifth = new JCheckBox("5. Klasse");
        JCheckBox sixth = new JCheckBox("6. Klasse");
        JCheckBox seventh = new JCheckBox("7. Klasse");
        JCheckBox eighth = new JCheckBox("8. Klasse");
        JCheckBox nineth = new JCheckBox("9. Klasse");
        JCheckBox tenth = new JCheckBox("10. Klasse");
        JCheckBox eleventh = new JCheckBox("11. Klasse");
        JCheckBox twelveth = new JCheckBox("12. Klasse");
        JPanel radioPan = Utilities.newBoxLayout();
        radioPan.setBorder(new EmptyBorder(20, 10, 20, 0));
        radioPan.setAlignmentY(Component.CENTER_ALIGNMENT);

        //bottom
        JPanel bottomPan = Utilities.newBoxLayout();
        JComboBox<String> subjectsBox = new JComboBox<>();
        JPanel subjectPan = new JPanel();
        subjectPan.setBorder(new EmptyBorder(10, 20, 20, 20));
        ArrayList<String> subjects = new ArrayList<>();
        JTextField subjectIn = new JTextField();
        JButton addSubject = new JButton("Hinzuf\u00fcgen");
        Utilities.add(subjectPan, new JLabel("Neues Fach:"), subjectIn, addSubject);
        addSubject.addActionListener(e -> {
            String text = subjectIn.getText();
            if (text.length() != 0) {
                subjects.add(text);
                subjectsBox.removeAllItems();
                subjects.forEach(subjectsBox::addItem);
                subjectIn.setText("");
            }
        });
        JPanel buttonPan = new JPanel();
        JButton cancel = new JButton("Abbrechen");
        cancel.addActionListener(e -> this.dispose());
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> save());
        Utilities.add(buttonPan, cancel, save);

        //right
        JPanel rightPan = Utilities.newBoxLayout();



        Utilities.format(firstnameIn, lastnameIn, subjectIn);

        this.add(Utilities.add(namePan, firstnamePan, lastnamePan), BorderLayout.NORTH);
        this.add(Utilities.add(radioPan, fifth, sixth, seventh, eighth, nineth, tenth, eleventh, twelveth), BorderLayout.WEST);
        this.add(Utilities.add(bottomPan, subjectsBox, subjectPan, buttonPan), BorderLayout.SOUTH);
        Utilities.setDefault(save);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void save() {

    }
}
