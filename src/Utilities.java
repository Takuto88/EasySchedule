import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
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
    static ArrayList<Elective> wahlpflicht = new ArrayList<>();
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
            wahlpflicht = ((ArrayList<Elective>)objects.get(4));
            gradeLevels = ((ArrayList<String>) objects.get(5));
            in.close();
        } catch (IOException | ClassNotFoundException | IndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }
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

    static JButton closeButton(JFrame frame, String text) {
        JButton b = new JButton(text == null ? "Abbrechen" : text);
        b.addActionListener(e -> frame.dispose());
        return b;
    }


    static void savePeriods(JPanel panel) {
        ArrayList<Period> list = new ArrayList<>();
        boolean error = false;
        Component[] panels = panel.getComponents();
        panels[panels.length-1] = null;
        panels = Arrays.stream(panels).filter(Objects::nonNull).toArray(Component[]::new);
        for (Component e : panels) {
            Component[] c = ((JPanel) e).getComponents();
            String text = ((JLabel) c[0]).getText();
            text = text.split(" ")[0];
            text = text.substring(0, text.length()-1);
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

    static Elective getDuration(JComboBox<String> box, JTextField field, JPanel panel) {
        ArrayList<JPanel> componentarray = Arrays.stream(panel.getComponents()).map(e -> (JPanel)e).collect(Collectors.toCollection(ArrayList::new));
        componentarray.removeAll(componentarray.subList(0, 2));
        componentarray.remove(componentarray.size()-1);

        DayOfWeek[] days = new DayOfWeek[componentarray.size()];
        PeriodRange[] ranges = new PeriodRange[componentarray.size()];

        for (int i = 0; i < componentarray.size(); i++) {
            ArrayList<JComboBox<String>> list = Arrays.stream(componentarray.get(i).
                    getComponents()).filter(e -> e instanceof JComboBox).map(e -> (JComboBox<String>)e).
                    collect(Collectors.toCollection(ArrayList::new));
            if (checkElectives(list)) {
                DayOfWeek day = dayToNumber(list.get(0).getSelectedItem().toString());
                String from = list.get(1).getSelectedItem().toString().split(" ")[0];
                int fromInt = Integer.parseInt(from.substring(0, from.length() - 1));
                String to = list.get(2).getSelectedItem().toString().split(" ")[0];
                int toInt = Integer.parseInt(to.substring(0, to.length() - 1));
                days[i] = day;
                ranges[i] = new PeriodRange(periods.get(fromInt), periods.get(toInt));
            }
        }
        return new Elective(box.getSelectedItem().toString(), field.getText(), days, ranges);
    }

    private static boolean checkElectives(ArrayList<JComboBox<String>> list) {
        for(JComboBox<String> combo: list)
            if (combo.getSelectedItem().toString().equals("-")) return false;
        return true;
    }

    static String[] getNumberOfPeriods() {
        String[] array = new String[Utilities.periods.size() + 1];
        array[0] = "-";
        for (int i = 0; i < Utilities.periods.size(); i++)
            array[i + 1] = i + ". Stunde";
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

    private static DayOfWeek dayToNumber(String day) {
        switch (day) {
            case "Montag": return DayOfWeek.MONDAY;
            case "Dienstag": return DayOfWeek.TUESDAY;
            case "Mittwoch": return DayOfWeek.WEDNESDAY;
            case "Donnerstag": return DayOfWeek.THURSDAY;
            case "Freitag": return DayOfWeek.FRIDAY;
            default: return DayOfWeek.SUNDAY;
        }
    }
}
