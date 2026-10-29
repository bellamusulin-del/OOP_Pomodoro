package pomodoro.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/** Loads the PNG pictures from src/main/resources/kepek. */
public final class KepBetoltes {

    /** Loads one picture; returns null (and prints a message) if it cannot be found. */
    public static BufferedImage betoltKep(String fajlNev) {
        URL kepCime = KepBetoltes.class.getResource("/kepek/" + fajlNev);
        if (kepCime == null) {
            System.out.println("Picture not found: " + fajlNev);
            return null;
        }
        try {
            return ImageIO.read(kepCime);
        } catch (IOException hiba) {
            System.out.println("Could not read picture " + fajlNev + ": " + hiba.getMessage());
            return null;
        }
    }

    /** Loads a picture and shrinks it to the given height, keeping the proportions. */
    public static ImageIcon betoltIkon(String fajlNev, int magassag) {
        BufferedImage eredetiKep = betoltKep(fajlNev);
        if (eredetiKep == null) {
            return null;
        }
        // Width -1 means "keep the proportions"
        Image kisebbKep = eredetiKep.getScaledInstance(-1, magassag, Image.SCALE_SMOOTH);
        return new ImageIcon(kisebbKep);
    }

    private KepBetoltes() {
        // utility class
    }
}
