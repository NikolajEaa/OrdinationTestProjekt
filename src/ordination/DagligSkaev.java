package ordination;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class DagligSkaev extends Ordination {
    // TODO
    private ArrayList<Dosis> doser;


    public DagligSkaev(LocalDate startDen, LocalDate slutDen) {
        super(startDen, slutDen);
        this.doser = new ArrayList<Dosis>();
    }
    public ArrayList<Dosis> getDoser() {
        return doser;
    }

    public void opretDosis(LocalTime tid, double antal) {
        Dosis dosis = new Dosis(tid, antal);
        doser.add(dosis);
    }

    @Override
    public double samletDosis() {
        //Starter med at finde antal dage i forløbet
        int numberOfDays = (int) ChronoUnit.DAYS.between(getStartDen(),getSlutDen());
        double antalDagligDoser = doegnDosis();
        //Udregner samlet dosis
        double samletDosis = numberOfDays * antalDagligDoser;
        return samletDosis;
    }

    @Override
    public double doegnDosis() {
        double antalDagligDoser = 0;
        for (Dosis dosis : doser) {
            antalDagligDoser += dosis.getAntal();
        }
        return antalDagligDoser;
    }

    @Override
    public String getType() {
        return "Daglig Skæv";
    }
}
