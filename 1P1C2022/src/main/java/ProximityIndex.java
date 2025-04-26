public class ProximityIndex {
    private String[] elements;
    private int size = 0;

    public void initialize(String[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("elements no puede ser null");
        }
        
        for(int rec= 0; rec < elements.length-1; rec++) {
        	if (elements[rec].compareTo(elements[rec+1]) >= 0)
                throw new IllegalArgumentException("hay repetidos o no está ordenado");
        }
        
        this.elements = elements;
        this.size = elements.length;

     }


    public String search(String element, int distance) {
        if (element == null) {
            throw new IllegalArgumentException("element no puede ser null");
        }

        int index = getClosestIndex(element);
        if (index >= size || !elements[index].equals(element)) {
            return null;
        }

        int retIndex = index + distance;
        retIndex = retIndex % size;
        retIndex = (size + retIndex) % size;
        return elements[retIndex];
    }

    private int getClosestIndex(String key) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (elements[mid].compareTo(key) == 0) {
                // Si encontramos una ocurrencia, buscamos la primera ocurrencia
                while (mid > 0 && elements[mid - 1] == key) {
                    mid--;
                }
                return mid;
            }
            if (elements[mid].compareTo(key) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

}
