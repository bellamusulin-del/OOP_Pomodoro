package pomodoro.model;

/**
 * The possible states of the Pomodoro clock.
 * Each state knows its display name and its background colour as a hex code.
 * The colour is stored as plain text, so the model does not need java.awt or javax.swing.
 */
public enum Allapot {

    MUNKA("Munka", "#FF1493"),
    ROVID_SZUNET("Rövid szünet", "#C77DFF"),
    HOSSZU_SZUNET("Hosszú szünet", "#5FB3F0"),
    SZUNETEL("Szünetel", "#F06FAE");

    private final String megjelenitettNev;
    private final String szinKod;

    Allapot(String megjelenitettNev, String szinKod) {
        this.megjelenitettNev = megjelenitettNev;
        this.szinKod = szinKod;
    }

    /** Text that the view shows to the user. */
    public String getMegjelenitettNev() {
        return megjelenitettNev;
    }

    /** Hex colour code like "#FF1493"; the view converts it to a real colour. */
    public String getSzinKod() {
        return szinKod;
    }
}
