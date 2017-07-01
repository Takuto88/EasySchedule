import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Utilities implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Dimension panelSize = new Dimension(350, 450);
    private static final String path = System.getProperty("user.home") + "/Desktop/stundenplan.ser";
    static final Font defaultFont = new Font("Roboto", Font.PLAIN, 14);
    private static final String[] hours = {"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};
    private static final String[] minutes = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
    private static HashMap<String, LocalTime> periods = new HashMap<>();
    private boolean isConfigured = false;

    static ArrayList<Teacher> teachers = new ArrayList<>();
    static ArrayList<Grade> classes= new ArrayList<>();

    static void updateList(ArrayList<?> data, JList<String> list) {
        DefaultListModel<String> model = new DefaultListModel<>();
        data.forEach(e -> model.addElement(e.toString()));
        list.setModel(model);
    }

    static void alert(String text, ActionListener listener, String title) {
        JFrame frame = new JFrame(title == null ? "Error!" : title);
        frame.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(new JLabel(text));
        JButton button = new JButton("OK");
        panel2.add(closeButton(frame, null));
        panel2.add(button);
        button.addActionListener(listener == null ? e -> frame.dispose() : listener);
        button.addActionListener(e -> frame.dispose());
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void setDefault(JButton b) {
        JRootPane pane = b.getRootPane();
        pane.setDefaultButton(b);
    }

    static <T> boolean isInList(T var, ArrayList<T> list) {
        for (T tmp : list) if (tmp.equals(var)) return true;
        return false;
    }

    static Number round(Number number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return number instanceof Double ? Double.parseDouble(format.format(number))
                : Integer.parseInt(format.format(number));
    }

    static void format(JComponent... jComponents) {
        Arrays.stream(jComponents).forEach(e -> {
            if (e instanceof JButton) e.setPreferredSize(new Dimension(120, 30));
            else e.setPreferredSize(new Dimension(150, 20));
        });
    }

    static void save() {
        try {
            if (!new File(path).exists())
                new File(path).createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            ArrayList<ArrayList<? extends Main>> objects = new ArrayList<>();
            objects.add(teachers);
            objects.add(classes);
            out.writeObject(objects);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    static void load() {
        if (new File(path).exists()) try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            ArrayList<ArrayList<? extends Main>> objects = ((ArrayList<ArrayList<? extends Main>>) in.readObject());
            teachers = (ArrayList<Teacher>) objects.get(0);
            classes = ((ArrayList<Grade>) objects.get(1));
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Integer> getTeachesIn(JPanel panel) {
        return Arrays.stream(panel.getComponents()).map(e -> (JRadioButton)e).filter(JRadioButton::isSelected).
                map(JRadioButton::getText).map(e -> e.charAt(0) == '1' ? e.substring(0, 2) : e.substring(0, 1)).
                map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
    }

    static JComponent addMultiple(JComponent con, JComponent... components) {
        Arrays.stream(components).forEach(con::add);
        return con;
    }

    static JPanel newBoxLayout() {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        return pan;
    }

    static void isConfigured() {

    }

    static String getUserInput(String prompt) {
        return JOptionPane.showInputDialog(prompt, JOptionPane.INFORMATION_MESSAGE);
    }

    static JComponent[] generateTimes(JFrame frame) {
        JComponent[] panels = new JComponent[17];
        for (int i = 0; i < 16; i++)
            panels[i] = addMultiple(new JPanel(), new JLabel(i + ". Stunde: von "), new JComboBox<>(hours),
                    new JLabel(":"), new JComboBox<>(minutes), new JLabel(" bis "), new JComboBox<>(hours),
                    new JLabel(":"), new JComboBox<>(minutes));
        JButton save = new JButton("Speichern");
        save.addActionListener(e -> savePeriods());
        panels[16] = addMultiple(new JPanel(), closeButton(frame, null), save);
        return panels;
    }

    static JButton closeButton(JFrame frame, String text) {
        JButton b = new JButton(text == null ? "Abbrechen" : text);
        b.addActionListener(e -> frame.dispose());
        return b;
    }

    private static void savePeriods() {

    }
}
