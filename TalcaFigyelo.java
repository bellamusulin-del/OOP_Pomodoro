package pomodoro.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import pomodoro.model.Allapot;
import pomodoro.model.PomodoroFigyelo;

/** An observer that shows the remaining time in the tooltip of a system tray icon. */
public class TalcaFigyelo implements PomodoroFigyelo {

    private TrayIcon talcaIkon = null;
    private String allapotNev = "Pomodoro";

    public TalcaFigyelo(Image ikonKep) {
        if (ikonKep == null) {
            return;
        }
        if (!SystemTray.isSupported()) {
            System.out.println("The system tray is not supported on this computer.");
            return;
        }

        talcaIkon = new TrayIcon(ikonKep, "Pomodoro");
        talcaIkon.setImageAutoSize(true);

        try {
            SystemTray.getSystemTray().add(talcaIkon);
        } catch (AWTException hiba) {
            System.out.println("Could not add the tray icon: " + hiba.getMessage());
            talcaIkon = null;
        }
    }

    @Override
    public void tikk(int hatralevoMasodperc) {
        if (talcaIkon != null) {
            talcaIkon.setToolTip(allapotNev + " – " + IdoFormazo.formaz(hatralevoMasodperc));
        }
    }

    @Override
    public void allapotValtozott(Allapot uj) {
        allapotNev = uj.getMegjelenitettNev();
    }
}
