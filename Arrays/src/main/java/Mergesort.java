/**
 * Implementación del algoritmo Merge Sort.
 * Merge Sort es un algoritmo de divide y vencerás que divide el array de entrada en dos mitades,
 * ordena cada mitad recursivamente y luego combina las mitades ordenadas.
 */
public class Mergesort {
  /**
   * Método público para ordenar un array usando Merge Sort.
   * @param array El array a ordenar
   */
  public static void sort(int[] array) {
    sort(array, array.length - 1);
  }

  /**
   * Método sobrecargado que toma el array y su último índice.
   * @param array El array a ordenar
   * @param n El último índice del array
   */
  public static void sort(int[] array, int n) {
    helper(array, 0, n);
  }

  /**
   * Método auxiliar recursivo que implementa la estrategia de divide y vencerás.
   * @param array El array a ordenar
   * @param left El índice más a la izquierda del subarray actual
   * @param right El índice más a la derecha del subarray actual
   */
  private static void helper(int[] array, int left, int right) {
    // Caso base: si el subarray tiene 0 o 1 elementos, ya está ordenado
    if (right <= left) {
      return;
    }

    // Calcula el punto medio para dividir el array en dos mitades
    int middle = (left + right) / 2;

    // Ordena recursivamente la mitad izquierda
    helper(array, left, middle);
    // Ordena recursivamente la mitad derecha
    helper(array, middle + 1, right);

    // Combina las dos mitades ordenadas
    merge(array, left, middle, right);
  }

  /**
   * Combina dos subarrays ordenados en un único subarray ordenado.
   * @param array El array que contiene los subarrays a combinar
   * @param left El índice más a la izquierda del primer subarray
   * @param middle El índice final del primer subarray y (middle+1) es el inicio del segundo subarray
   * @param right El índice más a la derecha del segundo subarray
   */
  private static void merge(int[] array, int left, int middle, int right) {
    // Crea un array temporal para almacenar el resultado combinado
    int[] temp = new int[right - left + 1];
    int i = left;      // Puntero para el subarray izquierdo
    int j = middle + 1; // Puntero para el subarray derecho
    int k = 0;         // Puntero para el array temporal

    // Combina los dos subarrays comparando elementos
    while (i <= middle && j <= right) {
      if (array[i] <= array[j]) {
        temp[k++] = array[i++]; // Toma el elemento del subarray izquierdo
      } else {
        temp[k++] = array[j++]; // Toma el elemento del subarray derecho
      }
    }

    // Copia los elementos restantes del subarray izquierdo
    while (i <= middle) {
      temp[k++] = array[i++];
    }

    // Copia los elementos restantes del subarray derecho
    while (j <= right) {
      temp[k++] = array[j++];
    }

    // Copia los elementos combinados de vuelta al array original
    for (i = 0; i < temp.length; i++) {
      array[left + i] = temp[i];
    }
  }
}
