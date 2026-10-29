package pomodoro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the model only. No window, no Swing. */
class PomodoroModelTeszt {

    private PomodoroModel modell;

    @BeforeEach
    void elokeszites() {
        modell = new PomodoroModel();
        // 1 minute work, 1 minute short break, 2 minutes long break -> short tests
        modell.beallitIdok(1, 1, 2);
    }

    /** Helper: ticks exactly as many seconds as the current phase has left. */
    private void vegigTikkelAFazison() {
        int hatralevoMasodpercek = modell.getHatralevoMasodperc();
        for (int masodperc = 0; masodperc < hatralevoMasodpercek; masodperc++) {
            modell.tick();
        }
    }

    /** Simple observer that only counts what it receives. */
    private static class SzamlaloFigyelo implements PomodoroFigyelo {
        int tikkSzam = 0;
        int allapotValtasSzam = 0;
        Allapot utolsoAllapot = null;

        @Override
        public void tikk(int hatralevoMasodperc) {
            tikkSzam++;
        }

        @Override
        public void allapotValtozott(Allapot uj) {
            allapotValtasSzam++;
            utolsoAllapot = uj;
        }
    }

    @Test
    void ujModellSzunetelAllapotbanIndul() {
        assertEquals(Allapot.SZUNETEL, modell.getAllapot());
        assertEquals(60, modell.getHatralevoMasodperc());
        assertEquals(0, modell.getBefejezettKorok());
    }

    @Test
    void startMunkaAllapotbaKapcsol() {
        modell.start();
        assertEquals(Allapot.MUNKA, modell.getAllapot());
    }

    @Test
    void tickCsokkentiAHatralevoIdot() {
        modell.start();
        modell.tick();
        assertEquals(59, modell.getHatralevoMasodperc());
    }

    @Test
    void szunetenKozbenNemTelikAzIdo() {
        modell.start();
        modell.tick();
        modell.szunet();
        modell.tick();
        modell.tick();
        assertEquals(Allapot.SZUNETEL, modell.getAllapot());
        assertEquals(59, modell.getHatralevoMasodperc());
    }

    @Test
    void szunetUtanStartFolytatjaAMunkat() {
        modell.start();
        modell.tick();
        modell.szunet();
        modell.start();
        assertEquals(Allapot.MUNKA, modell.getAllapot());
        assertEquals(59, modell.getHatralevoMasodperc());
    }

    @Test
    void munkaVegeUtanRovidSzunetKovetkezik() {
        modell.start();
        vegigTikkelAFazison();
        assertEquals(Allapot.ROVID_SZUNET, modell.getAllapot());
        assertEquals(1, modell.getBefejezettKorok());
        assertEquals(60, modell.getHatralevoMasodperc());
    }

    @Test
    void szunetVegeUtanVisszaterAMunka() {
        modell.start();
        vegigTikkelAFazison(); // work
        vegigTikkelAFazison(); // short break
        assertEquals(Allapot.MUNKA, modell.getAllapot());
    }

    @Test
    void negyedikKorUtanHosszuSzunetKovetkezik() {
        modell.start();

        // Rounds 1, 2 and 3: work then a short break each
        for (int kor = 1; kor <= 3; kor++) {
            vegigTikkelAFazison();
            assertEquals(Allapot.ROVID_SZUNET, modell.getAllapot());
            vegigTikkelAFazison();
            assertEquals(Allapot.MUNKA, modell.getAllapot());
        }

        // Round 4 ends -> long break
        vegigTikkelAFazison();
        assertEquals(Allapot.HOSSZU_SZUNET, modell.getAllapot());
        assertEquals(4, modell.getBefejezettKorok());
        assertEquals(120, modell.getHatralevoMasodperc());
    }

    @Test
    void resetMindentVisszaallit() {
        modell.start();
        vegigTikkelAFazison();
        modell.tick();

        modell.reset();

        assertEquals(Allapot.SZUNETEL, modell.getAllapot());
        assertEquals(60, modell.getHatralevoMasodperc());
        assertEquals(0, modell.getBefejezettKorok());
    }

    @Test
    void figyeloErtesitestKapTikkelesreEsAllapotvaltasra() {
        SzamlaloFigyelo figyelo = new SzamlaloFigyelo();
        modell.feliratkoz(figyelo);

        modell.start();
        modell.tick();
        modell.tick();

        assertEquals(1, figyelo.allapotValtasSzam);
        assertEquals(Allapot.MUNKA, figyelo.utolsoAllapot);
        assertEquals(2, figyelo.tikkSzam);
    }

    @Test
    void ervenytelenIdoKivetelt_dob() {
        assertThrows(IllegalArgumentException.class, () -> modell.beallitIdok(0, 5, 15));
    }
}
