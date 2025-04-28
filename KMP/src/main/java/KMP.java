import java.util.ArrayList;

public class KMP {
    /**
     * Preprocesa el patrón para crear el array de longest prefix suffix (lps)
     * El array lps almacena la longitud del prefijo más largo que también es sufijo
     * @param query El patrón a preprocesar
     * @return El array lps que contiene las longitudes de los prefijos más largos que son también sufijos
     */
    private static <T> int[] nextComputation(T[] query) {
        // Creamos un array de enteros con la misma longitud que el patrón
        int[] lps = new int[query.length];
        // len representa la longitud del prefijo más largo que también es sufijo
        int len = 0;
        // i es el índice que recorre el patrón, comenzando desde 1 porque lps[0] siempre es 0
        int i = 1;
        
        // El primer elemento del array lps siempre es 0 porque no hay prefijo propio para un patrón de longitud 1
        lps[0] = 0;
        
        // Recorremos el patrón para calcular lps[i] para cada posición
        while (i < query.length) {
            // Si el carácter actual coincide con el carácter en la posición len
            if (query[i].equals(query[len])) {
                // Incrementamos len porque encontramos un prefijo que coincide con un sufijo
                len++;
                // Guardamos la longitud del prefijo más largo que coincide con un sufijo en la posición i
                lps[i] = len;
                // Avanzamos al siguiente carácter del patrón
                i++;
            } else {
                // Si no hay coincidencia y len no es 0
                if (len != 0) {
                    // Retrocedemos len al valor anterior usando el array lps
                    // Esto nos permite reutilizar información de comparaciones previas
                    len = lps[len - 1];
                } else {
                    // Si len es 0, no hay coincidencia y simplemente avanzamos al siguiente carácter
                    lps[i] = 0;
                    i++;
                }
            }
        }
        
        return lps;
    }

    /**
     * Busca la primera ocurrencia del patrón en el texto usando el algoritmo KMP
     * @param query El patrón a buscar
     * @param target El texto donde buscar
     * @return El índice de la primera ocurrencia del patrón, o -1 si no se encuentra
     */
    public static <T> int indexOf(T[] query, T[] target) {
        // Casos base para manejar situaciones especiales
        // Si el patrón es más largo que el texto, no puede estar contenido en él
        if (query.length > target.length) return -1;
        // Si el patrón está vacío, se considera que está en la posición 0
        if (query.length == 0) return 0;
        // Si el texto está vacío y el patrón no, no puede estar contenido
        if (target.length == 0) return -1;
        
        // Preprocesamos el patrón para obtener el array lps
        int[] lps = nextComputation(query);
        // i es el índice que recorre el texto
        int i = 0;
        // j es el índice que recorre el patrón
        int j = 0;
        
        // Recorremos el texto mientras no lo hayamos completado
        while (i < target.length) {
            // Si los caracteres actuales del patrón y el texto coinciden
            if (query[j].equals(target[i])) {
                // Avanzamos ambos índices para comparar el siguiente par de caracteres
                i++;
                j++;
            }
            
            // Si hemos encontrado el patrón completo
            if (j == query.length) {
                // Retornamos la posición inicial donde se encontró el patrón
                return i - j;
            } 
            // Si no hemos terminado de recorrer el texto y hay un desacuerdo
            else if (i < target.length && !query[j].equals(target[i])) {
                // Si j no es 0, usamos el array lps para saltar caracteres
                if (j != 0) {
                    // Retrocedemos j usando el array lps para evitar comparaciones innecesarias
                    j = lps[j - 1];
                } else {
                    // Si j es 0, simplemente avanzamos en el texto
                    i++;
                }
            }
        }
        
        // Si llegamos aquí, no se encontró el patrón
        return -1;
    }

    /**
     * Busca todas las ocurrencias del patrón en el texto usando el algoritmo KMP
     * @param query El patrón a buscar
     * @param target El texto donde buscar
     * @return Una lista con todos los índices donde aparece el patrón
     */
    public static <T> ArrayList<Integer> findAll(T[] query, T[] target) {
        // Creamos una lista para almacenar todas las ocurrencias del patrón
        ArrayList<Integer> occurrences = new ArrayList<>();
        
        // Casos base similares a indexOf
        if (query.length > target.length) return occurrences;
        if (query.length == 0) {
            occurrences.add(0);
            return occurrences;
        }
        if (target.length == 0) return occurrences;
        
        // Preprocesamos el patrón
        int[] lps = nextComputation(query);
        // i recorre el texto
        int i = 0;
        // j recorre el patrón
        int j = 0;
        
        // Recorremos el texto
        while (i < target.length) {
            // Si los caracteres coinciden
            if (query[j].equals(target[i])) {
                // Avanzamos ambos índices
                i++;
                j++;
            }
            
            // Si encontramos una ocurrencia completa del patrón
            if (j == query.length) {
                // Guardamos la posición inicial de la ocurrencia
                occurrences.add(i - j);
                // Retrocedemos j usando lps para seguir buscando más ocurrencias
                j = lps[j - 1];
            } 
            // Si hay un desacuerdo y no hemos terminado de recorrer el texto
            else if (i < target.length && !query[j].equals(target[i])) {
                if (j != 0) {
                    // Usamos lps para saltar caracteres
                    j = lps[j - 1];
                } else {
                    // Si j es 0, avanzamos en el texto
                    i++;
                }
            }
        }
        
        return occurrences;
    }

    // Métodos de conveniencia para arrays de caracteres
    // Estos métodos convierten arrays de tipos primitivos a arrays de objetos
    // para poder usar los métodos genéricos anteriores
    
    /**
     * Busca la primera ocurrencia del patrón en el texto para arrays de caracteres
     * @param query El patrón a buscar (array de caracteres)
     * @param target El texto donde buscar (array de caracteres)
     * @return El índice de la primera ocurrencia del patrón, o -1 si no se encuentra
     */
    public static int indexOf(char[] query, char[] target) {
        // Convertimos los arrays de char a arrays de Character
        Character[] queryObj = new Character[query.length];
        Character[] targetObj = new Character[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return indexOf(queryObj, targetObj);
    }

    /**
     * Busca todas las ocurrencias del patrón en el texto para arrays de caracteres
     * @param query El patrón a buscar (array de caracteres)
     * @param target El texto donde buscar (array de caracteres)
     * @return Una lista con todos los índices donde aparece el patrón
     */
    public static ArrayList<Integer> findAll(char[] query, char[] target) {
        // Convertimos los arrays de char a arrays de Character
        Character[] queryObj = new Character[query.length];
        Character[] targetObj = new Character[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return findAll(queryObj, targetObj);
    }

    // Métodos de conveniencia para arrays de enteros
    // Similar a los métodos para arrays de caracteres, pero para arrays de int
    
    /**
     * Busca la primera ocurrencia del patrón en el texto para arrays de enteros
     * @param query El patrón a buscar (array de enteros)
     * @param target El texto donde buscar (array de enteros)
     * @return El índice de la primera ocurrencia del patrón, o -1 si no se encuentra
     */
    public static int indexOf(int[] query, int[] target) {
        // Convertimos los arrays de int a arrays de Integer
        Integer[] queryObj = new Integer[query.length];
        Integer[] targetObj = new Integer[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return indexOf(queryObj, targetObj);
    }

    /**
     * Busca todas las ocurrencias del patrón en el texto para arrays de enteros
     * @param query El patrón a buscar (array de enteros)
     * @param target El texto donde buscar (array de enteros)
     * @return Una lista con todos los índices donde aparece el patrón
     */
    public static ArrayList<Integer> findAll(int[] query, int[] target) {
        // Convertimos los arrays de int a arrays de Integer
        Integer[] queryObj = new Integer[query.length];
        Integer[] targetObj = new Integer[target.length];
        for (int i = 0; i < query.length; i++) queryObj[i] = query[i];
        for (int i = 0; i < target.length; i++) targetObj[i] = target[i];
        return findAll(queryObj, targetObj);
    }
}
