import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Lehrer extends Main implements Serializable {

    private static final long serialVersionUID = 3L;

    private String firstname;
    private String lastname;
    private ArrayList<Integer> teachesIn;
    private ArrayList<String> teaches;
    private ArrayList<LocalDate> notAvailable;

    public Lehrer(String firstname, String lastname, ArrayList<Integer> teachesIn,
                  ArrayList<String> teaches, ArrayList<LocalDate> notAvailable) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.teachesIn = teachesIn;
        this.teaches = teaches;
        this.notAvailable = notAvailable;
    }
}
