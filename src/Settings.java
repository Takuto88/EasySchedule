import javax.swing.*;

public class Settings extends JFrame {
    public Settings() {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Zeitplan", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Zeitplan")));
        tabPane.addTab("Fächer", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Fächer")));
        tabPane.addTab("Test", Utilities.addMultiple(Utilities.newBoxLayout(), new JButton("Test")));
        this.add(tabPane);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
