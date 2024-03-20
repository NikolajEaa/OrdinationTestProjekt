package controller;

import ordination.Laegemiddel;
import ordination.PN;
import ordination.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void opretPNOrdination() {
        Controller controller = Controller.getController();
        Patient patient = controller.opretPatient("12","Bent",78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre",0.1,0.2,0.5,"Styk");
        PN pn = controller.opretPNOrdination(LocalDate.of(2024,12,12),LocalDate.of(2024,12,24),patient,laegemiddel,2);

        assertNotNull(pn);

        LocalDate actualStartDato = pn.getStartDen();
        LocalDate exptectedStartDato = LocalDate.of(2024,12,12);
        assertEquals(exptectedStartDato,actualStartDato);

        LocalDate actualSlutDato = pn.getSlutDen();
        LocalDate exptectedSlutDato = LocalDate.of(2024,12,24);
        assertEquals(exptectedSlutDato,actualSlutDato);

        PN actualPn = (PN) patient.getOrdinationer().get(0);
        PN expectedPN = pn;
        assertEquals(expectedPN,actualPn);

        Laegemiddel actualLaegemiddel = pn.getLaegemiddel();
        Laegemiddel expectedLaemiddel = laegemiddel;
        assertEquals(expectedLaemiddel,actualLaegemiddel);
    }

    @Test
    void opretPNOrdinationSlutFørStart() {
        Controller controller = Controller.getController();
        Patient patient = controller.opretPatient("12","Bent",78);
        Laegemiddel laegemiddel = controller.opretLaegemiddel("Acetylsalicylsyre",0.1,0.2,0.5,"Styk");
        PN pn = controller.opretPNOrdination(LocalDate.of(2024,12,24),LocalDate.of(2024,12,22),patient,laegemiddel,2);


    }
}