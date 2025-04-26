import java.lang.reflect.Array;

public class PCaso1_Comparable<T extends Comparable<? super T>> {

		private Object[] arreglo;
		
		public void extraMethod(T p) {
			System.out.println(p);
		}

		public void initialize(int dim) {
			arreglo=new Object[dim];
		}

		private boolean isValidPos(int pos) {
			return ! (arreglo == null ||  pos < 0  ||  pos > arreglo.length);
		}
		
		public void setElement(int pos, T element) {
			if (! isValidPos(pos) )
				throw new RuntimeException("problema....");
			
			arreglo[pos]= element;
		}

		@SuppressWarnings("unchecked")
		public T getElement(int pos)
		{
			if (! isValidPos(pos) )
				throw new RuntimeException("problema....");

			return (T) arreglo[pos];
		}

		
		
		public static void main(String[] args) {
			PCaso1_Comparable<Integer> auxi= new PCaso1_Comparable<>();
			auxi.initialize(5);
			
			auxi.setElement(2, 10);
			auxi.setElement(4, 90);
			
			for(int rec= 0; rec < 5; rec++)
				System.out.println ( auxi.getElement(rec) );
			
			System.out.println();
			
			PCaso1_Comparable<String> auxi2= new PCaso1_Comparable<>();
			auxi2.initialize(3);
			
			auxi2.setElement(1, "hola");
			
			for(int rec= 0; rec < 3; rec++)
				System.out.println ( auxi2.getElement(rec) );
			
			auxi2.extraMethod("si");
		}
}
