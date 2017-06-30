import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Utilities implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Dimension panelSize = new Dimension(350, 450);
    private static final String path = System.getProperty("user.home") + "/Desktop/stundenplan.ser";
    private boolean isConfigured = false;

    static ArrayList<Teacher> teachers = new ArrayList<>();
    static ArrayList<Grade> classes= new ArrayList<>();

    static void updateList(ArrayList<?> data, JList<String> list, JScrollPane pane) {
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
        JButton cancel = new JButton("Abbrechen");
        panel2.add(cancel);
        panel2.add(button);
        ActionListener close = e -> frame.dispose();
        button.addActionListener(listener == null ? close : listener);
        button.addActionListener(close);
        cancel.addActionListener(close);
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
        if (number instanceof Double)
            return Double.parseDouble(format.format(number));
        else return Integer.parseInt(format.format(number));
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

    static Container addMultiple(Container con, JComponent... components) {
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
}
