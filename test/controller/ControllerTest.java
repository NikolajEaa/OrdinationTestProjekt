package controller;

import ordination.DagligSkaev;
import ordination.Laegemiddel;
import ordination.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void opretPNOrdination() {

    }

    @Test
    void opretDagligFastOrdination() {
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
        } catch (Exception exception){
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
        } catch (Exception exception){
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