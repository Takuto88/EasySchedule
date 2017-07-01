import java.util.ArrayList;

import javax.swing.*;

public class Settings extends JFrame {
    public Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Zeitplan", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Zeitplan")));
        tabPane.addTab("Fächer", addSubject());
        tabPane.addTab("Test", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Test")));
        this.add(tabPane);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }
    public static void main(String[] args)
    {
        new Settings();
    }
    static JPanel addSubject()
    {
        JPanel addSubject = new JPanel();

        ArrayList<String> subjects = new ArrayList<String>();
        JTextField userinput = new JTextField();
        userinput.setPreferredSize(new Dimension(100, 20));
        JButton add = new JButton("add");
        addSubject.add(userinput);
        addSubject.add(add);
        add.addActionListener(
                e -> {
                    subjects.add(userinput.getText());
                    userinput.setText("");
                    System.out.println(subjects);

                });
        return addSubject;
    }
}
