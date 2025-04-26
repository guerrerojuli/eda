public interface IndexParametricService<K, V> {
    // no acepta key ni data nulls=> lanza exception. Si el key está, realizar un update en el valor.
    // Si no existia lo inserta. Si hace falta crece de a chunks
    void insertOrUpdate(K key, V data);

    // nunca nunca nunca debe tirar exception. Devuelve el valor asociado si lo encuentra o null si no está.
    V find(K data);

    // nunca nunca nunca debe tirar exception. Borra y devuelve true si el elemento estaba. Si no lo encuentra devuelve false .
    boolean remove(K key);

    // nunca nunca nunca debe tirar exception. Devuelve la cantidad de elementos presentes
    int size();

    // imprimir en cualquier orden
    void dump();
}
