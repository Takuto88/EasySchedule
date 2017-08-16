import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
@SuppressWarnings("unchecked")
class Utilities implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Dimension panelSize = new Dimension(350, 450);
    private static final String path = System.getProperty("user.home") + "/Desktop/stundenplan.ser";
    private static final Font defaultFont = new Font("Roboto", Font.PLAIN, 14);
    static final String[] hours = {"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"};
    static final String[] minutes = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    static final String[] weekdays = {"-", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    static final String[] periodsList = {"-", "1. Stunde", "2. Stunde","3. Stunde", "4. Stunde",
            "5. Stunde", "6. Stunde", "7. Stunde", "8. Stunde", "9. Stunde", "10. Stunde",
            "11. Stunde", "12. Stunde", "13. Stunde", "14. Stunde", "15. Stunde"};
    static final Comparator<String> numberComparator = (e, k) -> {
        try {
            int a = Integer.parseInt(e);
            int b = Integer.parseInt(k);
            return a - b;
        } catch (NumberFormatException x) {
            return e.compareTo(k);
        }
    };

    static ArrayList<Period> periods = new ArrayList<>();
    static ArrayList<String> subjects = new ArrayList<>();
    static ArrayList<HashMap<String, String>> wahlpflicht = new ArrayList<>();
    static ArrayList<String> gradeLevels = new ArrayList<>();

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
            objects.add(teachers);
            objects.add(classes);
            objects.add(subjects);
            objects.add(periods);
            objects.add(wahlpflicht);
            objects.add(gradeLevels);
            out.writeObject(objects);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void load() {
        if (new File(path).exists()) try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            ArrayList<ArrayList> objects = ((ArrayList<ArrayList>) in.readObject());
            teachers = (ArrayList<Teacher>) objects.get(0);
            classes = ((ArrayList<Grade>) objects.get(1));
            subjects = ((ArrayList<String>)objects.get(2));
            periods = ((ArrayList<Period>) objects.get(3));
            wahlpflicht = ((ArrayList<HashMap<String, String>>)objects.get(4));
            gradeLevels = ((ArrayList<String>) objects.get(5));
            in.close();
        } catch (IOException | ClassNotFoundException | IndexOutOfBoundsException | NumberFormatException e) {
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
        //HashMap<String, LocalTime> map = new HashMap<>();
        ArrayList<Period> list = new ArrayList<>();
        boolean error = false;
        Component[] panels = panel.getComponents();
        panels[panels.length-1] = null;
        panels = Arrays.stream(panels).filter(Objects::nonNull).toArray(Component[]::new);
        for (Component e : panels) {
            Component[] c = ((JPanel) e).getComponents();
            String text = ((JLabel) c[0]).getText();
            text = text.split(" ")[0];
            text = text.substring(text.length()-2);
            int textAsNumber = Integer.parseInt(text);
            LocalTime begin = LocalTime.parse(((JComboBox<String>) c[1]).getSelectedItem() + ":" + ((JComboBox<String>) c[3]).getSelectedItem());
            LocalTime end = LocalTime.parse(((JComboBox<String>) c[5]).getSelectedItem() + ":" + ((JComboBox<String>) c[7]).getSelectedItem());
            if (begin.isAfter(end)) {
                alert("Der Stundenanfang kann nicht nach Stundenende liegen.", null, null);
                error = true;
            }
            list.add(new Period(textAsNumber, begin, end));
        }
        if (!error) periods = list;
    }

    static String formatTime(Integer time) {
        if (Integer.toString(time).length() == 1)
            return "0" + time;
        return Integer.toString(time);
    }

    static <T> void defaultList(JList<T> list) {
        ((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        list.setFont(defaultFont);
    }

    static boolean readyForNewSubject(JPanel mainPanel) {
        ArrayList<Component> list = Arrays.stream(mainPanel.getComponents()).collect(Collectors.toCollection(ArrayList::new));
        JPanel panel = (JPanel)list.get(list.size()-2);
        ArrayList<String> resultList = Arrays.stream(panel.getComponents()).filter(e -> e instanceof JComboBox).map(e -> (JComboBox<String>)e).
                map(JComboBox::getSelectedItem).map(Object::toString).filter(e -> e.equals("-")).
                collect(Collectors.toCollection(ArrayList::new));
        return resultList.size() == 0;
    }

    static HashMap<String, String> getDuration(JPanel panel) {
        ArrayList<JPanel> componentarray = Arrays.stream(panel.getComponents()).map(e -> (JPanel)e).collect(Collectors.toCollection(ArrayList::new));
        componentarray.removeAll(componentarray.subList(0, 2));
        componentarray.remove(componentarray.size()-1);
        HashMap<String, String> map = new HashMap<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> froms = new ArrayList<>();
        ArrayList<String> tos = new ArrayList<>();
        for (JPanel p: componentarray) {
            ArrayList<JComboBox<String>> list = Arrays.stream(p.getComponents()).filter(e -> e instanceof JComboBox).map(e -> (JComboBox<String>)e).collect(Collectors.toCollection(ArrayList::new));
            String day = list.get(0).getSelectedItem().toString();
            String from = list.get(1).getSelectedItem().toString();
            String to = list.get(2).getSelectedItem().toString();
            if (!day.equals("-") && !from.equals("-") && !to.equals("-")) {
                days.add(day);
                from = from.replace(" Stunde", "");
                froms.add(from);
                tos.add(to);
            }
        }
        map.put("days", days.stream().collect(Collectors.joining(", ")));
        map.put("froms", froms.stream().collect(Collectors.joining(", ")));
        map.put("tos", tos.stream().collect(Collectors.joining(", ")));
        return map;
    }

    static String[] getNumberOfPeriods() {
        String[] array = new String[Utilities.periods.size()/2];
        for (int i = 0; i < Utilities.periods.size()/2; i++)
            array[i] = i + ". Stunde";
        return array;
    }

    static <T> void enableAdvancedSelection(JComboBox<T> identifier, JComboBox<T> box, T[] data) {
        int index = identifier.getSelectedIndex();
        Object obj = box.getSelectedItem();
        T[] values = Arrays.copyOfRange(data, index, data.length);
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        Arrays.stream(values).forEach(model::addElement);
        box.setModel(model);
        box.setSelectedItem(obj);
    }
}
