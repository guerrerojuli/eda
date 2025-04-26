public class Corredores {
    private int getClosestIndex(int key, int[] elements, int size) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (elements[mid] == key) {
                // Si encontramos una ocurrencia, buscamos la primera ocurrencia
                while (mid > 0 && elements[mid - 1] == key) {
                    mid--;
                }
                return mid;
            }
            if (elements[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    public int[] tiemposEntre(int[] tiempos, Pedido[] p) {
        int[] tiemposEntre = new int[p.length];

        for (int i = 0; i < p.length; i++) {
            int desde = p[i].desde;
            int hasta = p[i].hasta;

            int indexDesde = getClosestIndex(desde, tiempos, tiempos.length);
            int indexHasta = getClosestIndex(hasta + 1, tiempos, tiempos.length);

            tiemposEntre[i] = indexHasta - indexDesde;
        }

        return tiemposEntre;
    }
}
