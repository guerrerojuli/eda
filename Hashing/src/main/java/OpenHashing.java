import java.util.function.Function;

public class OpenHashing<K, V> implements IndexParametricService<K, V> {
    final private int initialLookupSize = 10;
    private static final double MAX_LOAD_FACTOR = 0.75;
    private Node<K, V>[] Lookup;
    private int occupiedCount = 0;
    private final Function<? super K, Integer> prehash;

    public OpenHashing(Function<? super K, Integer> mappingFn) {
        if (mappingFn == null)
            throw new RuntimeException("fn not provided");
        
        prehash = mappingFn;
        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newLookup = (Node<K, V>[]) new Node[initialLookupSize];
        Lookup = newLookup;
    }

    // Time Complexity: O(1)
    private int hash(K key) {
        if (key == null)
            throw new IllegalArgumentException("key cannot be null");
        
        return prehash.apply(key) % Lookup.length;
    }

    // Time Complexity: O(n) where n is the number of elements
    private void rehash() {
        Node<K, V>[] oldLookup = Lookup;
        int newSize = Lookup.length * 2;
        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newLookup = (Node<K, V>[]) new Node[newSize];
        
        // Reset count and update Lookup reference
        occupiedCount = 0;
        Lookup = newLookup;

        // Reinsert all elements
        for (Node<K, V> node : oldLookup) {
            while (node != null) {
                insertOrUpdate(node.key, node.value);
                node = node.next;
            }
        }
    }

    // Time Complexity: 
    // - Average case: O(1 + α) where α is the load factor
    // - Worst case: O(n) when all elements hash to the same bucket
    @Override
    public void insertOrUpdate(K key, V data) {
        if (key == null || data == null) {
            String msg = String.format("inserting or updating (%s,%s). ", key, data);
            if (key == null)
                msg += "Key cannot be null. ";
            if (data == null)
                msg += "Data cannot be null.";
            throw new IllegalArgumentException(msg);
        }

        int idx = hash(key);
        Node<K, V> current = Lookup[idx];

        // Check if key already exists in the chain
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = data;
                return;
            }
            current = current.next;
        }

        // Key doesn't exist, insert at the beginning of the chain
        Node<K, V> newNode = new Node<>(key, data);
        newNode.next = Lookup[idx];
        Lookup[idx] = newNode;
        occupiedCount++;

        // Check if rehashing is needed
        if ((double) occupiedCount / Lookup.length > MAX_LOAD_FACTOR) {
            rehash();
        }
    }

    // Time Complexity:
    // - Average case: O(1 + α) where α is the load factor
    // - Worst case: O(n) when all elements hash to the same bucket
    @Override
    public V find(K key) {
        if (key == null)
            return null;

        int idx = hash(key);
        Node<K, V> current = Lookup[idx];

        while (current != null) {
            if (current.key.equals(key))
                return current.value;
            current = current.next;
        }
        return null;
    }

    // Time Complexity:
    // - Average case: O(1 + α) where α is the load factor
    // - Worst case: O(n) when all elements hash to the same bucket
    @Override
    public boolean remove(K key) {
        if (key == null)
            return false;

        int idx = hash(key);
        Node<K, V> current = Lookup[idx];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    Lookup[idx] = current.next;
                } else {
                    prev.next = current.next;
                }
                occupiedCount--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // Time Complexity: O(1)
    @Override
    public int size() {
        return occupiedCount;
    }

    // Time Complexity: O(m + n) where m is the table size and n is the number of elements
    @Override
    public void dump() {
        for (int rec = 0; rec < Lookup.length; rec++) {
            System.out.printf("slot %d: ", rec);
            Node<K, V> current = Lookup[rec];
            if (current == null) {
                System.out.println("is empty");
                continue;
            }
            while (current != null) {
                System.out.printf("(key=%s, value=%s) -> ", current.key, current.value);
                current = current.next;
            }
            System.out.println("null");
        }
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        private Node(K theKey, V theValue) {
            key = theKey;
            value = theValue;
            next = null;
        }

        public String toString() {
            return String.format("(key=%s, value=%s)", key, value);
        }
    }

    public static void main(String[] args) {
        OpenHashing<Integer, String> myHash = new OpenHashing<>(f -> f);
        myHash.insertOrUpdate(55, "Ana");
        myHash.insertOrUpdate(44, "Juan");
        myHash.insertOrUpdate(18, "Paula");
        myHash.insertOrUpdate(19, "Lucas");
        myHash.insertOrUpdate(21, "Sol");
        myHash.dump();
    }
} 