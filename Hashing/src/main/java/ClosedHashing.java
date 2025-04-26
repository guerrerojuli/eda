import java.util.function.Function;

public class ClosedHashing<K, V> implements IndexParametricService<K, V> {
	final private int initialLookupSize = 10;
	private static final double MAX_LOAD_FACTOR = 0.75;
	private int occupiedCount = 0;

	@SuppressWarnings({"unchecked"})
	private Slot<K,V>[] Lookup = (Slot<K,V>[]) new Slot[initialLookupSize];

	private Function<? super K, Integer> prehash;

	public ClosedHashing(Function<? super K, Integer> mappingFn) {
		if (mappingFn == null)
			throw new RuntimeException("fn not provided");

		prehash = mappingFn;
	}

	private int hash(K key) {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null");

		return prehash.apply(key) % Lookup.length;
	}

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
		int start = idx;
		int candidate = -1; // track first logical deletion found
		
		while (true) {
			Slot<K,V> slot = Lookup[idx];
			if (slot == null) { // physical deletion found: empty slot
				if (candidate != -1) {
					idx = candidate; // use candidate from earlier logical deletion
				}
				Lookup[idx] = new Slot<>(key, data);
				occupiedCount++;
				break;
			} else {
				if (!slot.deleted && slot.key.equals(key)) {
					// key exists, update
					slot.value = data;
					return;
				}
				// record first logical deletion encountered
				if (slot.deleted && candidate == -1) {
					candidate = idx;
				}
			}
			idx = (idx + 1) % Lookup.length;
			if (idx == start)
				throw new RuntimeException("Table is full");
		}
		
		if (occupiedCount > Lookup.length * MAX_LOAD_FACTOR) {
			rehash();
		}
	}

	private void rehash() {
		Slot<K, V>[] oldLookup = Lookup;
		int newSize = Lookup.length * 2;
		@SuppressWarnings({"unchecked"})
		Slot<K,V>[] newLookup = (Slot<K,V>[]) new Slot[newSize];

		// Reset count and update Lookup reference.
		occupiedCount = 0;
		Lookup = newLookup;

		for (Slot<K, V> entry : oldLookup) {
			if (entry != null && !entry.deleted) {
				int idx = hash(entry.key);
				while (Lookup[idx] != null) {
					idx = (idx + 1) % Lookup.length;
				}
				Lookup[idx] = new Slot<>(entry.key, entry.value);
				occupiedCount++;
			}
		}
	}

	public V find(K key) {
		if (key == null)
			return null;

		int idx = hash(key);
		int start = idx;
		while (true) {
			Slot<K,V> slot = Lookup[idx];
			if (slot == null)
				return null; // physical deletion reached → not found
			if (!slot.deleted && slot.key.equals(key))
				return slot.value;
			idx = (idx + 1) % Lookup.length;
			if (idx == start)
				return null;
		}      
	}

	public boolean remove(K key) {
		if (key == null)
			return false;
		
		int idx = hash(key);
		int start = idx;
		while (true) {
			Slot<K,V> slot = Lookup[idx];
			if (slot == null)
				return false;
			if (!slot.deleted && slot.key.equals(key)) {
				int nextIdx = (idx + 1) % Lookup.length;
				// If next slot is physically empty, remove slot physically.
				if (Lookup[nextIdx] == null) {
					Lookup[idx] = null;
				} else {
					// Otherwise mark as logically deleted.
					slot.deleted = true;
				}
				occupiedCount--;
				return true;
			}
			idx = (idx + 1) % Lookup.length;
			if (idx == start)
				return false;
		}
	}

	public void dump()  {
		for(int rec = 0; rec < Lookup.length; rec++) {
			if (Lookup[rec] == null)
 				System.out.println(String.format("slot %d is empty", rec));
			else
				System.out.println(String.format("slot %d contains %s",rec, Lookup[rec]));
		}
	}

	public int size() {
		return occupiedCount;
	}

	static private final class Slot<K, V>	{
		private final K key;
		private V value;
		boolean deleted = false; // deletion flag
		
		private Slot(K theKey, V theValue){
			key = theKey;
			value = theValue;
		}

		public String toString() {
		 return String.format("(key=%s, value=%s%s)", key, value, deleted ? ", deleted" : "");
		}
	}

	public static void main(String[] args) {
		ClosedHashing<Integer, String> myHash = new ClosedHashing<>(f -> f);
		myHash.insertOrUpdate(55, "Ana");
		myHash.insertOrUpdate(44, "Juan");
		myHash.insertOrUpdate(18, "Paula");
		myHash.insertOrUpdate(19, "Lucas");
		myHash.insertOrUpdate(21, "Sol");
		myHash.dump();
		
	}
	
/*	
	public static void main(String[] args) {
		ClosedHashing<Integer, String> myHash= new ClosedHashing<>(f->f);
		myHash.insertOrUpdate(55, "Ana");
		myHash.insertOrUpdate(29, "Victor");
		myHash.insertOrUpdate(25, "Tomas");
		myHash.insertOrUpdate(19, "Lucas");
		myHash.insertOrUpdate(21, "Sol");
		myHash.dump();
	}
*/
}

