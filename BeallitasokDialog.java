package pomodoro.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Modal settings dialog where the user can change the three lengths (in minutes).
 * It only collects numbers; the controller decides what to do with them.
 */
public class BeallitasokDialog extends JDialog {

    private final JSpinner munkaSpinner;
    private final JSpinner rovidSzunetSpinner;
    private final JSpinner hosszuSzunetSpinner;

    // Becomes true only when the user presses OK
    private boolean elfogadva = false;

    public BeallitasokDialog(JFrame szuloAblak, int munkaPercek, int rovidSzunetPercek, int hosszuSzunetPercek) {
        // The "true" at the end makes the dialog modal
        super(szuloAblak, "Beállítások", true);

        // Each spinner: start value, minimum, maximum, step
        munkaSpinner = new JSpinner(new SpinnerNumberModel(munkaPercek, 1, 120, 1));
        rovidSzunetSpinner = new JSpinner(new SpinnerNumberModel(rovidSzunetPercek, 1, 60, 1));
        hosszuSzunetSpinner = new JSpinner(new SpinnerNumberModel(hosszuSzunetPercek, 1, 120, 1));

        JPanel mezokPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mezokPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        mezokPanel.add(new JLabel("Munka (perc):"));
        mezokPanel.add(munkaSpinner);
        mezokPanel.add(new JLabel("Rövid szünet (perc):"));
        mezokPanel.add(rovidSzunetSpinner);
        mezokPanel.add(new JLabel("Hosszú szünet (perc):"));
        mezokPanel.add(hosszuSzunetSpinner);

        RozsaGomb okGomb = new RozsaGomb("OK", null);
        RozsaGomb megseGomb = new RozsaGomb("Mégse", null);

        okGomb.addActionListener(esemeny -> {
            elfogadva = true;
            dispose();
        });
        megseGomb.addActionListener(esemeny -> {
            elfogadva = false;
            dispose();
        });

        JPanel gombPanel = new JPanel();
        gombPanel.add(okGomb);
        gombPanel.add(megseGomb);

        // Pastel pink background for everything
        getContentPane().setBackground(RozsaTema.VILAGOS_ROZSA);
        mezokPanel.setBackground(RozsaTema.VILAGOS_ROZSA);
        gombPanel.setBackground(RozsaTema.VILAGOS_ROZSA);

        setLayout(new BorderLayout());
        add(mezokPanel, BorderLayout.CENTER);
        add(gombPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(szuloAblak);
        setResizable(false);
    }

    public boolean isElfogadva() {
        return elfogadva;
    }

    public int getMunkaPercek() {
        return (Integer) munkaSpinner.getValue();
    }

    public int getRovidSzunetPercek() {
        return (Integer) rovidSzunetSpinner.getValue();
    }

    public int getHosszuSzunetPercek() {
        return (Integer) hosszuSzunetSpinner.getValue();
    }
}
