import java.io.Serializable;
import java.time.*;

public class PeriodRange implements Serializable {

    private static final long serialVersionUID = 6L;

    private Period from;
    private Period to;

    public PeriodRange(Period from, Period to) {
        this.from = from;
        this.to = to;
    }

    public Period getFrom() {
        return from;
    }

    public void setFrom(Period from) {
        this.from = from;
    }

    public Period getTo() {
        return to;
    }

    public void setTo(Period to) {
        this.to = to;
    }

    public int getDuration() {
        return (int)Duration.between(from.getBegin(), to.getEnd()).toMinutes();
    }
}
