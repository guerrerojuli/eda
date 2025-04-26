import java.lang.reflect.Array;

public class PCaso1<T> {

		private Object[] arreglo;

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
			PCaso1<Number> auxi= new PCaso1<>();
			auxi.initialize(5);
			
			auxi.setElement(2, 10);
			auxi.setElement(4, 90);
			
			for(int rec= 0; rec < 5; rec++)
				System.out.println ( auxi.getElement(rec) );
			
			System.out.println();
			
			PCaso1<String> auxi2= new PCaso1<>();
			auxi2.initialize(3);
			
			auxi2.setElement(1, "hola");
			
			for(int rec= 0; rec < 3; rec++)
				System.out.println ( auxi2.getElement(rec) );
		}
}
