package ordination;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DagligFast extends Ordination{

    private Dosis[] doser = new Dosis[4];

    public DagligFast(LocalDate startDen, LocalDate slutDen) {
        super(startDen, slutDen);
    }


    public Dosis[] getDoser() {
        return doser;
    }

    //Opreter en dosis, og sætter den på første ledige plads
    public void createDosis(LocalTime tid, double antal) {
        Dosis dosis = new Dosis(tid, antal);
        for (int i = 0; i < doser.length; i++) {
            if (doser[i].equals(null)) {
                doser[i] = dosis;
                i = 4;
            }
        }
    }

    @Override
    public double samletDosis() {
       long antalDage = ChronoUnit.DAYS.between(getStartDen(), getSlutDen());
       return antalDage * doegnDosis();
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
