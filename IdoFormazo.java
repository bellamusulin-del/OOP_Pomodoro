package pomodoro.view;

/** Turns seconds into text like "04:07". */
public final class IdoFormazo {

    public static String formaz(int osszesMasodperc) {
        int percek = osszesMasodperc / 60;
        int maradekMasodpercek = osszesMasodperc % 60;
        return String.format("%02d:%02d", percek, maradekMasodpercek);
    }

    private IdoFormazo() {
        // utility class
    }
}
