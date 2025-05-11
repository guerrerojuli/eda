package jguerrero;

public class Laberinto {

    public static boolean existeCamino(int[][] laberinto, int filaInicio, int columnaInicio, int filaFin, int columnaFin) {
        // Verificar si las coordenadas están dentro de los límites del laberinto
        if (filaInicio < 0 || filaInicio >= laberinto.length || 
            columnaInicio < 0 || columnaInicio >= laberinto[0].length ||
            filaFin < 0 || filaFin >= laberinto.length || 
            columnaFin < 0 || columnaFin >= laberinto[0].length) {
            return false;
        }

        // Verificar si la celda actual es una pared o ya fue visitada
        if (laberinto[filaInicio][columnaInicio] == 1 || laberinto[filaInicio][columnaInicio] == -1) {
            return false;
        }

        // Verificar si llegamos al destino
        if (filaInicio == filaFin && columnaInicio == columnaFin) {
            return true;
        }

        // Marcar la celda actual como visitada
        laberinto[filaInicio][columnaInicio] = -1;

        // Intentar moverse en las cuatro direcciones posibles
        boolean caminoEncontrado = 
            existeCamino(laberinto, filaInicio - 1, columnaInicio, filaFin, columnaFin) || // Arriba
            existeCamino(laberinto, filaInicio + 1, columnaInicio, filaFin, columnaFin) || // Abajo
            existeCamino(laberinto, filaInicio, columnaInicio - 1, filaFin, columnaFin) || // Izquierda
            existeCamino(laberinto, filaInicio, columnaInicio + 1, filaFin, columnaFin);   // Derecha

        return caminoEncontrado;
    }

    public static void main(String[] args) {
        int[][] laberinto = {
                {0, 0, 1, 0},
                {1, 0, 1, 0},
                {0, 0, 0, 0},
                {0, 1, 1, 1}
        };

        boolean existe = existeCamino(laberinto, 0, 0, 3, 0);
        if (existe) {
            System.out.println("Existe un camino en el laberinto.");
        } else {
            System.out.println("No existe un camino en el laberinto.");
        }
        System.out.println("Caminos recorridos:");
        imprimirLaberinto(laberinto);

        int[][] laberintoSinSalida = {
                {0, 0, 1, 0},
                {1, 0, 1, 1},
                {0, 0, 0, 0},
                {0, 1, 1, 1}
        };
        boolean existeSinSalida = existeCamino(laberintoSinSalida, 0, 0, 0, 3);
        if (existeSinSalida) {
            System.out.println("Existe un camino en el laberinto sin salida (¡error!).");
        } else {
            System.out.println("No existe un camino en el laberinto sin salida.");
        }
        System.out.println("Caminos recorridos:");
        imprimirLaberinto(laberintoSinSalida);
    }

    public static void imprimirLaberinto(int[][] laberinto) {
        for (int[] fila : laberinto) {
            for (int celda : fila) {
                System.out.print(celda + " ");
            }
            System.out.println();
        }
    }
}
