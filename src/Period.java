import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public class Period implements Serializable {

    private static final long serialVersionUID = 5L;

    public static final Period DEFAULT = new Period(-1, LocalTime.MIN, LocalTime.MAX);

    private int periodNumber;
    private LocalTime begin;
    private LocalTime end;

    public Period(int periodNumber, LocalTime begin, LocalTime end) {
        if (begin.isAfter(end)) {
            throw new IllegalArgumentException("Beginning of period can not be after end of period.");
        }
        this.periodNumber = periodNumber;
        this.begin = begin;
        this.end = end;

    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public void setBegin(LocalTime begin) {
        this.begin = begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public int getDuration() {
        return (int)Duration.between(begin, end).toMinutes();
    }

    public boolean isDefaultPeriod() {
        return periodNumber == -1;
    }

    @Override
    public String toString() {
        return isDefaultPeriod() ? "-" : periodNumber + ". Stunde";
    }
}
