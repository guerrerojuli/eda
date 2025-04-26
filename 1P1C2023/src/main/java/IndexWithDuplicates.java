import java.util.Arrays;

public class IndexWithDuplicates<E extends Comparable<E> > {

    private final int chunk_size = 5;
    private E [] m_idx;
    private int m_size;
    @SuppressWarnings("unchecked")
    public IndexWithDuplicates(){
        m_idx = (E[]) new Comparable [chunk_size];
    }

    public void initialize(E[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("elements cannot be null");
        }
        for ( E e : elements )
            insert(e);
    }

    private void grow(){
        if (m_size < m_idx.length)
            return;
        m_idx = Arrays.copyOf(m_idx, m_idx.length + chunk_size );
    }

    public void insert(E key) {
        grow();

        int position = 0;
        for ( position = 0; position < m_size && m_idx[position].compareTo( key ) < 0; ++position);

        for (int i = m_size; i > position; --i)
            m_idx[i] = m_idx[i - 1];
        m_idx[position] = key;
        ++m_size;
    }

    private int getClosestIndex(E key) {
        int left = 0;
        int right = m_size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (m_idx[mid].compareTo(key) == 0) {
                // Si encontramos una ocurrencia, buscamos la primera ocurrencia
                while (mid > 0 && m_idx[mid - 1] == key) {
                    mid--;
                }
                return mid;
            }
            if (m_idx[mid].compareTo(key) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    void repeatedValues( E[] values, SimpleLinkedList<E> repeatedLst, SimpleLinkedList<E> singleLst, SimpleLinkedList<E> notIndexedLst )
    {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null");
        }
        if (repeatedLst == null || singleLst == null || notIndexedLst == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }

        for ( E e : values ) {
            int index = getClosestIndex(e);
            if (index < m_size && m_idx[index].equals(e)) {
                if (index < m_size - 1 && m_idx[index + 1].equals(e)) {
                    repeatedLst.insert(e);
                } else {
                    singleLst.insert(e);
                }
            } else {
                notIndexedLst.insert(e);
            }
        }
    }


    public static void main(String[] args) {
        IndexWithDuplicates<Integer> idx = new IndexWithDuplicates<>();
        idx.initialize(  new Integer[] {100, 50, 30, 50, 80, 10, 100, 30, 20, 138} );

        SimpleLinkedList<Integer> repeatedLst = new SimpleLinkedList();
        SimpleLinkedList<Integer> singleLst  = new SimpleLinkedList();
        SimpleLinkedList<Integer> notIndexedLst  = new SimpleLinkedList();
        idx.repeatedValues( new Integer[] { 100, 70, 40, 120, 33, 80, 10, 50 }, repeatedLst, singleLst, notIndexedLst );

        System.out.println("Repeated Values");
        repeatedLst.dump();

        System.out.println("Single Values");
        singleLst.dump();

        System.out.println("Non Indexed Values");
        notIndexedLst.dump();
    }


}
