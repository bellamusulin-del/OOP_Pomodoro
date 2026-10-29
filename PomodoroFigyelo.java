package pomodoro.model;

/**
 * The Observer interface.
 * Anything that wants to be told about changes in the model implements this
 * interface and subscribes with PomodoroModel.feliratkoz(...).
 */
public interface PomodoroFigyelo {

    /** Called after every second that passes while the clock is running. */
    void tikk(int hatralevoMasodperc);

    /** Called whenever the state of the clock changes (for example work -> break). */
    void allapotValtozott(Allapot uj);
}
