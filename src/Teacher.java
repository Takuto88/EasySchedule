import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Teacher extends Main implements Serializable {

    private static final long serialVersionUID = 3L;

    private String firstname;
    private String lastname;
    private ArrayList<Integer> teachesIn;
    private ArrayList<String> teachesSubject ;
    private ArrayList<LocalDate> notAvailable;

    public Teacher(String firstname, String lastname, ArrayList<Integer> teachesIn,
                   ArrayList<String> teachesSubject, ArrayList<LocalDate> notAvailable) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.teachesIn = teachesIn;
        this.teachesSubject = teachesSubject;
        this.notAvailable = notAvailable;
    }
}
