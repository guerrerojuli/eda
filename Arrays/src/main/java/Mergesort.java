public class Mergesort {
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

    int middle = (left + right) / 2;

    helper(array, left, middle);
    helper(array, middle + 1, right);

    merge(array, left, middle, right);
  }

  private static void merge(int[] array, int left, int middle, int right) {
    int[] temp = new int[right - left + 1];
    int i = left;
    int j = middle + 1;
    int k = 0;

    while (i <= middle && j <= right) {
      if (array[i] <= array[j]) {
        temp[k++] = array[i++];
      } else {
        temp[k++] = array[j++];
      }
    }

    while (i <= middle) {
      temp[k++] = array[i++];
    }

    while (j <= right) {
      temp[k++] = array[j++];
    }

    for (i = 0; i < temp.length; i++) {
      array[left + i] = temp[i];
    }
  }
}
