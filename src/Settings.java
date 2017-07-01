import javax.swing.*;

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
}
