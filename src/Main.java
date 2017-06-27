import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private static final long serialVersionUID = 2L;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        super("Stundenplan");
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());

        JPanel topLeftPan = new JPanel(new BorderLayout());
        //{
        JPanel 
        JButton addTeacher = new JButton("Neue Lehrkraft");
        JButton addClass = new JButton("Neue Klasse");
        //}

        JPanel mainPan = new JPanel();
        //{
        JPanel teacherPan = new JPanel();
            //{
        JList<String> teacherList = new JList<>();
        JScrollPane teacherPane = new JScrollPane(teacherList);
        teacherPane.setPreferredSize(new Dimension(Utilities.panewidth, Utilities.paneheight));
        teacherPan.add(teacherPane);
            //}
        JPanel classPan = new JPanel();
            //{
        JList<String> classList = new JList<>();
        JScrollPane classPane = new JScrollPane(classList);
        classPane.setPreferredSize(new Dimension(Utilities.panewidth, Utilities.paneheight));
        classPan.add(classPane);
            //}
        //}

        JPanel bottomPan = new JPanel();
        //{
        JButton generate = new JButton("Stundenplan generieren");
        //}

        topPan.add(addTeacher, FlowLayout.LEFT);
        topPan.add(addClass, FlowLayout.CENTER);

        mainPan.add(teacherPan);
        mainPan.add(classPan);

        bottomPan.add(generate);

        this.add(topPan, BorderLayout.NORTH);
        this.add(mainPan, BorderLayout.CENTER);
        this.add(bottomPan, BorderLayout.SOUTH);


        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
