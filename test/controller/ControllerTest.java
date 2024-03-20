package controller;

import ordination.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    Controller controller = Controller.getController();
    private Patient patient;
    private Laegemiddel laegemiddel;

    @Test
    void opretPNOrdination() {
        Patient patient = controller.opretPatient("12", "Bent", 78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        PN pn = controller.opretPNOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, 2);

        assertNotNull(pn);

        LocalDate actualStartDato = pn.getStartDen();
        LocalDate exptectedStartDato = LocalDate.of(2024, 12, 12);
        assertEquals(exptectedStartDato, actualStartDato);

        LocalDate actualSlutDato = pn.getSlutDen();
        LocalDate exptectedSlutDato = LocalDate.of(2024, 12, 24);
        assertEquals(exptectedSlutDato, actualSlutDato);

        PN actualPn = (PN) patient.getOrdinationer().get(0);
        PN expectedPN = pn;
        assertEquals(expectedPN, actualPn);

        Laegemiddel actualLaegemiddel = pn.getLaegemiddel();
        Laegemiddel expectedLaemiddel = laegemiddel;
        assertEquals(expectedLaemiddel, actualLaegemiddel);
    }

    @Test
    void opretPNOrdinationSlutFørStart() {
        Controller controller = Controller.getController();
        Patient patient = controller.opretPatient("12", "Bent", 78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        try {
            PN pn = controller.opretPNOrdination(LocalDate.of(2024, 12, 24), LocalDate.of(2024, 12, 22), patient, laegemiddel, 2);
        } catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Start dato skal være før slut dato!"));

        }
    }
    @Test
    void opretPNOrdinationNegativAntal() {
        Controller controller = Controller.getController();
        Patient patient = controller.opretPatient("12", "Bent", 78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        try {
            PN pn = controller.opretPNOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, -2);
        } catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Antal skal være positiv"));
        }
    }
    @Test
    void opretPNOrdinationNulAntal() {
        Controller controller = Controller.getController();
        Patient patient = controller.opretPatient("12", "Bent", 78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        try {
            PN pn = controller.opretPNOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, 0);
        } catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Antal skal være positiv"));
        }
    }

    @BeforeEach
    void setUp() {
        patient = new Patient("123456-7890", "Fornavn Efternavn", 78);
        laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
    }


    @Test
    void DagligFastGyldig_TC1() {
        DagligFast dagligFast = controller.opretDagligFastOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, 1, 2, 2, 0);

        double expected = 5;
        double actual = dagligFast.doegnDosis();

        System.out.println("Doegn Dosis");
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);

        assertTrue(patient.getOrdinationer().contains(dagligFast));
    }

    @Test
    void DagligFastUgyldig_TC1() {

        DagligFast dagligFast = null;
        try {
            dagligFast = controller.opretDagligFastOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, 0, 0, 0, 0);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Daglig Fast skal have en værdi på en af den pågældende dage"));
        }
        assertTrue(!patient.getOrdinationer().contains(dagligFast));
    }

    @Test
    void DagligFastUgyldig_TC2() {

        DagligFast dagligFast = null;
        try {
            dagligFast = controller.opretDagligFastOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, -1, 1, 2, 0);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Ordinationerne må ikke være negative"));
        }
        assertTrue(!patient.getOrdinationer().contains(dagligFast));
    }

    @Test
    void opretDagligSkaevOrdination() {
        //Arrange
        Controller controller = Controller.getController();
        Patient patient = new Patient("123456-7890", "Fornavn Efternavn", 78);
        Laegemiddel laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");

        //Act
        DagligSkaev dagligSkaevTC1 = controller.opretDagligSkaevOrdination(
                LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 24),
                patient,
                laegemiddel,
                new LocalTime[]{LocalTime.of(11, 30), LocalTime.of(17, 30), LocalTime.of(23, 30)},
                new double[]{2, 5, 2});

        //Assert
        assertNotNull(dagligSkaevTC1);
        assertEquals(dagligSkaevTC1, patient.getOrdinationer().get(0));
        assertEquals(laegemiddel, dagligSkaevTC1.getLaegemiddel());
        assertEquals(3, dagligSkaevTC1.getDoser().size());
        try {
            controller.opretDagligSkaevOrdination(
                    LocalDate.of(2024, 12, 24),
                    LocalDate.of(2024, 12, 12),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(11, 30), LocalTime.of(17, 30), LocalTime.of(23, 30)},
                    new double[]{2, 5, 2});
        } catch (Exception exception) {
            assertTrue(exception.getMessage().contains("Start dato skal være før slut dato!"));
        }

        try {
            controller.opretDagligSkaevOrdination(
                    LocalDate.of(2024, 12, 12),
                    LocalDate.of(2024, 12, 24),
                    patient,
                    laegemiddel,
                    new LocalTime[]{LocalTime.of(11, 30), LocalTime.of(17, 30)},
                    new double[]{2, 5, 2});
        } catch (Exception exception) {
            assertTrue(exception.getMessage().contains("Det skal være én dosis for hvert klokkeslet"));
        }
    }

    @Test
    void anbefaletDosisPrDoegn() {
        //Arrange
        Patient patient5Kg = new Patient("123456-7890", "Fornavn Efternavn", 5);
        Patient patient25Kg = new Patient("123456-7890", "Fornavn Efternavn", 25);
        Patient patient50Kg = new Patient("123456-7890", "Fornavn Efternavn", 50);
        Patient patient120Kg = new Patient("123456-7890", "Fornavn Efternavn", 120);
        Patient patient150Kg = new Patient("123456-7890", "Fornavn Efternavn", 150);
        Laegemiddel laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        Controller controller = Controller.getController();

        //Act
        double anbefaletDosis5Kg = controller.anbefaletDosisPrDoegn(patient5Kg, laegemiddel);
        double anbefaletDosis25Kg = controller.anbefaletDosisPrDoegn(patient25Kg, laegemiddel);
        double anbefaletDosis50Kg = controller.anbefaletDosisPrDoegn(patient50Kg, laegemiddel);
        double anbefaletDosis120Kg = controller.anbefaletDosisPrDoegn(patient120Kg, laegemiddel);
        double anbefaletDosis150Kg = controller.anbefaletDosisPrDoegn(patient150Kg, laegemiddel);

        //Assert
        assertEquals(0.5, anbefaletDosis5Kg);
        assertEquals(5, anbefaletDosis25Kg);
        assertEquals(10, anbefaletDosis50Kg);
        assertEquals(60, anbefaletDosis120Kg);
        assertEquals(75, anbefaletDosis150Kg);
    }
}