package pomodoro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for the daily statistics. JUnit gives us a temporary folder, so no real file is touched. */
class StatisztikaTeszt {

    @TempDir
    Path ideiglenesMappa;

    private Path statisztikaFajl;
    private NapiStatisztika statisztika;
    private PomodoroModel modell;

    @BeforeEach
    void elokeszites() {
        statisztikaFajl = ideiglenesMappa.resolve("statisztika.txt");
        statisztika = new NapiStatisztika(statisztikaFajl);

        modell = new PomodoroModel();
        modell.beallitIdok(1, 1, 2);
        modell.feliratkoz(new StatisztikaFigyelo(statisztika));
    }

    private void vegigTikkelAFazison() {
        int hatralevoMasodpercek = modell.getHatralevoMasodperc();
        for (int masodperc = 0; masodperc < hatralevoMasodpercek; masodperc++) {
            modell.tick();
        }
    }

    @Test
    void befejezettMunkaNoveliAMaiSzamot() {
        modell.start();
        vegigTikkelAFazison();
        assertEquals(1, statisztika.getMaiDarabszam());
    }

    @Test
    void szunetUtanFolytatottMunkaIsSzamit() {
        modell.start();
        modell.tick();
        modell.szunet();
        modell.start();
        vegigTikkelAFazison();
        assertEquals(1, statisztika.getMaiDarabszam());
    }

    @Test
    void megszakitottMunkaNemSzamit() {
        modell.start();
        modell.tick();
        modell.szunet();
        modell.reset();
        assertEquals(0, statisztika.getMaiDarabszam());
    }

    @Test
    void rovidSzunetVegeNemSzamitPomodoronak() {
        modell.start();
        vegigTikkelAFazison(); // work -> counted
        vegigTikkelAFazison(); // short break -> must NOT be counted
        assertEquals(1, statisztika.getMaiDarabszam());
    }

    @Test
    void statisztikaFajlbolVisszatoltheto() {
        modell.start();
        vegigTikkelAFazison();

        NapiStatisztika ujPeldany = new NapiStatisztika(statisztikaFajl);
        assertEquals(1, ujPeldany.getMaiDarabszam());
    }

    @Test
    void utolsoNapokMindigPontosanAKertDarabszamotAdja() {
        assertEquals(7, statisztika.getUtolsoNapok(7).size());
    }
}
