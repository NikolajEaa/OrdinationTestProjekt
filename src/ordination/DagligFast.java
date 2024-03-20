package ordination;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DagligFast extends Ordination {

    private Dosis[] doser = new Dosis[4];

    public DagligFast(LocalDate startDen, LocalDate slutDen) {
        super(startDen, slutDen);
    }


    public Dosis[] getDoser() {
        return doser;
    }

    //Opreter en dosis, og sætter den på første ledige plads
    public void addDosiser(double antal, double antal2, double antal3, double antal4) {
        Dosis morgenDosis = new Dosis(LocalTime.of(9,0,0), antal);
        doser[0] = morgenDosis;
        Dosis middagDosis = new Dosis(LocalTime.of(12,0,0), antal2);
        doser[1] = middagDosis;
        Dosis aftenDosis = new Dosis(LocalTime.of(18,0,0), antal3);
        doser[2] = aftenDosis;
        Dosis natDosis = new Dosis(LocalTime.of(23,59,59), antal4);
        doser[3] = natDosis;
    }

    @Override
    public double samletDosis() {
        long antalDage = ChronoUnit.DAYS.between(getStartDen(), getSlutDen());


        return (antalDage + 1) * doegnDosis();
    }

    @Override
    public double doegnDosis() {
        int sum = 0;
        for (Dosis dosis : doser) {
            sum += dosis.getAntal();
        }
        return sum;
    }

    @Override
    public String getType() {
        return "Daglig Fast";
    }
}
