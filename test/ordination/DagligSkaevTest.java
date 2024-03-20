package ordination;

import controller.Controller;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DagligSkaevTest {

    @Test
    void opretDagligSkaev() {
        // Arrange

        //Act
        DagligSkaev dagligSkaev = new DagligSkaev(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24));

        //Assert
        assertNotNull(dagligSkaev);
        assertEquals(LocalDate.of(2024, 12, 12), dagligSkaev.getStartDen());
        assertEquals(LocalDate.of(2024, 12, 24), dagligSkaev.getSlutDen());
    }

    @Test
    void opretDosis() {
        // Arrange
        DagligSkaev dagligSkaev = new DagligSkaev(LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 24));

        //Act
        dagligSkaev.opretDosis(LocalTime.of(16, 30), 4);

        //Assert
        assertNotNull(dagligSkaev.getDoser().get(0));
        assertEquals(LocalTime.of(16, 30), dagligSkaev.getDoser().get(0).getTid());
        assertEquals(4, dagligSkaev.getDoser().get(0).getAntal());
    }

    @Test
    void samletDosis() {
        // Arrange
        Controller controller = Controller.getController();
        Patient patient = new Patient("123456-7890", "Fornavn Efternavn", 78);
        Laegemiddel laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        DagligSkaev dagligSkaev1 = controller.opretDagligSkaevOrdination(
                LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 24),
                patient,
                laegemiddel,
                new LocalTime[]{LocalTime.of(12, 30), LocalTime.of(16, 30), LocalTime.of(18, 30)},
                new double[]{2, 2, 4});

        DagligSkaev dagligSkaev2 = controller.opretDagligSkaevOrdination(
                LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 24),
                patient,
                laegemiddel,
                new LocalTime[]{LocalTime.of(12, 30)},
                new double[]{4});


        //Act
        double samletDosis1 = dagligSkaev1.samletDosis();
        double samletDosis2 = dagligSkaev2.samletDosis();

        //Assert
        assertEquals(104, samletDosis1);
        assertEquals(52, samletDosis2);
    }

    @Test
    void doegnDosis() {
        // Arrange
        Controller controller = Controller.getController();
        Patient patient = new Patient("123456-7890", "Fornavn Efternavn", 78);
        Laegemiddel laegemiddel = new Laegemiddel("Acetylsalicylsyre", 0.1, 0.2, 0.5, "Styk");
        DagligSkaev dagligSkaev1 = controller.opretDagligSkaevOrdination(
                LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 24),
                patient,
                laegemiddel,
                new LocalTime[]{LocalTime.of(12, 30), LocalTime.of(16, 30), LocalTime.of(18, 30)},
                new double[]{2, 2, 4});

        DagligSkaev dagligSkaev2 = controller.opretDagligSkaevOrdination(
                LocalDate.of(2024, 12, 12),
                LocalDate.of(2024, 12, 24),
                patient,
                laegemiddel,
                new LocalTime[]{LocalTime.of(12, 30)},
                new double[]{4});

        //Act
        double døgnDosis1 = dagligSkaev1.doegnDosis();
        double døgnDosis2 = dagligSkaev2.doegnDosis();

        //Assert
        assertEquals(8, døgnDosis1);
        assertEquals(4, døgnDosis2);

    }
}