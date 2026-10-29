package pomodoro.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Counts how many pomodoros were finished on each day and keeps the numbers in a text file.
 * File format: one line per day, for example "2026-10-29;4".
 * No Swing here, so it can be tested without a window.
 */
public class NapiStatisztika {

    private final Path fajlUtvonal;
    private final Map<LocalDate, Integer> napiDarabszamok = new TreeMap<>();

    public NapiStatisztika(Path fajlUtvonal) {
        this.fajlUtvonal = fajlUtvonal;
        betoltFajlbol();
    }

    /** Adds one finished pomodoro to today's counter and saves the file. */
    public void pomodoroKeszult() {
        LocalDate ma = LocalDate.now();
        int eddigiDarabszam = napiDarabszamok.getOrDefault(ma, 0);
        napiDarabszamok.put(ma, eddigiDarabszam + 1);
        mentesFajlba();
    }

    public int getMaiDarabszam() {
        return napiDarabszamok.getOrDefault(LocalDate.now(), 0);
    }

    /**
     * Returns the last N days (oldest first, today last).
     * Days without any pomodoro are included with the value 0, so the chart has no gaps.
     */
    public Map<LocalDate, Integer> getUtolsoNapok(int napokSzama) {
        Map<LocalDate, Integer> eredmeny = new LinkedHashMap<>();
        LocalDate ma = LocalDate.now();

        for (int napokOtaVisszafele = napokSzama - 1; napokOtaVisszafele >= 0; napokOtaVisszafele--) {
            LocalDate nap = ma.minusDays(napokOtaVisszafele);
            int darabszam = napiDarabszamok.getOrDefault(nap, 0);
            eredmeny.put(nap, darabszam);
        }
        return eredmeny;
    }

    private void betoltFajlbol() {
        if (!Files.exists(fajlUtvonal)) {
            return; // first start, nothing saved yet
        }
        try {
            List<String> sorok = Files.readAllLines(fajlUtvonal);
            for (String sor : sorok) {
                String[] reszek = sor.split(";");
                if (reszek.length == 2) {
                    LocalDate nap = LocalDate.parse(reszek[0].trim());
                    int darabszam = Integer.parseInt(reszek[1].trim());
                    napiDarabszamok.put(nap, darabszam);
                }
            }
        } catch (IOException | RuntimeException hiba) {
            System.out.println("Could not read the statistics file: " + hiba.getMessage());
        }
    }

    private void mentesFajlba() {
        List<String> sorok = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> bejegyzes : napiDarabszamok.entrySet()) {
            sorok.add(bejegyzes.getKey() + ";" + bejegyzes.getValue());
        }
        try {
            Files.write(fajlUtvonal, sorok);
        } catch (IOException hiba) {
            System.out.println("Could not save the statistics file: " + hiba.getMessage());
        }
    }
}
