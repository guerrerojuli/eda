import java.lang.reflect.Array;

public class PCaso2_Comparable<T extends Comparable<? super T>> {

		private T[] arreglo;

		public void extraMethod(T p) {
			System.out.println(p);
		}
		
		@SuppressWarnings("unchecked")
		public void initialize(int dim, Class<T> theClass) {
			arreglo=(T[]) Array.newInstance(theClass, dim);
		}

		private boolean isValidPos(int pos) {
			return ! (arreglo == null ||  pos < 0  ||  pos > arreglo.length);
		}
		
		public void setElement(int pos, T element) {
			if (! isValidPos(pos) )
				throw new RuntimeException("problema....");
			
			arreglo[pos]= element;
		}

		public T getElement(int pos)
		{
			if (! isValidPos(pos) )
				throw new RuntimeException("problema....");

			return arreglo[pos];
		}

		
		
		public static void main(String[] args) {
			PCaso2_Comparable<Integer> auxi= new PCaso2_Comparable<>();
			auxi.initialize(5, Integer.class);
			
			auxi.setElement(2, 10);
			auxi.setElement(4, 90);
			
			for(int rec= 0; rec < 5; rec++)
				System.out.println ( auxi.getElement(rec) );
			
			System.out.println();
			
			PCaso2_Comparable<String> auxi2= new PCaso2_Comparable<>();
			auxi2.initialize(3, String.class);
			
			auxi2.setElement(1, "hola");
			
			for(int rec= 0; rec < 3; rec++)
				System.out.println ( auxi2.getElement(rec) );
			
			auxi2.extraMethod("si");
		}
}
