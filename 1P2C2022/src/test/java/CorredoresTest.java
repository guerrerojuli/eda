import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CorredoresTest {

    @Test
    public void testTiemposEntre() {
        Corredores c = new Corredores();
        Pedido[] pedidos = new Pedido[] {
            new Pedido(200, 240),
            new Pedido(180, 210),
            new Pedido(220, 280),
            new Pedido(0, 200),
            new Pedido(290, 10000)
        };
        int[] tiempos = new int[] {
            192,
            200,
            210,
            221,
            229,
            232,
            240,
            240,
            243,
            247,
            280,
            285
        };
        int[] expected = new int[] {7, 3, 8, 2, 0};
        int[] actual = c.tiemposEntre(tiempos, pedidos);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyPedidos() {
        Corredores c = new Corredores();
        Pedido[] pedidos = new Pedido[] { };  // empty pedidos array
        int[] tiempos = new int[] {192, 200, 210}; // arbitrary tiempos
        int[] expected = new int[] { };
        int[] actual = c.tiemposEntre(tiempos, pedidos);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyTiempos() {
        Corredores c = new Corredores();
        Pedido[] pedidos = new Pedido[] {
            new Pedido(100, 200),
            new Pedido(150, 250)
        };
        int[] tiempos = new int[] { };  // empty tiempos array, so no time falls in any range
        int[] expected = new int[] {0, 0};
        int[] actual = c.tiemposEntre(tiempos, pedidos);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPedidoOutsideRange() {
        Corredores c = new Corredores();
        int[] tiempos = new int[] {100, 200, 300, 400};
        Pedido[] pedidos = new Pedido[] {
            new Pedido(450, 500), // completely above the upper bound
            new Pedido(0, 50)     // completely below the lower bound
        };
        int[] expected = new int[] {0, 0};
        int[] actual = c.tiemposEntre(tiempos, pedidos);
        assertArrayEquals(expected, actual);
    }
}
