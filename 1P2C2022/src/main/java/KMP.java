import java.util.ArrayList;

public class KMP {
    /**
     * Preprocesses the query to create the longest prefix suffix (lps) array
     * @param query The query to preprocess
     * @return The lps array
     */
    private static <T> int[] nextComputation(T[] query) {
        int[] lps = new int[query.length];
        int len = 0; // Length of the previous longest prefix suffix
        int i = 1;
        
        // lps[0] is always 0
        lps[0] = 0;
        
        // Loop to calculate lps[i] for i = 1 to pattern.length - 1
        while (i < query.length) {
            if (query[i].equals(query[len])) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        
        return lps;
    }

    
    /**
     * Searches for the query in the target using KMP algorithm
     * @param target The target to search in
     * @param query The query to search for
     * @return The index of the first occurrence of the query, or -1 if not found
     */
    public static <T> int indexOf(T[] query, T[] target) {
        if (query.length > target.length) return -1;
        if (query.length == 0) return 0;
        if (target.length == 0) return -1;
        
        int[] lps = nextComputation(query);
        int i = 0; // Index for text
        int j = 0; // Index for pattern
        
        while (i < target.length) {
            if (query[j].equals(target[i])) {
                i++;
                j++;
            }
            
            if (j == query.length) {
                return i - j; // Pattern found at index i-j
            } else if (i < target.length && !query[j].equals(target[i])) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        
        return -1; // Pattern not found
    }

    /**
     * Searches for all occurrences of the pattern in the text using KMP algorithm
     * @param query The query to search for
     * @param target The target to search in
     * @return An array of all the indices of the query in the target
     */
    public static <T> ArrayList<Integer> findAll(T[] query, T[] target) {
        ArrayList<Integer> occurrences = new ArrayList<>();
        if (query.length > target.length) return occurrences;
        if (query.length == 0) {
            occurrences.add(0);
            return occurrences;
        }
        if (target.length == 0) return occurrences;
        
        int[] lps = nextComputation(query);
        int i = 0; // Index for target
        int j = 0; // Index for query
        
        while (i < target.length) {
            if (query[j].equals(target[i])) {
                i++;
                j++;
            }
            
            if (j == query.length) {
                occurrences.add(i - j); // Query found at index i-j
                j = lps[j - 1];
            } else if (i < target.length && !query[j].equals(target[i])) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        
        return occurrences;
    }

    // Convenience methods for char arrays
    public static int indexOf(char[] query, char[] target) {
        Character[] queryObj = new Character[query.length];
        Character[] targetObj = new Character[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return indexOf(queryObj, targetObj);
    }

    public static ArrayList<Integer> findAll(char[] query, char[] target) {
        Character[] queryObj = new Character[query.length];
        Character[] targetObj = new Character[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return findAll(queryObj, targetObj);
    }

    public static int indexOf(int[] query, int[] target) {
        Integer[] queryObj = new Integer[query.length];
        Integer[] targetObj = new Integer[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return indexOf(queryObj, targetObj);
    }

    public static ArrayList<Integer> findAll(int[] query, int[] target) {
        Integer[] queryObj = new Integer[query.length];
        Integer[] targetObj = new Integer[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return findAll(queryObj, targetObj);
    }
}
