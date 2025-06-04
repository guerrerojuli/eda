
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.function.Function;

public class ClosedHashing<K, V> implements IndexParametricService<K, V> {

	private final int initialLookupSize = 10;
	private final double threshold = 0.75;
	private int size = 0;

	// estática. No crece. Espacio suficiente...
	@SuppressWarnings({"unchecked"})
	private Slot<K,V>[] lookUp = (Slot<K,V>[]) new Slot[initialLookupSize];

	private Function<? super K, Integer> prehash;

	public ClosedHashing(Function<? super K, Integer> mappingFn) {
		if (mappingFn == null)
			throw new RuntimeException("function not provided");

		prehash = mappingFn;
	}

	// ajuste al tamaño de la tabla
	private int hash(K key) {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null");

		return prehash.apply(key) % lookUp.length;
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
	private void checkSpaceNotColision(){
		if ((double) size / lookUp.length > threshold){
			Slot<K, V>[] oldLookup = lookUp;

			lookUp = (Slot<K, V>[]) new Slot[oldLookup.length * 2];

			size = 0;

			for (Slot<K, V> kvSlot : oldLookup) {
				if (kvSlot != null) insertOrUpdate(kvSlot.key, kvSlot.value);
			}
		}
	}

	//NO MANEJA COLISIONES
	public void insertOrUpdate(K key, V data) {

		checkData(key, data);
		checkSpaceNotColision();

		int newKey = hash(key);

		if(lookUp[newKey % lookUp.length] != null) {
			if(lookUp[newKey % lookUp.length].equals(key)) {
				lookUp[newKey % lookUp.length].value = data;
				return;
			}
			else
				throw new IllegalArgumentException("There's another key for slot %d".formatted(newKey % lookUp.length));
		}
		lookUp[newKey % lookUp.length] = new Slot<>(key, data);
	}

	@SuppressWarnings("unchecked")
	private void checkSpaceColision(){
		if ((double) size / lookUp.length > threshold){
			Slot<K, V>[] oldLookup = lookUp;

			lookUp = (Slot<K, V>[]) new Slot[oldLookup.length * 2];

			size = 0;

			for (Slot<K, V> kvSlot : oldLookup) {
				if (kvSlot != null) insertOrUpdateClosedHashing(kvSlot.key, kvSlot.value);
			}
		}
	}

	public void insertOrUpdateClosedHashing(K key, V data) {
		checkData(key, data);

		checkSpaceColision();

		int newKey = hash(key);
		int count = 0;

		//firstDeleted va a guardar el primer lugar libre lógico que encuentra por si, al finalizar de recorrer,
		//no matcheó ninguna key o no encontró null.
		int firstDeleted = -1;

		while(lookUp[newKey % lookUp.length] != null && count < lookUp.length){
			if(lookUp[newKey % lookUp.length].key.equals(key)) {
				lookUp[newKey % lookUp.length].value = data;
				return;
			}
			if (!lookUp[newKey % lookUp.length].isOccupied && firstDeleted == -1)
				firstDeleted = newKey % lookUp.length;
			newKey++;
			count++;
		}

		if(firstDeleted != -1)
			lookUp[firstDeleted] = new Slot<>(key,data);
		else
			lookUp[newKey % lookUp.length] = new Slot<>(key, data);
		size++;
	}

	// find or get
	@Override
	public V find(K key) {
		if (key == null) return null;

		int i = hash(key);
		int count = 0;

		while (lookUp[i % lookUp.length] != null && count < lookUp.length) {
			if (lookUp[i % lookUp.length].key.equals(key))
				return lookUp[i % lookUp.length].value;
			i++;
			count++;
		}
		return null;
	}

	// Chequear si anda bien
	@Override
	public boolean remove(K key) {
		if (key == null)
			return false;

		int newKey = hash(key) % lookUp.length;

		// lo encontre?
		if (lookUp[newKey] == null)
			return false;

		lookUp[newKey] = null;
		size--;
		return true;
	}

	public boolean removeClosedHashing(K key) {
		if (key == null)
			return false;

		int i = hash(key);
		int count = 0;

		while(lookUp[i % lookUp.length] != null && count < lookUp.length){
			if (lookUp[i % lookUp.length].key.equals(key) && lookUp[i % lookUp.length].isOccupied) {
				if (lookUp[(i + 1) % lookUp.length] == null)
					lookUp[i % lookUp.length] = null;
				else
					lookUp[i % lookUp.length].setNotOccupied();
				size--;
				return true;
			}
			i++;
			count++;
		}
		return false;
	}

	@Override
	public void dump()  {
		for(int rec = 0; rec < lookUp.length; rec++) {
			if (lookUp[rec] == null )
 				System.out.println(String.format("slot %d is empty", rec));
			else if(!lookUp[rec].isOccupied) {
				System.out.println(String.format("slot %d is logically empty", rec));
			} else
				System.out.println(String.format("slot %d contains %s",rec, lookUp	[rec]));
		}
	}

	@Override
	public int size() {
		return size;
	}

	static private final class Slot<K, V>	{
		private final K key;
		private V value;
		private boolean isOccupied;
		
		private Slot(K theKey, V theValue){
			key= theKey;
			value= theValue;
			isOccupied = true;
		}
	
		public String toString() {
		 return String.format("(key=%s, value=%s)", key, value );
		}

		public void setOccupied(){
			this.isOccupied = true;
		}

		public void setNotOccupied(){
			this.isOccupied = false;
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		/*
		// Primer Test --> Sin manejo de colisiones
		ClosedHashing<Integer, String> myHash= new ClosedHashing<>(f->f);
		myHash.insertOrUpdate(55, "Ana");
		myHash.insertOrUpdate(44, "Juan");
		myHash.insertOrUpdate(18, "Paula");
		myHash.insertOrUpdate(19, "Lucas");
		myHash.insertOrUpdate(21, "Sol");
		//myTest
		myHash.insertOrUpdate(33, "Guido");
		myHash.insertOrUpdate(22, "Edwin");
		myHash.insertOrUpdate(6, "Zaid");
		myHash.dump();									//DEBERIA TIRAR EXCEPCION
		myHash.insertOrUpdate(15, "Eros");
		//myHash.dump();
		*/

		/*
		// Test ClosedHashing
		ClosedHashing<Integer, String> myHash= new ClosedHashing<>(f->f);
		myHash.insertOrUpdateClosedHashing(3, "Dick");
		myHash.insertOrUpdateClosedHashing(23, "Joe");
		myHash.insertOrUpdateClosedHashing(4, "Sue");
		myHash.insertOrUpdateClosedHashing(15, "Meg");
		myHash.insertOrUpdateClosedHashing(6, "Dick");
		myHash.insertOrUpdateClosedHashing(7, "Joe");
		myHash.insertOrUpdateClosedHashing(8, "Sue");
		myHash.insertOrUpdateClosedHashing(15, "Meg");
		myHash.dump();
		System.out.println("After Deleting:");
		myHash.removeClosedHashing(23);
		myHash.removeClosedHashing(15);
		myHash.dump();
		*/

		/*
		//Con strings como clave
		System.out.println("STRING COMO CLAVE");

		Function<String, Integer> function = s -> {
			int sum = 0;
			for (int i = 0; i < s.length(); i++) {
				sum += (int) s.charAt(i); // Sumar el valor ASCII de cada carácter
			}
			return sum;
		};
		ClosedHashing<String, String> myHash2= new ClosedHashing<>(function);

		String fileName = "amazon-categories30.txt";
		InputStream is = ClosedHashing.class.getClassLoader().getResourceAsStream(fileName);
		InputStreamReader in = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(in);
		String line;

		while(((line = br.readLine()) != null)){
			Scanner scanner = new Scanner(line).useDelimiter("#");
			String title = scanner.next();
			myHash2.insertOrUpdateClosedHashing(title, line);
		}
		myHash2.dump();
		*/
	}

}