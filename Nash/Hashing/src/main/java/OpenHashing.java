import java.util.LinkedList;
import java.util.function.Function;

public class OpenHashing<K, V> implements IndexParametricService<K, V>{
    final private int initialLookupSize = 10;
    final private double threshold = 0.75;
    private int size = 0;

    // estática. No crece. Espacio suficiente...
    @SuppressWarnings({"unchecked"})
    private LinkedList<Slot<K, V>>[] lookup = new LinkedList[initialLookupSize];

    private Function<? super K, Integer> prehash;

    public OpenHashing(Function<? super K, Integer> mappingFn) {
        if (mappingFn == null)
            throw new RuntimeException("fn not provided");

        prehash = mappingFn;
    }

    private int hash(K key) {
        if (key == null)
            throw new IllegalArgumentException("key cannot be null");

        return prehash.apply(key) % lookup.length;
    }

    private Slot<K, V> getSlot(K key) {
        if (key == null)
            return null;

        for (Slot<K, V> node : lookup[hash(key)]) {
            if (node.key.equals(key))
                return node;
        }

        return null;
    }

    private void checkData(K key, V data){
        if (key == null || data == null) {
            String msg= String.format("inserting or updating (%s,%s). ", key, data);
            if (key==null)
                msg+= "Key cannot be null. ";

            if (data==null)
                msg+= "Data cannot be null.";

            throw new IllegalArgumentException(msg);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkSpace(){
        if ((double) size / lookup.length > threshold){
            LinkedList<Slot<K, V>>[] oldLookup = lookup;

            lookup = new LinkedList[oldLookup.length * 2];

            size = 0;

            for (LinkedList<Slot<K, V>> nodes : oldLookup) {
                if (nodes != null) {
                    for (Slot<K, V> slot : nodes)
                        insertOrUpdate(slot.key, slot.value);
                }
            }
        }
    }


    @Override
    public void insertOrUpdate(K key, V data) {

        checkData(key, data);
        checkSpace();

        int newKey = hash(key);

        if (lookup[newKey % lookup.length] == null)  // Creamos zona de overflow
            lookup[newKey % lookup.length] = new LinkedList<>();

        Slot<K, V> slot = getSlot(key);

        if (slot != null)
            slot.value = data;
        else { // no encontro el valor en la tabla
            lookup[newKey % lookup.length].addLast(new Slot<>(key, data));
            size++;
        }

    }

    @Override
    public V find(K key) {
        if (key == null)
            return null;

        int i = hash(key);

        for (Slot<K, V> slot : lookup[i % lookup.length]) {
            if (slot.key.equals(key))
                return slot.value;
        }

        return null;
    }

    @Override
    public boolean remove(K key) {
        if (key == null)
            return false;

        int i = hash(key);

        if (lookup[i % lookup.length] == null) // no existia la zona de overflow
            return false;

        Slot<K, V> slot = getSlot(key);
        if (slot == null)
            return false;

        lookup[i % lookup.length].remove(slot);
        size--;

        // Si queda vacío lo dejamos en null
        if (lookup[i % lookup.length].isEmpty())
            lookup[i % lookup.length] = null;

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void dump() {
        for (int rec = 0; rec < lookup.length; rec++) {
            if (lookup[rec] == null)
                System.out.printf("slot %d is empty%n", rec);
            else {
                System.out.printf("slot %d: ", rec);
                for (Slot<K, V> node : lookup[rec]) {
                    System.out.printf("[%s , %s] ", node.key, node.value);
                }
                System.out.println();
            }
        }
    }

    static private final class Slot<K, V>	{
        private final K key;
        private V value;

        private Slot(K theKey, V theValue){
            key= theKey;
            value= theValue;
        }

        public String toString() {
            return String.format("(key=%s, value=%s)", key, value );
        }
    }

}
