package pomodoro.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import pomodoro.model.Allapot;
import pomodoro.model.PomodoroFigyelo;
import pomodoro.model.PomodoroModel;

/**
 * The window (the "V" in MVC).
 * It is also an observer: the model calls tikk() and allapotValtozott(),
 * and the view only repaints its labels, colours and mascot picture.
 * The buttons have NO logic here; the controller attaches the listeners.
 */
public class PomodoroView extends JFrame implements PomodoroFigyelo {

    private static final int NAGY_IKON_MAGASSAG = 130;
    private static final int KICSI_IKON_MAGASSAG = 26;

    private final PomodoroModel modell;

    // The big mascot pictures, one for each look of the clock
    private final ImageIcon munkaMaszkot;
    private final ImageIcon rovidSzunetMaszkot;
    private final ImageIcon hosszuSzunetMaszkot;
    private final ImageIcon szunetelMaszkot;

    private final JPanel foPanel = new JPanel(new BorderLayout(10, 10));

    private final JLabel maszkotCimke = new JLabel("", SwingConstants.CENTER);
    private final JLabel allapotCimke = new JLabel("", SwingConstants.CENTER);
    private final JLabel idoCimke = new JLabel("25:00", SwingConstants.CENTER);
    private final JLabel korCimke = new JLabel("", SwingConstants.CENTER);

    private final RozsaGomb startGomb;
    private final RozsaGomb szunetGomb;
    private final RozsaGomb resetGomb;
    private final RozsaGomb beallitasokGomb;
    private final RozsaGomb statisztikaGomb;

    public PomodoroView(PomodoroModel modell) {
        super("Pomodoro-óra ♥");
        this.modell = modell;

        // Load the pictures once
        munkaMaszkot = KepBetoltes.betoltIkon("kitty_masni.png", NAGY_IKON_MAGASSAG);
        rovidSzunetMaszkot = KepBetoltes.betoltIkon("cinnamoroll.png", NAGY_IKON_MAGASSAG);
        hosszuSzunetMaszkot = KepBetoltes.betoltIkon("kitty_virag.png", NAGY_IKON_MAGASSAG);
        szunetelMaszkot = KepBetoltes.betoltIkon("cinnamoroll.png", NAGY_IKON_MAGASSAG);

        // Cute buttons with tiny pictures on the first three
        startGomb = new RozsaGomb("Start", KepBetoltes.betoltIkon("kitty_masni.png", KICSI_IKON_MAGASSAG));
        szunetGomb = new RozsaGomb("Szünet", KepBetoltes.betoltIkon("cinnamoroll.png", KICSI_IKON_MAGASSAG));
        resetGomb = new RozsaGomb("Reset", KepBetoltes.betoltIkon("kitty_virag.png", KICSI_IKON_MAGASSAG));
        beallitasokGomb = new RozsaGomb("♥ Beállítások", null);
        statisztikaGomb = new RozsaGomb("♥ Statisztika", null);

        felepitUI();

        // Subscribe to the model, then show the current values once
        this.modell.feliratkoz(this);
        frissitMindent();
    }

    // ---------- Building the window ----------

    private void felepitUI() {
        allapotCimke.setFont(RozsaTema.betu(java.awt.Font.BOLD, 28));
        allapotCimke.setForeground(Color.WHITE);

        idoCimke.setFont(RozsaTema.betu(java.awt.Font.BOLD, 90));
        idoCimke.setForeground(Color.WHITE);

        korCimke.setFont(RozsaTema.betu(java.awt.Font.PLAIN, 18));
        korCimke.setForeground(Color.WHITE);

        // Top: mascot picture above the state text
        JPanel felsoPanel = new JPanel(new BorderLayout(0, 5));
        felsoPanel.setOpaque(false);
        felsoPanel.add(maszkotCimke, BorderLayout.CENTER);
        felsoPanel.add(allapotCimke, BorderLayout.SOUTH);

        // Bottom: two rows of buttons
        JPanel elsoGombSor = new JPanel(new GridLayout(1, 3, 10, 10));
        elsoGombSor.setOpaque(false);
        elsoGombSor.add(startGomb);
        elsoGombSor.add(szunetGomb);
        elsoGombSor.add(resetGomb);

        JPanel masodikGombSor = new JPanel(new GridLayout(1, 2, 10, 10));
        masodikGombSor.setOpaque(false);
        masodikGombSor.add(beallitasokGomb);
        masodikGombSor.add(statisztikaGomb);

        JPanel gombPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        gombPanel.setOpaque(false);
        gombPanel.add(elsoGombSor);
        gombPanel.add(masodikGombSor);

        JPanel alsoPanel = new JPanel(new BorderLayout(5, 10));
        alsoPanel.setOpaque(false);
        alsoPanel.add(korCimke, BorderLayout.NORTH);
        alsoPanel.add(gombPanel, BorderLayout.CENTER);

        foPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        foPanel.add(felsoPanel, BorderLayout.NORTH);
        foPanel.add(idoCimke, BorderLayout.CENTER);
        foPanel.add(alsoPanel, BorderLayout.SOUTH);

        setContentPane(foPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 640);
        setLocationRelativeTo(null);
    }

    // ---------- Observer methods (called by the model) ----------

    @Override
    public void tikk(int hatralevoMasodperc) {
        idoCimke.setText(IdoFormazo.formaz(hatralevoMasodperc));
    }

    @Override
    public void allapotValtozott(Allapot ujAllapot) {
        allapotCimke.setText(ujAllapot.getMegjelenitettNev());
        korCimke.setText("Befejezett körök: " + modell.getBefejezettKorok() + " ♥");

        // Turn the hex text from the model into a real colour
        Color hatterSzin = Color.decode(ujAllapot.getSzinKod());
        foPanel.setBackground(hatterSzin);

        maszkotCimke.setIcon(maszkotKivalasztasa(ujAllapot));

        // Start only makes sense when paused, Pause only when running
        boolean szunetelAKijelzo = (ujAllapot == Allapot.SZUNETEL);
        startGomb.setEnabled(szunetelAKijelzo);
        szunetGomb.setEnabled(!szunetelAKijelzo);
    }

    // ---------- Helpers ----------

    /** Picks the mascot picture that belongs to the given state. */
    private ImageIcon maszkotKivalasztasa(Allapot allapot) {
        if (allapot == Allapot.MUNKA) {
            return munkaMaszkot;
        } else if (allapot == Allapot.ROVID_SZUNET) {
            return rovidSzunetMaszkot;
        } else if (allapot == Allapot.HOSSZU_SZUNET) {
            return hosszuSzunetMaszkot;
        } else {
            return szunetelMaszkot;
        }
    }

    /** Shows everything once, using the current values of the model. */
    private void frissitMindent() {
        tikk(modell.getHatralevoMasodperc());
        allapotValtozott(modell.getAllapot());
    }

    // ---------- Getters so the controller can attach its listeners ----------

    public RozsaGomb getStartGomb() {
        return startGomb;
    }

    public RozsaGomb getSzunetGomb() {
        return szunetGomb;
    }

    public RozsaGomb getResetGomb() {
        return resetGomb;
    }

    public RozsaGomb getBeallitasokGomb() {
        return beallitasokGomb;
    }

    public RozsaGomb getStatisztikaGomb() {
        return statisztikaGomb;
    }
}
