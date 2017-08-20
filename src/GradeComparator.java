import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradeComparator implements Comparator<String> {

    @Override
    public int compare(String grade1, String grade2) {
        isValid(grade1);
        isValid(grade2);

        // Einfacher fall: Wenn die Strings gleich sind, dann geben wir 0 zurück. Weitere Checks brauchen wir nicht.
        if (grade1.equalsIgnoreCase(grade2)) {
            return 0;
        }

        int grade1number = getGradeNumber(grade1);
        int grade2number = getGradeNumber(grade2);
        String grade1letter = getGradeLetter(grade1);
        String grade2letter = getGradeLetter(grade2);

        // Die zahl hat priorität. Die vergleichen wir zuerst. Ist eine größer oder kleiner als die andere, ist der
        // Buchstabe schlicht egal.
        if (grade1number > grade2number) {
            return 1;
        } else if (grade1number < grade2number) {
            return -1;
        }

        // Wenn wir bis hier hin gekommen sind, dann ist die Klassenzahl gleich. Jetzt wird der Buchstabe interessant:
        char[] grade1array = grade1letter.toCharArray();
        char[] grade2array = grade2letter.toCharArray();
        int length = grade1array.length > grade2array.length ? grade2array.length : grade1array.length;
        for(int i = 0; i < length; i++) {
            if (grade1array[i] > grade1array[i]) return 1;
            else if (grade1array[i] > grade1array[i]) return -1;

            if (length == i-1) return 1;
        }

        // Wenn wir bis hier hin gekommen sind, dann sind die Buchstaben identisch.
        return 0;
    }

    /**
     * Stellt sicher, dass der String auch wirklich etwas ist, dass wir vergleichen können
     *
     * @param grade Klassen string zum prüfen.
     */
    private void isValid(String grade) {
        if (grade == null)
            throw new NullPointerException("Klasse ist null.");

        /*
         * Regulärer Ausdruck. Der String muss anfangen (^) mit zahlen zwischen 0-9 ([0-9]), davon können beliebig
         * viele kommen (*). Der String endet ($) mit einem buchstaben zwischen klein a und groß Z ([a-zA-Z]).
         * Das ? zeigt an, dess der Buchstabe da sein kann, aber nicht muss. "5" soll ja auch gültig sein.
         */
        if (!grade.matches("^[0-9]*[a-zA-Z]*?$"))
            throw new IllegalArgumentException("Klasse muss folgendes Format haben: 9, oder z.B. 9b oder 9B und nicht" + grade);
    }

    /**
     * Extrahiert die klassenzahl als integer aus unserem String. Beispiel: String "5a" wird zu integer 5
     *
     * @param grade Die grade als string.
     * @return Die klassenzahl als integer
     */
    private int getGradeNumber(String grade) {
        int gradeNumber = 0;

        /*
         * Auch hier sind wieder reguläre Ausdrücke am start, aber zusätzlich sogenannte gruppen enthalten.
         * Das sind die Gebilde in runden klammern. Folgendes: ([0-9]*) ist die erste Gruppe und wird die Zahl
         * beeinhalten. Gruppe zwei ([a-zA-Z]?) enthält, sofern vorhanden, den Buchstaben.
         */
        Pattern pattern = Pattern.compile("^([0-9]*)([a-zAZ]*?)");
        Matcher matcher = pattern.matcher(grade);
        if (matcher.matches()) {
            gradeNumber = Integer.parseInt(matcher.group(1)); // Erste gruppe (unsere Zahl) in einen Integer umwandeln
        }

        return gradeNumber;
    }

    /**
     * Holt den buchstaben als "char" raus. Also von z.B. 5c das 'c'
     * @param grade Die grade als string
     * @return Den buchstaben. Standart ist '!' wenn keiner gefunden wurde.
     */
    private String getGradeLetter(String grade) {
        String letter = "!"; // Defaultwert, wenn keiner da ist. '!' < 'a' gibt true zurück.
        Pattern pattern = Pattern.compile("^([0-9]*)([a-zAZ]*?)");
        Matcher matcher = pattern.matcher(grade);
        if (matcher.matches() && !matcher.group(2).isEmpty()) {
            // Zweite gruppe, den Buchstaben rausholen, nomalisieren (= nur kleinbuchstaben) und in char umwandeln
            letter = matcher.group(2).toLowerCase();
        }

        return letter;
    }

}
