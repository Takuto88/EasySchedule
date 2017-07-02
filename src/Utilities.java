import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

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
    static final String[] hours = {"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};
    static final String[] minutes = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    static HashMap<String, LocalTime> periods = new HashMap<>();
    static ArrayList<String> subjects = new ArrayList<>();

    static ArrayList<Teacher> teachers = new ArrayList<>();
    static ArrayList<Grade> classes= new ArrayList<>();

    static void updateList(ArrayList<?> data, JList<String> list) {
        DefaultListModel<String> model = new DefaultListModel<>();
        data.forEach(e -> model.addElement(e.toString()));
        list.setModel(model);
    }

    static void alert(@NotNull String text, @Nullable ActionListener listener, @Nullable String title) {
        JFrame frame = new JFrame(title == null ? "Error!" : title);
        frame.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(new JLabel(text));
        JButton button = new JButton("OK");
        if (listener != null)
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
            ArrayList<ArrayList> objects = new ArrayList<>();
            ArrayList<HashMap<String, LocalTime>> mapList = new ArrayList<HashMap<String, LocalTime>>() {{add(periods);}};
            objects.add(teachers);
            objects.add(classes);
            objects.add(subjects);
            objects.add(mapList);
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
            ArrayList<ArrayList> objects = ((ArrayList<ArrayList>) in.readObject());
            teachers = (ArrayList<Teacher>) objects.get(0);
            classes = ((ArrayList<Grade>) objects.get(1));
            subjects = ((ArrayList<String>)objects.get(2));
            periods = ((ArrayList<HashMap<String, LocalTime>>)objects.get(3)).get(0);
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

    static JComponent add(JComponent con, JComponent... components) {
        Arrays.stream(components).forEach(con::add);
        return con;
    }

    static JPanel newBoxLayout() {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        return pan;
    }

    static String getUserInput(String prompt) {
        return JOptionPane.showInputDialog(prompt, JOptionPane.INFORMATION_MESSAGE);
    }

    static JButton closeButton(JFrame frame, String text) {
        JButton b = new JButton(text == null ? "Abbrechen" : text);
        b.addActionListener(e -> frame.dispose());
        return b;
    }

    static void savePeriods(JPanel panel) {
        HashMap<String, LocalTime> map = new HashMap<>();
        boolean error = false;
        for (Component e : panel.getComponents()) {
            Component[] c = ((JPanel) e).getComponents();
            String text = ((JLabel) c[0]).getText();
            text = text.charAt(0) == '1' && text.charAt(1) != '.' ? text.substring(0, 2) : text.substring(0, 1);
            LocalTime begin = LocalTime.parse(((JComboBox<String>) c[1]).getSelectedItem() + ":" + ((JComboBox<String>) c[3]).getSelectedItem());
            LocalTime end = LocalTime.parse(((JComboBox<String>) c[5]).getSelectedItem() + ":" + ((JComboBox<String>) c[7]).getSelectedItem());
            if (begin.isAfter(end)) {
                alert("Der Stundenanfang kann nicht nach Stundenende liegen.", null, null);
                error = true;
            }
            map.put(text + "begin", begin);
            map.put(text + "end", end);
        }
        if (!error) periods = map;
    }

    static String formatTime(Integer time) {
        if (Integer.toString(time).length() == 1)
            return "0" + time;
        return Integer.toString(time);
    }
}
