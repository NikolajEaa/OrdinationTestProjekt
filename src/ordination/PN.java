package ordination;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class PN extends Ordination {

    private double antalEnheder;
    private final ArrayList<LocalDate> givetDosisTidspunkt = new ArrayList<>();

    public PN(LocalDate startDen, LocalDate slutDen, double antalEnheder) {
        super(startDen, slutDen);
        this.antalEnheder = antalEnheder;
    }

    /**
     * Registrerer at der er givet en dosis paa dagen givesDen
     * Returnerer true hvis givesDen er inden for ordinationens gyldighedsperiode og datoen huskes
     * Retrurner false ellers og datoen givesDen ignoreres
     *
     * @param givesDen
     * @return
     */
    public boolean givDosis(LocalDate givesDen) {
        // TODO
        if (givesDen.isBefore(getStartDen()) || givesDen.isAfter(getSlutDen())) {
            return false;
        } else {
            givetDosisTidspunkt.add(givesDen);
            return true;
        }
    }

    public double doegnDosis() {
        Collections.sort(givetDosisTidspunkt);
        int antaldage = 1;
        double doegnDosis = 0;
        if (givetDosisTidspunkt.isEmpty()) {
            doegnDosis = 0;
        } else {
            antaldage += ((int) ChronoUnit.DAYS.between(givetDosisTidspunkt.getFirst(),
                    givetDosisTidspunkt.getLast()));
            //Udregner doegndosis
            doegnDosis = antalEnheder * getAntalGangeGivet() / (double)antaldage;
        }
        return doegnDosis;
    }

    @Override
    public String getType() {
        return "PN";
    }


    public double samletDosis() {
        // TODO
        return getAntalGangeGivet() * antalEnheder;
    }

    /**
     * Returnerer antal gange ordinationen er anvendt
     *
     * @return
     */
    public int getAntalGangeGivet() {
        return givetDosisTidspunkt.size();
    }

    public double getAntalEnheder() {
        return antalEnheder;
    }

    public ArrayList<LocalDate> getGivetDosisTidspunkt() {
        return new ArrayList<LocalDate>(givetDosisTidspunkt);
    }
}
