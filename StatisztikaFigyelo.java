package pomodoro.model;

/**
 * An observer that watches the state changes of the model.
 * A pomodoro counts as finished when a WORK phase ends and a BREAK starts.
 * (Pausing goes from MUNKA to SZUNETEL, so a paused or reset round is not counted.)
 */
public class StatisztikaFigyelo implements PomodoroFigyelo {

    private final NapiStatisztika statisztika;
    private Allapot elozoAllapot = Allapot.SZUNETEL;

    public StatisztikaFigyelo(NapiStatisztika statisztika) {
        this.statisztika = statisztika;
    }

    @Override
    public void tikk(int hatralevoMasodperc) {
        // This observer does not care about single seconds
    }

    @Override
    public void allapotValtozott(Allapot uj) {
        boolean munkaVegeVoltEppen = (elozoAllapot == Allapot.MUNKA)
                && (uj == Allapot.ROVID_SZUNET || uj == Allapot.HOSSZU_SZUNET);

        if (munkaVegeVoltEppen) {
            statisztika.pomodoroKeszult();
        }
        elozoAllapot = uj;
    }
}
