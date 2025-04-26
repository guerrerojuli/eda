import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyTimerTest {

    private MyTimer myTimer;

    @BeforeEach
    void initTimer() {
        myTimer = new MyTimer(0);
    }

    @Test
    void getDurationTest() {

        long expected = 2000;

        myTimer.stop(2000);
        long result = myTimer.getElapsedTime();
        assertEquals(result, expected, "El tiempo transcurrido no es el correcto");
    }

    @Test
    void getDaysTest() {
        long expected = 2;

        myTimer.stop(172800000); // Ms de 2 dias
        long result = myTimer.getDays();
        assertEquals(result, expected, "Los dias transcurridos no son correctos");
    }

    @Test
    void getHsTest() {
        long expected = 3;

        myTimer.stop(10800000); // Ms de 3 hs
        long result = myTimer.getHs();
        assertEquals(result, expected, "Los dias transcurridos no son correctos");
    }
}