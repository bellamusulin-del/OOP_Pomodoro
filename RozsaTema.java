package pomodoro.view;

import java.awt.Color;
import java.awt.Font;

/** All the "girly" colours and fonts in one place, so they are easy to change. */
public final class RozsaTema {

    public static final Color FORRO_ROZSA = Color.decode("#FF1493");      // hot pink
    public static final Color MELY_ROZSA_SZOVEG = Color.decode("#D6006F"); // text on the white buttons
    public static final Color VILAGOS_ROZSA = Color.decode("#FFD1E8");     // pastel background
    public static final Color GOMB_FELETTE = Color.decode("#FFE4F1");      // mouse over a button
    public static final Color GOMB_LENYOMVA = Color.decode("#FFC2E0");     // button pressed

    // "Comic Sans MS" is a round, cute font; Java uses a default font if it is missing
    public static final String BETUTIPUS = "Comic Sans MS";

    public static Font betu(int stilus, int meret) {
        return new Font(BETUTIPUS, stilus, meret);
    }

    private RozsaTema() {
        // utility class, nobody should create it
    }
}
