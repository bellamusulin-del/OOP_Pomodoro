package pomodoro;

import java.nio.file.Paths;
import javax.swing.SwingUtilities;
import pomodoro.controller.PomodoroController;
import pomodoro.model.NapiStatisztika;
import pomodoro.model.PomodoroModel;
import pomodoro.model.StatisztikaFigyelo;
import pomodoro.view.HangjelzesFigyelo;
import pomodoro.view.KepBetoltes;
import pomodoro.view.PomodoroView;
import pomodoro.view.TalcaFigyelo;

/** Entry point: creates the MVC parts, adds the extra observers and connects everything. */
public class Fo {

    public static void main(String[] args) {
        // All Swing objects must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PomodoroModel modell = new PomodoroModel();
            NapiStatisztika statisztika = new NapiStatisztika(Paths.get("statisztika.txt"));
            PomodoroView nezet = new PomodoroView(modell);

            // Extra observers: each one reacts to the model in its own way
            modell.feliratkoz(new StatisztikaFigyelo(statisztika));
            modell.feliratkoz(new HangjelzesFigyelo());
            modell.feliratkoz(new TalcaFigyelo(KepBetoltes.betoltKep("cinnamoroll.png")));

            PomodoroController vezerlo = new PomodoroController(modell, nezet, statisztika);

            nezet.setVisible(true);
        });
    }
}
