import java.util.HashMap;
import java.util.Map;

public class AlgoReimpl {
    
    // Returns the maximum frequency using the original quadratic algorithm.
    // Explanation: Iterates pairwise comparing elements.
    //
    // Complexity:
    // Temporal: O(n^2)
    // Espacial: O(1)
    public static int unalgoritmo(int[] array) {
        // ...existing code...
        int i = 0;
        int j = 0;
        int m = 0;
        int c = 0;
        while (i < array.length) {
            if (array[i] == array[j]) {
                c++;
            }
            j++;
            if (j >= array.length) {
                if (c > m) {
                    m = c;
                }
                c = 0;
                i++;
                j = i;
            }
        }
        return m;
    }
    
    // Returns the maximum frequency using a HashMap for counting.
    // Explanation: It counts each element's frequency in a single pass.
    //
    // Complexity:
    // Temporal: O(n) on average, as it traverses the array once.
    // Espacial: O(n), to store frequencies for each unique element.
    public static int mialgoritmo(int[] array) {
        // ...existing code...
        Map<Integer, Integer> freq = new HashMap<>();
        int maxFreq = 0;
        for (int num : array) {
            int count = freq.getOrDefault(num, 0) + 1;
            freq.put(num, count);
            if (count > maxFreq) {
                maxFreq = count;
            }
        }
        return maxFreq;
    }
    
    // Optionally, add a main method for quick testing.
    public static void main(String[] args) {
        int[] testArray = {1, 2, 2, 3, 2, 1};
        System.out.println("unalgoritmo: " + unalgoritmo(testArray)); // expected 3
        System.out.println("mialgoritmo: " + mialgoritmo(testArray)); // expected 3
    }
}
