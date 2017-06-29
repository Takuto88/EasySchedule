import javax.swing.*;

public class LehrerGUI extends JFrame {
    public LehrerGUI(boolean edit, JList<Lehrer> list) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JTextField firstnameIn = new JTextField();
        JTextField lastnameIn = new JTextField();
        JPanel firstnamePan = new JPanel();
        JPanel lastnamePan = new JPanel();

        JRadioButton fifth = new JRadioButton("5. Klasse");
        JRadioButton sixth = new JRadioButton("6. Klasse");
        JRadioButton seventh = new JRadioButton("7. Klasse");
        JRadioButton eighth = new JRadioButton("8. Klasse");
        JRadioButton nineth = new JRadioButton("9. Klasse");
        JRadioButton tenth = new JRadioButton("10. Klasse");
        JRadioButton eleventh = new JRadioButton("11. Klasse");
        JRadioButton twelveth = new JRadioButton("12. Klasse");
        JPanel radioPan = new JPanel();

        this.add(Utilities.addMultiple(firstnamePan, new JLabel("Vorname: "), firstnameIn));
        this.add(Utilities.addMultiple(lastnamePan, new JLabel("Nachname: "), lastnameIn));
        this.add(Utilities.addMultiple(radioPan, fifth, sixth, seventh, eighth, nineth, tenth, eleventh, twelveth));
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
