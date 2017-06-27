import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        JPanel buttonPan1 = new JPanel();
            //{
        JButton addTeacher = new JButton("Neue Lehrkraft");
        buttonPan1.add(addTeacher);
            //}
        JPanel panePan1 = new JPanel();
            //{
        JList<String> teacherList = new JList<>();
        JScrollPane teacherPane = new JScrollPane(teacherList);
        teacherPane.setPreferredSize(new Dimension(Utilities.panewidth, Utilities.paneheight));
        panePan1.add(teacherPane);
            //}
        //}
        topLeftPan.add(buttonPan1, BorderLayout.NORTH);
        topLeftPan.add(panePan1, BorderLayout.SOUTH);

        JPanel topRightPan = new JPanel(new BorderLayout());
        //{
        JPanel buttonPan2 = new JPanel();
            //{
        JButton addClass = new JButton("Neue Klasse");
        buttonPan2.add(addClass);
            //}
        JPanel panePan2 = new JPanel();
            //{
        JList<String> classList = new JList<>();
        JScrollPane classPane = new JScrollPane(classList);
        classPane.setPreferredSize(new Dimension(Utilities.panewidth, Utilities.paneheight));
        panePan2.add(classPane);
            //}
        //}
        topRightPan.add(buttonPan2, BorderLayout.NORTH);
        topRightPan.add(panePan2, BorderLayout.SOUTH);


        JPanel bottomPan = new JPanel();
        //{
        JButton generate = new JButton("Stundenplan generieren");
        bottomPan.add(generate);
        bottomPan.setBorder(new EmptyBorder(0, 0, 20, 0));
        //}

        JPanel mainP = new JPanel();
        mainP.add(topLeftPan);
        mainP.add(topRightPan);

        this.add(mainP, BorderLayout.NORTH);
        this.add(bottomPan, BorderLayout.SOUTH);


        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
