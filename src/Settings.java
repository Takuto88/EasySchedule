import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Settings extends JFrame {
    public Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Zeitplan", Utilities.addMultiple(Utilities.newBoxLayout(), Utilities.generateTimes(this)));
        tabPane.addTab("Wahlpflicht", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Wahlplflicht")));
        tabPane.addTab("Fächer", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Fächer")));
        this.add(tabPane);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel addSubject() {
        JPanel addSubject = new JPanel();
        ArrayList<String> subjects = new ArrayList<>();
        JTextField userinput = new JTextField();
        userinput.setPreferredSize(new Dimension(100, 20));
        JButton add = new JButton("Hinzufügen");
        addSubject.add(userinput);
        addSubject.add(add);
        add.addActionListener(e -> {
            subjects.add(userinput.getText());
            userinput.setText("");
        });
        return addSubject;
    }
}
