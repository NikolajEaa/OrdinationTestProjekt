package ordination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class DagligFastTest {
    Patient patient;
    Laegemiddel laegemiddel;
    DagligFast dagligFast;


    @BeforeEach
            void setUp() {
    patient = new Patient("12456-7890", "Fornavn Efternavn", 78);
    laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
    dagligFast = new DagligFast(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24));
    dagligFast.setLaegemiddel(laegemiddel);
    patient.addOrdination(dagligFast);



    }

    @Test
    void DagligFastConstructor_TC1() {

        DagligFast dagligFast = new DagligFast(LocalDate.of(2024, 12, 12),
                                                LocalDate.of(2024, 12, 24));
        LocalDate expected = LocalDate.of(2024, 12, 12);
        LocalDate actual = dagligFast.getStartDen();

        System.out.println("Start Dato");
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        expected = LocalDate.of(2024, 12, 24);
        actual = dagligFast.getSlutDen();

        System.out.println("Slut Dato");
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }
    @Test
    void DagligFastConstructor_TC2() {

        DagligFast dagligFast = new DagligFast(LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 12));
        LocalDate expected = LocalDate.of(2024, 12, 12);
        LocalDate actual = dagligFast.getStartDen();

        System.out.println("Start Dato");
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        expected = LocalDate.of(2024, 12, 12);
        actual = dagligFast.getSlutDen();

        System.out.println("Slut Dato");
        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }

    @Test
    void DagligFastDoegnDosis_TC1() {

        dagligFast.addDosiser(2, 4, 3, 1);

        double expected = 10;
        double actual = dagligFast.doegnDosis();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }
    @Test
    void DagligFastDoegnDosis_TC2() {

        dagligFast.addDosiser(0, 4, 0, 1);

        double expected = 5;
        double actual = dagligFast.doegnDosis();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }
    @Test
    void DagligFastSamletDosis_TC1() {

        dagligFast.addDosiser(2, 4, 3, 1);

        double expected = 130;
        double actual = dagligFast.samletDosis();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }
    @Test
    void DagligFastSamletDosis_TC2() {

        dagligFast.addDosiser(0, 4, 0, 1);

        double expected = 65;
        double actual = dagligFast.samletDosis();

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }
}