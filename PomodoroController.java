package pomodoro.controller;

import java.time.LocalDate;
import java.util.Map;
import javax.swing.Timer;
import pomodoro.model.NapiStatisztika;
import pomodoro.model.PomodoroModel;
import pomodoro.view.BeallitasokDialog;
import pomodoro.view.PomodoroView;
import pomodoro.view.StatisztikaDialog;

/**
 * The controller (the "C" in MVC).
 * It owns the javax.swing.Timer, connects the buttons to the model
 * and opens the dialogs. It never changes labels or colours itself.
 */
public class PomodoroController {

    private static final int EGY_MASODPERC_EZREDMASODPERCBEN = 1000;
    private static final int HANY_NAPOT_MUTAT_A_DIAGRAM = 7;

    private final PomodoroModel modell;
    private final PomodoroView nezet;
    private final NapiStatisztika statisztika;

    // javax.swing.Timer fires on the Event Dispatch Thread, so no Thread.sleep is needed
    private final Timer masodpercIdozito;

    public PomodoroController(PomodoroModel modell, PomodoroView nezet, NapiStatisztika statisztika) {
        this.modell = modell;
        this.nezet = nezet;
        this.statisztika = statisztika;

        // Every second the timer simply tells the model that a second passed
        masodpercIdozito = new Timer(EGY_MASODPERC_EZREDMASODPERCBEN, esemeny -> {
            this.modell.tick();
        });

        gombokBekotese();
    }

    /** Attaches one ActionListener to each button. */
    private void gombokBekotese() {
        nezet.getStartGomb().addActionListener(esemeny -> {
            modell.start();
            masodpercIdozito.start();
        });

        nezet.getSzunetGomb().addActionListener(esemeny -> {
            modell.szunet();
            masodpercIdozito.stop();
        });

        nezet.getResetGomb().addActionListener(esemeny -> {
            modell.reset();
            masodpercIdozito.stop();
        });

        nezet.getBeallitasokGomb().addActionListener(esemeny -> {
            beallitasokMegnyitasa();
        });

        nezet.getStatisztikaGomb().addActionListener(esemeny -> {
            statisztikaMegnyitasa();
        });
    }

    /** Pauses the clock, shows the modal dialog and saves the numbers if OK was pressed. */
    private void beallitasokMegnyitasa() {
        modell.szunet();
        masodpercIdozito.stop();

        BeallitasokDialog dialog = new BeallitasokDialog(
                nezet,
                modell.getMunkaPercek(),
                modell.getRovidSzunetPercek(),
                modell.getHosszuSzunetPercek());

        // Modal: the code stops on this line until the dialog is closed
        dialog.setVisible(true);

        if (dialog.isElfogadva()) {
            modell.beallitIdok(
                    dialog.getMunkaPercek(),
                    dialog.getRovidSzunetPercek(),
                    dialog.getHosszuSzunetPercek());
        }
    }

    /** Shows the bar chart of the last days. The clock keeps running in the background. */
    private void statisztikaMegnyitasa() {
        Map<LocalDate, Integer> utolsoNapok = statisztika.getUtolsoNapok(HANY_NAPOT_MUTAT_A_DIAGRAM);
        int maiDarabszam = statisztika.getMaiDarabszam();

        StatisztikaDialog dialog = new StatisztikaDialog(nezet, utolsoNapok, maiDarabszam);
        dialog.setVisible(true);
    }
}
