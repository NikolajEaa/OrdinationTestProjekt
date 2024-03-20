package ordination;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PNTest {


    @Test
    void givDosisGyldige() {
        PN pn = new PN(LocalDate.of(2024,03,18),LocalDate.of(2024,03,24),56);
        boolean actual = pn.givDosis(LocalDate.of(2024,03,18));
        LocalDate actualLocalDate = pn.getGivetDosisTidspunkt().get(0);

        LocalDate expectedTime = LocalDate.of(2024,03,18);

        assertEquals(actual,true);
        assertEquals(actualLocalDate,expectedTime);
    }

    @Test
    void givDosisUgyldige() {
        PN pn = new PN(LocalDate.of(2024,03,18),LocalDate.of(2024,03,24),56);
        boolean actual = pn.givDosis(LocalDate.of(2024,03,17));

        int actualSize = pn.getGivetDosisTidspunkt().size();
        int expectedSize = 0;

        assertEquals(actual,false);
        assertEquals(actualSize,expectedSize);
    }

    @Test
    void givDosisUgyldigeEfterSidsteDag() {
        PN pn = new PN(LocalDate.of(2024,03,18),LocalDate.of(2024,03,24),56);
        boolean actual = pn.givDosis(LocalDate.of(2024,03,25));

        int actualSize = pn.getGivetDosisTidspunkt().size();
        int expectedSize = 0;

        assertEquals(actual,false);
        assertEquals(actualSize,expectedSize);
    }

    @Test
    void doegnDosis() {
        PN pn = new PN(LocalDate.of(2024,03,01),LocalDate.of(2024,03,21),10);
        pn.givDosis(LocalDate.of(2024,03,02));
        pn.givDosis(LocalDate.of(2024,03,04));
        pn.givDosis(LocalDate.of(2024,03,12));

        double actual = pn.doegnDosis();
        double expected = 2.7;

        assertEquals(expected,actual,0.1);
    }
    @Test
    void doegnDosisSortering() {
        PN pn = new PN(LocalDate.of(2024,03,01),LocalDate.of(2024,03,21),10);
        pn.givDosis(LocalDate.of(2024,03,12));
        pn.givDosis(LocalDate.of(2024,03,02));
        pn.givDosis(LocalDate.of(2024,03,04));

        double actual = pn.doegnDosis();
        double expected = 2.7;

        assertEquals(expected,actual,0.1);
    }

    @Test
    void samletDosis() {
        PN pn = new PN(LocalDate.of(2024,03,18),LocalDate.of(2024,03,24),10);

        int actual = pn.getAntalGangeGivet() * (int)pn.getAntalEnheder();
        int expected = 0;
        assertEquals(expected,actual);
    }
    @Test
    void doegnDosisIngenDosisGivet() {
        PN pn = new PN(LocalDate.of(2024,03,01),LocalDate.of(2024,03,21),10);

        double actual = pn.doegnDosis();
        double expected = 0;

        assertEquals(actual,expected);
    }
    @Test
    void samletDosisTiInputs() {
        PN pn = new PN(LocalDate.of(2024,03,18),LocalDate.of(2024,03,24),10);
        for (int i = 0; i < 10; i++) {
            pn.givDosis(LocalDate.of(2024,03,20));
        }

        int actual = pn.getAntalGangeGivet() * (int)pn.getAntalEnheder();
        int expected = 100;
        assertEquals(expected,actual);
    }
}