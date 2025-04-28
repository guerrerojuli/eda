/**
 * Implementación del algoritmo Quick Sort.
 * Quick Sort es un algoritmo de divide y vencerás que funciona seleccionando un elemento 'pivote'
 * y particionando el array alrededor del pivote, de manera que los elementos menores al pivote
 * quedan antes y los mayores quedan después.
 */
public class Quicksort {
  /**
   * Método público para ordenar un array usando Quick Sort.
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

    // Elige el elemento más a la izquierda como pivote
    int pivot = array[left];

    // Mueve temporalmente el pivote al final del array
    swap(array, left, right);

    // Particiona el array y obtiene la posición final del pivote
    int partitionIndex = partition(array, left, right - 1, pivot);
    // Coloca el pivote en su posición final
    swap(array, partitionIndex, right);

    // Ordena recursivamente los elementos antes del pivote
    helper(array, left, partitionIndex - 1);
    // Ordena recursivamente los elementos después del pivote
    helper(array, partitionIndex + 1, right);
  }

  /**
   * Intercambia dos elementos en el array.
   * @param array El array que contiene los elementos a intercambiar
   * @param i Índice del primer elemento
   * @param j Índice del segundo elemento
   */
  private static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Particiona el array alrededor del valor del pivote.
   * @param array El array a particionar
   * @param left El índice más a la izquierda del subarray a particionar
   * @param right El índice más a la derecha del subarray a particionar
   * @param pivot El valor del pivote
   * @return La posición final del pivote
   */
  private static int partition(int[] array, int left, int right, int pivot) {
    while (left <= right) {
      // Encuentra el primer elemento desde la izquierda que es mayor o igual al pivote
      while (array[left] < pivot) {
        left++;
      }

      // Encuentra el primer elemento desde la derecha que es menor o igual al pivote
      while (array[right] > pivot) {
        right--;
      }

      // Si los punteros no se han cruzado, intercambia los elementos
      if (left <= right) {
        swap(array, left, right);
        left++;
        right--;
      }
    }

    // Retorna la posición donde debe colocarse el pivote
    return left;
  }
}
