import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class Elective implements Serializable {

    private static final long serialVersionUID = 7L;

    private String grade;
    private String name;
    private ArrayList<DayOfWeek> days;
    private ArrayList<PeriodRange> range;

    public Elective(String grade, String name, ArrayList<DayOfWeek> days, ArrayList<PeriodRange> range) {
        this.grade = grade;
        this.name = name;
        this.days = days;
        this.range = range;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayOfWeek> days) {
        this.days = days;
    }

    public ArrayList<PeriodRange> getRange() {
        return range;
    }

    public void setRange(ArrayList<PeriodRange> range) {
        this.range = range;
    }

    public boolean isIncomplete() {
        return (grade.length() == 0 || name.length() == 0 || days.size() == 0 || range.size() == 0);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < days.size(); i++)
            if (range.get(i).isSinglePeriod()) {
                b.append(days.get(i).getDisplayName(TextStyle.FULL, Locale.GERMAN)).append(", ").
                        append(range.get(i).getTo()).append("; ");
            } else {
                b.append(days.get(i).getDisplayName(TextStyle.FULL, Locale.GERMAN)).
                        append(", ").
                        append(range.get(i).getFrom().toString().split(" ")[0]).
                        append(" - ").
                        append(range.get(i).getTo()).
                        append("; ");
            }
        return name + ", Klassenstufe " + grade + ": " + b.toString();
    }
}
