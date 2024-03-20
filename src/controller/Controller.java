package controller;

import ordination.*;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Storage storage;
    private static Controller controller;

    private Controller() {
        storage = new Storage();
    }

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static Controller getTestController() {
        return new Controller();
    }

    /**
     * Hvis startDato er efter slutDato kastes en IllegalArgumentException og
     * ordinationen oprettes ikke
     * Pre: startDen, slutDen, patient og laegemiddel er ikke null
     * Pre: antal >= 0
     *
     * @return opretter og returnerer en PN ordination.
     */
    public PN opretPNOrdination(LocalDate startDen, LocalDate slutDen,
                                Patient patient, Laegemiddel laegemiddel, double antal) {
        if (!checkStartFoerSlut(startDen, slutDen)) {
            throw new IllegalArgumentException("Start dato skal være før slut dato!");
        }
        //Opretter ordinationen
        PN pnOrdination = new PN(startDen, slutDen, antal);
        pnOrdination.setLaegemiddel(laegemiddel);
        //Gives til patienten
        patient.addOrdination(pnOrdination);
        return pnOrdination;
    }

    /**
     * Opretter og returnerer en DagligFast ordination. Hvis startDato er efter
     * slutDato kastes en IllegalArgumentException og ordinationen oprettes ikke
     * Pre: startDen, slutDen, patient og laegemiddel er ikke null
     * Pre: margenAntal, middagAntal, aftanAntal, natAntal >= 0
     */
    public DagligFast opretDagligFastOrdination(LocalDate startDen,
                                                LocalDate slutDen, Patient patient, Laegemiddel laegemiddel,
                                                double morgenAntal, double middagAntal, double aftenAntal,
                                                double natAntal) {
        if (!checkStartFoerSlut(startDen, slutDen)) {
            throw new IllegalArgumentException("Start dato skal være før slut dato!");
        }
        if (morgenAntal < 0 || middagAntal < 0 || aftenAntal < 0 || natAntal < 0) {
            throw new IllegalArgumentException("Ordinationerne må ikke være negative");
        }
        double sum = morgenAntal + middagAntal + aftenAntal + natAntal;

        if (sum == 0) {
            throw new IllegalArgumentException("Daglig Fast skal have en værdi på en af den pågældende dage");
        }
        //Opretter dagligfast
        DagligFast dagligFast = new DagligFast(startDen, slutDen);
        dagligFast.setLaegemiddel(laegemiddel);
        dagligFast.addDosiser(morgenAntal, middagAntal, aftenAntal, natAntal);
        //gives til patien
        patient.addOrdination(dagligFast);
        return dagligFast;
    }

    /**
     * Opretter og returnerer en DagligSkæv ordination. Hvis startDato er efter
     * slutDato kastes en IllegalArgumentException og ordinationen oprettes ikke.
     * Hvis antallet af elementer i klokkeSlet og antalEnheder er forskellige kastes også en IllegalArgumentException.
     * <p>
     * Pre: startDen, slutDen, patient og laegemiddel er ikke null
     * Pre: alle tal i antalEnheder > 0
     */
    public DagligSkaev opretDagligSkaevOrdination(LocalDate startDen,
                                                  LocalDate slutDen, Patient patient, Laegemiddel laegemiddel,
                                                  LocalTime[] klokkeSlet, double[] antalEnheder) {
        //Tjekker datoen
        if (!checkStartFoerSlut(startDen, slutDen)) {
            throw new IllegalArgumentException("Start dato skal være før slut dato!");
        }

        //Tjekker klokkeslet og enheder
        if (klokkeSlet.length != antalEnheder.length) {
            throw new IllegalArgumentException("Det skal være én dosis for hvert klokkeslet");
        }

        //Opretter dagligskaev og giver den laegemiddel
        DagligSkaev dagligSkaev = new DagligSkaev(startDen, slutDen);
        dagligSkaev.setLaegemiddel(laegemiddel);
        //Opretter de passende doser
        for (int i = 0; i < klokkeSlet.length; i++) {
            dagligSkaev.opretDosis(klokkeSlet[i], antalEnheder[i]);
        }
        //Adder til patien
        patient.addOrdination(dagligSkaev);
        return dagligSkaev;
    }

    /**
     * En dato for hvornår ordinationen anvendes tilføjes ordinationen. Hvis
     * datoen ikke er indenfor ordinationens gyldighedsperiode kastes en
     * IllegalArgumentException
     * Pre: ordination og dato er ikke null
     */
    public void ordinationPNAnvendt(PN ordination, LocalDate dato) {
        if (dato.isAfter(ordination.getSlutDen()) || dato.isBefore(ordination.getStartDen())) {
            throw new IllegalArgumentException("Dato er udenfor gyldighedsperiode");
        }
        ordination.givDosis(dato);
    }

    /**
     * Den anbefalede dosis for den pågældende patient (der skal tages hensyn
     * til patientens vægt). Det er en forskellig enheds faktor der skal
     * anvendes, og den er afhængig af patientens vægt.
     * Pre: patient og lægemiddel er ikke null
     */
    public double anbefaletDosisPrDoegn(Patient patient, Laegemiddel laegemiddel) {
        double anbefaletDosis = 0;
        double vaegt = patient.getVaegt();
        if (vaegt < 25) {
            anbefaletDosis = laegemiddel.getEnhedPrKgPrDoegnLet();
        } else if (vaegt <   120) {
            anbefaletDosis = laegemiddel.getEnhedPrKgPrDoegnNormal();
        } else anbefaletDosis = laegemiddel.getEnhedPrKgPrDoegnTung();
        return anbefaletDosis;
    }

    /**
     * For et givent vægtinterval og et givent lægemiddel, hentes antallet af
     * ordinationer.
     * Pre: laegemiddel er ikke null
     */
    public int antalOrdinationerPrVægtPrLægemiddel(double vægtStart,
                                                   double vægtSlut, Laegemiddel laegemiddel) {
        ArrayList<Patient> patienter = (ArrayList<Patient>) getAllPatienter();
        ArrayList<Ordination> ordinationerPrVægtPrLægemiddel = new ArrayList<>();

        int antalOrdinationer = 0;
        //Løber alle patiener igennem
        for (Patient patient : patienter) {
            //Hvis patientens vægt er inde for intervallet checkes ordinationerne
            if (patient.getVaegt() >= vægtStart && patient.getVaegt() <= vægtSlut) {
                for (Ordination ordination : patient.getOrdinationer()) {
                    if (ordination.getLaegemiddel().equals(laegemiddel)) {
                        antalOrdinationer++;
                    }
                }
            }
        }
        return antalOrdinationer;
    }

    public List<Patient> getAllPatienter() {
        return storage.getAllPatienter();
    }

    public List<Laegemiddel> getAllLaegemidler() {
        return storage.getAllLaegemidler();
    }

    /**
     * Metode der kan bruges til at checke at en startDato ligger før en
     * slutDato.
     *
     * @return true hvis startDato er før slutDato, false ellers.
     */
    private boolean checkStartFoerSlut(LocalDate startDato, LocalDate slutDato) {
        boolean result = true;
        if (slutDato.compareTo(startDato) < 0) {
            result = false;
        }
        return result;
    }

    public Patient opretPatient(String cpr, String navn, double vaegt) {
        Patient p = new Patient(cpr, navn, vaegt);
        storage.addPatient(p);
        return p;
    }

    public Laegemiddel opretLaegemiddel(String navn,
                                        double enhedPrKgPrDoegnLet, double enhedPrKgPrDoegnNormal,
                                        double enhedPrKgPrDoegnTung, String enhed) {
        Laegemiddel lm = new Laegemiddel(navn, enhedPrKgPrDoegnLet,
                enhedPrKgPrDoegnNormal, enhedPrKgPrDoegnTung, enhed);
        storage.addLaegemiddel(lm);
        return lm;
    }

    public void createSomeObjects() {
        this.opretPatient("121256-0512", "Jane Jensen", 63.4);
        this.opretPatient("070985-1153", "Finn Madsen", 83.2);
        this.opretPatient("050972-1233", "Hans Jørgensen", 89.4);
        this.opretPatient("011064-1522", "Ulla Nielsen", 59.9);
        this.opretPatient("090149-2529", "Ib Hansen", 87.7);

        this.opretLaegemiddel("Acetylsalicylsyre", 0.1, 0.15, 0.16, "Styk");
        this.opretLaegemiddel("Paracetamol", 1, 1.5, 2, "Ml");
        this.opretLaegemiddel("Fucidin", 0.025, 0.025, 0.025, "Styk");
        this.opretLaegemiddel("Methotrexat", 0.01, 0.015, 0.02, "Styk");

        this.opretPNOrdination(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 12),
                storage.getAllPatienter().get(0), storage.getAllLaegemidler()
                        .get(1),
                123);

        this.opretPNOrdination(LocalDate.of(2021, 2, 12), LocalDate.of(2021, 2, 14),
                storage.getAllPatienter().get(0), storage.getAllLaegemidler()
                        .get(0),
                3);

        this.opretPNOrdination(LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 25),
                storage.getAllPatienter().get(3), storage.getAllLaegemidler()
                        .get(2),
                5);

        this.opretPNOrdination(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 12),
                storage.getAllPatienter().get(0), storage.getAllLaegemidler()
                        .get(1),
                123);

        this.opretDagligFastOrdination(LocalDate.of(2021, 1, 10),
                LocalDate.of(2021, 1, 12), storage.getAllPatienter().get(1),
                storage.getAllLaegemidler().get(1), 2, 0, 1, 0);

        LocalTime[] kl = {LocalTime.of(12, 0), LocalTime.of(12, 40),
                LocalTime.of(16, 0), LocalTime.of(18, 45)};
        double[] an = {0.5, 1, 2.5, 3};

        this.opretDagligSkaevOrdination(LocalDate.of(2021, 1, 23),
                LocalDate.of(2021, 1, 24), storage.getAllPatienter().get(1),
                storage.getAllLaegemidler().get(2), kl, an);
    }

}
