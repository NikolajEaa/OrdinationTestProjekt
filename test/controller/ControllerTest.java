package controller;

import ordination.DagligFast;
import ordination.Dosis;
import ordination.Laegemiddel;
import ordination.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controller = Controller.getController();
    Patient patient;
    Laegemiddel laegemiddel;

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

        DagligFast dagligFast;
        try {
        dagligFast = controller.opretDagligFastOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, 0, 0, 0, 0);
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Daglig Fast skal have en værdi på en af den pågældende dage"));
        }
    }
    @Test
    void DagligFastUgyldig_TC2() {

        DagligFast dagligFast;
        try {
            dagligFast = controller.opretDagligFastOrdination(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24), patient, laegemiddel, -1, 1, 2, 0);
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Ordinationerne må ikke være negative"));
        }
    }


}