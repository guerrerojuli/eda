import java.lang.reflect.Array;
import java.util.Arrays;

public class IndexWithDuplicates<T extends Comparable<? super T>> implements IndexParametricService<T> {
  private static final int CHUNK_SIZE = 10;
  private T[] elements;
  private int size;

  public IndexWithDuplicates(Class<T> className) {
    this.elements = (T[]) Array.newInstance(className, CHUNK_SIZE);
  }

  private int getClosestIndex(T key) {
    int left = 0;
    int right = size - 1;
    while (left <= right) {
      int mid = (left + right) / 2;
      if (elements[mid].compareTo(key) == 0) {
        // Si encontramos una ocurrencia, buscamos la primera ocurrencia
        while (mid > 0 && elements[mid - 1] == key) {
          mid--;
        }
        return mid;
      }
      if (elements[mid].compareTo(key) < 0) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    return left;
  }

  @Override
  public void initialize(T[] elements) {
    if (elements == null) {
      throw new IllegalArgumentException("Elements cannot be null");
    }
    this.elements = Arrays.copyOf(elements, elements.length);
    Arrays.sort(this.elements);
    this.size = elements.length;
  }

  @Override
  public boolean search(T key) {
    int index = getClosestIndex(key);
    return index < size && elements[index].equals(key);
  }

  @Override
  public void insert(T key) {
    if (size == elements.length) {
      elements = Arrays.copyOf(elements, elements.length + CHUNK_SIZE);
    }

    int index = getClosestIndex(key);
    if (index < size) {
      for (int i = size; i > index; i--) {
        elements[i] = elements[i - 1];
      }
      elements[index] = key;
      size++;
    }
  }

  @Override
  public void delete(T key) {
    int index = getClosestIndex(key);
    if (index < size && elements[index].equals(key)) {
      for (int i = index; i < size - 1; i++) {
        elements[i] = elements[i + 1];
      }
      size--;
    }
  }

  @Override
  public int occurrences(T key) {
    int index = getClosestIndex(key);
    int count = 0;
    while (index < size && elements[index].equals(key)) {
      count++;
      index++;
    }
    return count;
  }

  @Override
  public T[] range(T leftKey, T rightKey, boolean leftIncluded, boolean rightIncluded) {
    if (leftKey.compareTo(rightKey) > 0) {
      throw new IllegalArgumentException("Left key cannot be greater than right key");
    }

    T[] result = (T[]) Array.newInstance(leftKey.getClass(), size);
    int index = getClosestIndex(leftKey);
    int count = 0;

    while (index < size && (elements[index].compareTo(rightKey) < 0 || (rightIncluded && elements[index].equals(rightKey)))) {
      if (elements[index].compareTo(leftKey) > 0 || (leftIncluded && elements[index].equals(leftKey))) {
        result[count++] = elements[index];
      }
      index++;
    }

    return Arrays.copyOf(result, count);
  }

  @Override
  public void sortedPrint() {
    System.out.println(Arrays.toString(elements));
  }

  @Override
  public T getMax() {
    if (size == 0) {
      throw new RuntimeException("Index is empty");
    }
    return elements[size - 1];
  }

  @Override
  public T getMin() {
    if (size == 0) {
      throw new RuntimeException("Index is empty");
    }
    return elements[0];
  }
}
