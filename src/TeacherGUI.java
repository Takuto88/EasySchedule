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
        Utilities.addMultiple(firstnamePan, new JLabel("Vorname: "), firstnameIn);
        Utilities.addMultiple(lastnamePan, new JLabel("Nachname: "), lastnameIn);
        JPanel namePan = Utilities.newBoxLayout();

        //left
        JRadioButton fifth = new JRadioButton("5. Klasse");
        JRadioButton sixth = new JRadioButton("6. Klasse");
        JRadioButton seventh = new JRadioButton("7. Klasse");
        JRadioButton eighth = new JRadioButton("8. Klasse");
        JRadioButton nineth = new JRadioButton("9. Klasse");
        JRadioButton tenth = new JRadioButton("10. Klasse");
        JRadioButton eleventh = new JRadioButton("11. Klasse");
        JRadioButton twelveth = new JRadioButton("12. Klasse");
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
        Utilities.addMultiple(subjectPan, new JLabel("Neues Fach:"), subjectIn, addSubject);
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
        Utilities.addMultiple(buttonPan, cancel, save);

        //right
        JPanel rightPan = Utilities.newBoxLayout();



        Utilities.format(firstnameIn, lastnameIn, subjectIn);

        this.add(Utilities.addMultiple(namePan, firstnamePan, lastnamePan), BorderLayout.NORTH);
        this.add(Utilities.addMultiple(radioPan, fifth, sixth, seventh, eighth, nineth, tenth, eleventh, twelveth), BorderLayout.WEST);
        this.add(Utilities.addMultiple(bottomPan, subjectsBox, subjectPan, buttonPan), BorderLayout.SOUTH);
        Utilities.setDefault(save);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void save() {

    }
}
