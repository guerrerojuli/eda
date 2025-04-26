public class Quicksort {
  public static void sort(int[] array) {
    sort(array, array.length - 1);
  }

  public static void sort(int[] array, int n) {
    helper(array, 0, n);
  }

  private static void helper(int[] array, int left, int right) {
    if (right <= left) {
      return;
    }

    int pivot = array[left];

    // Excluimos al pivot de la particion
    swap(array, left, right);

    int partitionIndex = partition(array, left, right - 1, pivot);
    swap(array, partitionIndex, right);

    helper(array, left, partitionIndex - 1);
    helper(array, partitionIndex + 1, right);
  }

  private static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  private static int partition(int[] array, int left, int right, int pivot) {
    while (left <= right) {
      while (array[left] < pivot) {
        left++;
      }

      while (array[right] > pivot) {
        right--;
      }

      if (left <= right) {
        swap(array, left, right);
        left++;
        right--;
      }
    }

    return left;
  }
}
