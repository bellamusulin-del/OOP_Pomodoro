package pomodoro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The model of the Pomodoro clock (the "M" in MVC).
 * It stores the state, the remaining seconds and the finished rounds.
 * It does NOT import anything from javax.swing, and it has no timer:
 * somebody else has to call tick() once every second.
 */
public class PomodoroModel {

    private static final int MASODPERC_PERCENKENT = 60;
    private static final int KOROK_A_HOSSZU_SZUNETIG = 4;

    // The observers that want to be informed about changes (never given out directly)
    private final List<PomodoroFigyelo> figyelok = new ArrayList<>();

    private Allapot jelenlegiAllapot;
    // When the clock is paused, we remember here what to continue after pressing Start
    private Allapot folytatandoAllapot;
    private int hatralevoMasodperc;
    private int befejezettKorok;

    // Configurable lengths in minutes
    private int munkaPercek = 25;
    private int rovidSzunetPercek = 5;
    private int hosszuSzunetPercek = 15;

    public PomodoroModel() {
        alapAllapotBeallitasa();
    }

    // ---------- Observer handling ----------

    /** Adds a new observer to the list. */
    public void feliratkoz(PomodoroFigyelo ujFigyelo) {
        figyelok.add(ujFigyelo);
    }

    /** Tells every observer that one second has passed. */
    private void ertesitTikkelesrol() {
        // We loop over a copy, so an observer can safely subscribe while we notify
        List<PomodoroFigyelo> figyelokMasolata = new ArrayList<>(figyelok);
        for (PomodoroFigyelo figyelo : figyelokMasolata) {
            figyelo.tikk(hatralevoMasodperc);
        }
    }

    /** Tells every observer that the state has changed. */
    private void ertesitAllapotvaltozasrol() {
        List<PomodoroFigyelo> figyelokMasolata = new ArrayList<>(figyelok);
        for (PomodoroFigyelo figyelo : figyelokMasolata) {
            figyelo.allapotValtozott(jelenlegiAllapot);
        }
    }

    // ---------- Actions that the controller can call ----------

    /** Starts the clock, or continues it after a pause. */
    public void start() {
        if (jelenlegiAllapot != Allapot.SZUNETEL) {
            return; // already running, nothing to do
        }
        jelenlegiAllapot = folytatandoAllapot;
        ertesitAllapotvaltozasrol();
    }

    /** Pauses the clock and remembers what we were doing. */
    public void szunet() {
        if (jelenlegiAllapot == Allapot.SZUNETEL) {
            return; // already paused
        }
        folytatandoAllapot = jelenlegiAllapot;
        jelenlegiAllapot = Allapot.SZUNETEL;
        ertesitAllapotvaltozasrol();
    }

    /** Puts everything back to the beginning: paused, full work time, zero rounds. */
    public void reset() {
        alapAllapotBeallitasa();
        ertesitAllapotvaltozasrol();
        ertesitTikkelesrol();
    }

    /** One second has passed. Does nothing while the clock is paused. */
    public void tick() {
        if (jelenlegiAllapot == Allapot.SZUNETEL) {
            return;
        }

        hatralevoMasodperc = hatralevoMasodperc - 1;
        ertesitTikkelesrol();

        if (hatralevoMasodperc <= 0) {
            fazisVegeElerkezett();
        }
    }

    /** Saves new lengths (in minutes) and resets the clock. */
    public void beallitIdok(int ujMunkaPercek, int ujRovidSzunetPercek, int ujHosszuSzunetPercek) {
        if (ujMunkaPercek <= 0 || ujRovidSzunetPercek <= 0 || ujHosszuSzunetPercek <= 0) {
            throw new IllegalArgumentException("Every length must be at least 1 minute");
        }
        munkaPercek = ujMunkaPercek;
        rovidSzunetPercek = ujRovidSzunetPercek;
        hosszuSzunetPercek = ujHosszuSzunetPercek;
        reset();
    }

    // ---------- Private helper methods ----------

    private void alapAllapotBeallitasa() {
        jelenlegiAllapot = Allapot.SZUNETEL;
        folytatandoAllapot = Allapot.MUNKA;
        hatralevoMasodperc = idoMasodpercben(Allapot.MUNKA);
        befejezettKorok = 0;
    }

    /** Decides what comes after the phase that just ended. */
    private void fazisVegeElerkezett() {
        if (jelenlegiAllapot == Allapot.MUNKA) {
            // A work round is finished
            befejezettKorok = befejezettKorok + 1;

            boolean hosszuSzunetJon = (befejezettKorok % KOROK_A_HOSSZU_SZUNETIG == 0);
            if (hosszuSzunetJon) {
                atvaltAllapotra(Allapot.HOSSZU_SZUNET);
            } else {
                atvaltAllapotra(Allapot.ROVID_SZUNET);
            }
        } else {
            // A break is over, back to work
            atvaltAllapotra(Allapot.MUNKA);
        }
    }

    private void atvaltAllapotra(Allapot ujAllapot) {
        jelenlegiAllapot = ujAllapot;
        hatralevoMasodperc = idoMasodpercben(ujAllapot);
        ertesitAllapotvaltozasrol();
        ertesitTikkelesrol();
    }

    private int idoMasodpercben(Allapot allapot) {
        switch (allapot) {
            case ROVID_SZUNET:
                return rovidSzunetPercek * MASODPERC_PERCENKENT;
            case HOSSZU_SZUNET:
                return hosszuSzunetPercek * MASODPERC_PERCENKENT;
            default:
                return munkaPercek * MASODPERC_PERCENKENT;
        }
    }

    // ---------- Getters (only simple values, never the observer list) ----------

    public Allapot getAllapot() {
        return jelenlegiAllapot;
    }

    public int getHatralevoMasodperc() {
        return hatralevoMasodperc;
    }

    public int getBefejezettKorok() {
        return befejezettKorok;
    }

    public int getMunkaPercek() {
        return munkaPercek;
    }

    public int getRovidSzunetPercek() {
        return rovidSzunetPercek;
    }

    public int getHosszuSzunetPercek() {
        return hosszuSzunetPercek;
    }
}
