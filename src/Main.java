import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class Main extends JFrame {

    private static final long serialVersionUID = 2L;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        super("Stundenplan");
        Utilities.load();
        this.setLayout(new BorderLayout());

        JPanel topLeftPan = new JPanel(new BorderLayout());
        //{
        JPanel buttonPan1 = new JPanel();
            //{
        JButton rmTeacher = new JButton("Lehrkraft l\u00f6schen");
        JButton addTeacher = new JButton("Neue Lehrkraft");
        buttonPan1.add(rmTeacher);
        buttonPan1.add(addTeacher);
            //}
        JList<String> teacherList = new JList<>(new String[]{"Oertel, Lisa", "Zimmermann, Andreas"});
        JScrollPane teacherPane = new JScrollPane(teacherList);
        //}
        topLeftPan.add(buttonPan1, BorderLayout.NORTH);
        topLeftPan.add(teacherPane, BorderLayout.CENTER);

        JPanel topRightPan = new JPanel(new BorderLayout());
        //{
        JPanel buttonPan2 = new JPanel();
            //{
        JButton rmClass = new JButton("Klasse l\u00f6schen");
        JButton addClass = new JButton("Neue Klasse");
        buttonPan2.add(rmClass);
        buttonPan2.add(addClass);
            //}

        JList<String> classList = new JList<>(new String[]{"5. Klasse", "6. Klasse", "7. Klasse", "8. Klasse", "9. Klasse", "10. Klasse", "11. Klasse", "12. Klasse"});
        JScrollPane classPane = new JScrollPane(classList);
        //}
        topRightPan.add(buttonPan2, BorderLayout.NORTH);
        topRightPan.add(classPane, BorderLayout.CENTER);


        JPanel bottomPan = new JPanel();
        //{
        JButton generate = new JButton("Stundenplan generieren");
        bottomPan.add(generate);
        bottomPan.setBorder(new EmptyBorder(0, 0, 20, 0));
        //}


        JPanel mainP = new JPanel();
        mainP.add(topLeftPan);
        mainP.add(topRightPan);

        //ActionListener
        addTeacher.addActionListener(e -> new TeacherGUI(false, null));
        rmTeacher.addActionListener(delete(teacherList, true));

        addClass.addActionListener(e -> new GradeGUI(false, null));
        rmClass.addActionListener(delete(classList, false));

        this.add(mainP, BorderLayout.CENTER);
        this.add(bottomPan, BorderLayout.SOUTH);

        Utilities.setDefault(generate);
        Utilities.defaultList(teacherList);
        Utilities.defaultList(classList);
        //Utilities.updateList(Utilities.teachers, teacherList);
        //Utilities.updateList(Utilities.classes, classList);

        teacherPane.setPreferredSize(Utilities.panelSize);
        classPane.setPreferredSize(Utilities.panelSize);

        //MenuBar
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("EasySchedule");
        JMenuItem item = new JMenuItem("Einstellungen");
        item.addActionListener(e -> new Settings());
        menu.add(item);
        bar.add(menu);
        this.setJMenuBar(bar);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Utilities.save();
            }
        });
    }

    private ActionListener delete(JList<String> list, boolean teacher) {
        return e -> {
            int index = list.getSelectedIndex();
            String name = list.getSelectedValue();
            if (index != -1) Utilities.alert("M\u00f6chten Sie \"" + name + "\" wirklich l\u00f6schen?",
                    e2 -> (teacher ? Utilities.teachers : Utilities.classes).remove(index), (teacher ? "Lehrkraft" : "Klasse") + " l\u00f6schen?");
        };
    }
}
