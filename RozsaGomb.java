package pomodoro.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JButton;

/** A cute round (pill-shaped) button with white fill and pink text. It can show a small picture. */
public class RozsaGomb extends JButton {

    public RozsaGomb(String szoveg, Icon ikon) {
        super(szoveg, ikon);

        // We paint the background ourselves, so switch off the normal look
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);

        setFont(RozsaTema.betu(java.awt.Font.BOLD, 16));
        setForeground(RozsaTema.MELY_ROZSA_SZOVEG);
        setIconTextGap(8);
        setMargin(new Insets(8, 18, 8, 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics grafika) {
        Graphics2D rajzolo = (Graphics2D) grafika.create();
        rajzolo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Choose the fill colour depending on what the mouse is doing
        Color kitoltoSzin;
        if (!isEnabled()) {
            kitoltoSzin = new Color(255, 255, 255, 110);
        } else if (getModel().isPressed()) {
            kitoltoSzin = RozsaTema.GOMB_LENYOMVA;
        } else if (getModel().isRollover()) {
            kitoltoSzin = RozsaTema.GOMB_FELETTE;
        } else {
            kitoltoSzin = Color.WHITE;
        }

        int kerekitesMerete = getHeight(); // fully round ends
        rajzolo.setColor(kitoltoSzin);
        rajzolo.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, kerekitesMerete, kerekitesMerete);

        rajzolo.setColor(RozsaTema.FORRO_ROZSA);
        rajzolo.setStroke(new BasicStroke(2f));
        rajzolo.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, kerekitesMerete, kerekitesMerete);

        rajzolo.dispose();

        // The normal painting draws the text and the picture on top
        super.paintComponent(grafika);
    }
}
