package pomodoro.view;

import java.awt.Toolkit;
import pomodoro.model.Allapot;
import pomodoro.model.PomodoroFigyelo;

/** An observer that beeps when work changes to a break or a break changes to work. */
public class HangjelzesFigyelo implements PomodoroFigyelo {

    private Allapot elozoAllapot = Allapot.SZUNETEL;

    @Override
    public void tikk(int hatralevoMasodperc) {
        // Nothing to do every second
    }

    @Override
    public void allapotValtozott(Allapot uj) {
        // Pressing Start / Pause / Reset must be silent: only a switch between two running phases beeps
        boolean eddigFutottAzOra = (elozoAllapot != Allapot.SZUNETEL);
        boolean mostIsFutAzOra = (uj != Allapot.SZUNETEL);

        if (eddigFutottAzOra && mostIsFutAzOra) {
            Toolkit.getDefaultToolkit().beep();
        }
        elozoAllapot = uj;
    }
}
