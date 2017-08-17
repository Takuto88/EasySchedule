import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class Elective {

    private String grade;
    private String name;
    private DayOfWeek[] days;
    private PeriodRange[] range;

    public Elective(String grade, String name, DayOfWeek[] days, PeriodRange[] range) {
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

    public DayOfWeek[] getDays() {
        return days;
    }

    public void setDays(DayOfWeek[] days) {
        this.days = days;
    }

    public PeriodRange[] getRange() {
        return range;
    }

    public void setRange(PeriodRange[] range) {
        this.range = range;
    }

    public boolean isIncomplete() {
        return (grade.length() == 0 || name.length() == 0 || days.length == 0 || range.length == 0);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < days.length; i++)
            b.append(days[i].getDisplayName(TextStyle.FULL, Locale.GERMAN)).append(", ").append(range[i].getFrom()).append(" - ").append(range[i].getTo()).append("; ");
        return name + ", Stufe " + grade + ": " + b.toString();
    }
}
