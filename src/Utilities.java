import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {

    private static final long serialVersionUID = 1L;
    static final int panewidth = 350;
    static final int paneheight = 450;

    static ArrayList<Lehrer> teachers = new ArrayList<>();
    static ArrayList<Klasse> classes= new ArrayList<>();


    static <T> void updateList(ArrayList<T> data, JList<T> list) {
        DefaultListModel<T> model = new DefaultListModel<T>();
        data.forEach(model::addElement);
        list.setModel(model);
    }

    static void alert(String text, ActionListener listener, String title) {
        JFrame frame = new JFrame(title == null ? "Error!" : title);
        frame.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(new JLabel(text));
        JButton button = new JButton("OK");
        panel2.add(button);
        button.addActionListener(listener == null ? e -> frame.dispose() : listener);
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
}
