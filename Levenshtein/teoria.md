**Resumen Consolidado de la Distancia de Levenshtein: Conceptos y Ejemplos Prácticos**

**1. ¿Qué es la Distancia de Levenshtein?**

La distancia de Levenshtein es una métrica que mide la **similitud entre dos cadenas de caracteres**. Se define como el **número mínimo de operaciones de edición de un solo carácter** (inserción, eliminación o sustitución) necesarias para transformar una cadena en la otra. Cuanto menor sea la distancia, más similares son las cadenas.

* **Ejemplo Conceptual:** Para transformar "casa" en "calle":
    1.  casa -> cala (sustituir 's' por 'l')
    2.  cala -> calla (insertar 'l')
    3.  calla -> calle (sustituir 'a' por 'e')
        Se necesitan 3 operaciones. La distancia de Levenshtein es 3.

**2. El Algoritmo (Wagner-Fischer con Programación Dinámica)**

La forma más común y eficiente de calcular la distancia de Levenshtein es mediante programación dinámica, utilizando el algoritmo propuesto por Wagner y Fischer.

* **Idea Central:** Se construye una matriz (tabla) donde cada celda `(i, j)` contendrá la distancia de Levenshtein entre los primeros `i` caracteres de la cadena 1 (S1) y los primeros `j` caracteres de la cadena 2 (S2).
* **Dimensiones de la Matriz:** Si S1 tiene longitud `m` y S2 tiene longitud `n`, la matriz tendrá dimensiones `(m+1) x (n+1)`. Las filas y columnas extra (índices 0) representan el caso de comparar con una cadena vacía.

* **Inicialización:**
    * La primera fila `(0, j)` se inicializa con los valores de 0 a `n`. Esto representa el costo de transformar una cadena vacía en el prefijo de S2 de longitud `j` (requiere `j` inserciones).
    * La primera columna `(i, 0)` se inicializa con los valores de 0 a `m`. Representa el costo de transformar el prefijo de S1 de longitud `i` en una cadena vacía (requiere `i` eliminaciones).
    * `matrix[0][0]` es 0 (distancia entre dos cadenas vacías).

  *Ejemplo de Inicialización (de `04-C.pptx`, 'big data' vs 'bigdata')*
    ```
          "" B I G D A T A
      ""  0  1  2  3  4  5  6  7  // Fila 0: distancia("", prefijo_s2)
      B   1
      I   2
      G   3
            ...
      A   8                        // Columna 0: distancia(prefijo_s1, "")
    ```

* **Llenado de la Matriz (Recurrencia):** Se recorre la matriz celda por celda (a partir de `(1, 1)`), calculando el valor de `matrix[i][j]` basándose en los valores ya calculados (arriba, izquierda, diagonal superior izquierda). Para cada celda `(i, j)`:
    1.  Se comparan los caracteres `s1[i-1]` y `s2[j-1]` (se usa `i-1` y `j-1` porque los strings son 0-indexados y la matriz 1-indexada respecto a los caracteres).
    2.  Se calcula el **costo de sustitución**: `cost = 0` si los caracteres son iguales, `cost = 1` si son diferentes.
    3.  El valor de `matrix[i][j]` es el mínimo entre:
        * `matrix[i-1][j] + 1`: Costo de eliminar el carácter `s1[i-1]` (venir desde arriba).
        * `matrix[i][j-1] + 1`: Costo de insertar el carácter `s2[j-1]` (venir desde la izquierda).
        * `matrix[i-1][j-1] + cost`: Costo de sustituir (si `cost=1`) o de no hacer nada (si `cost=0`) (venir desde la diagonal).

        *Fórmula:*
        `matrix[i][j] = min( matrix[i-1][j] + 1, matrix[i][j-1] + 1, matrix[i-1][j-1] + cost )`

* **Resultado Final:** La distancia de Levenshtein entre S1 y S2 es el valor que se encuentra en la última celda de la matriz: `matrix[m][n]`.

**3. Ejemplo de Cálculo ('big data' vs 'bigdaa')**

Usando la implementación `Levenshtein.java` y su función `printMatrix`:

```java
// Llamada: Levenshtein.distance("big data", "bigdaa");

// Salida de printMatrix (simplificada):
//         "" b i g d a a
//      ""  0  1  2  3  4  5  6
//      b   1  0  1  2  3  4  5
//      i   2  1  0  1  2  3  4
//      g   3  2  1  0  1  2  3
//      _   4  3  2  1  1  2  3  // '_' representa el espacio
//      d   5  4  3  2  1  2  3
//      a   6  5  4  3  2  1  2
//      t   7  6  5  4  3  2  2
//      a   8  7  6  5  4  3  2  <--- Resultado Final

// Distancia = 2
```

* **Análisis rápido de la última celda `matrix[8][6]` ('big data' vs 'bigdaa'):**
    * Caracteres: 'a' vs 'a' (iguales), `cost = 0`.
    * Valores vecinos: `matrix[7][6]` (arriba) = 2, `matrix[8][5]` (izquierda) = 3, `matrix[7][5]` (diagonal) = 2.
    * Cálculo: `min( matrix[7][6]+1, matrix[8][5]+1, matrix[7][5]+cost )`
    * `min( 2+1, 3+1, 2+0 ) = min(3, 4, 2) = 2`.

**4. Implementación Java (`Levenshtein.java`)**

* **Método Principal (`distance1`):**
    ```java
    private static int distance1(String s1, String s2) {
        int length1 = s1.length();
        int length2 = s2.length();
        // 1. Crear la matriz
        int[][] memo = new int[length1 + 1][length2 + 1];

        // 2. Inicializar primera fila y columna
        for (int j = 0; j <= length2; j++) { memo[0][j] = j; }
        for (int i = 0; i <= length1; i++) { memo[i][0] = i; }

        // 3. Llenar el resto de la matriz
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                // Calcular costo (0 si son iguales, 1 si diferentes)
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                // Aplicar la fórmula de recurrencia
                memo[i][j] = min(memo[i - 1][j] + 1,      // Deletion
                                 memo[i][j - 1] + 1,      // Insertion
                                 memo[i - 1][j - 1] + cost); // Substitution/Match
            }
        }
        // printMatrix(memo, s1, s2); // Opcional para visualizar
        // 4. Devolver el resultado final
        return memo[length1][length2];
    }

    // Helper min
    private static int min(int a, int b, int c) { ... }
    ```
* **Optimización de Espacio (`distance2`):** Existe una versión que reduce la necesidad de espacio de la matriz completa a solo necesitar espacio proporcional a la longitud de la cadena más corta (O(min(m, n))), manteniendo solo la información de las filas relevantes para el cálculo actual.

**5. Similitud Normalizada**

La distancia mide qué tan *diferentes* son las cadenas. A menudo, es útil tener una medida de *similitud*, típicamente entre 0 (completamente diferentes) y 1 (idénticas).

* **Fórmula:** Una forma común de normalizar es:
  `Similitud = 1.0 - (Distancia / Longitud_Máxima)`
  Donde `Longitud_Máxima` es la longitud de la cadena más larga entre S1 y S2.
* **Implementación (`normalizedSimilarity`):**
    ```java
    public static double normalizedSimilarity(String s1, String s2) {
        int distance = distance(s1, s2); // Calcula la distancia primero
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0; // Distancia entre vacíos es 0, similitud es 1
        }
        return 1.0 - ((double) distance / maxLength);
    }
    ```
* **Ejemplo (`big data` vs `bigdaa`):**
    * Distancia = 2
    * Longitud Máxima = 8 (`big data`)
    * Similitud = 1.0 - (2 / 8) = 1.0 - 0.25 = **0.75**

**6. Complejidad Computacional**

* **Tiempo:** O(m * n), donde `m` y `n` son las longitudes de las dos cadenas. Cada celda de la matriz `m x n` se calcula una vez, y cada cálculo toma tiempo constante.
* **Espacio:**
    * Implementación estándar (`distance1`): O(m * n) debido a la necesidad de almacenar toda la matriz.
    * Implementación optimizada (`distance2`): O(min(m, n)), ya que solo necesita almacenar información equivalente a una o dos filas de la matriz.

**7. Casos de Uso**

La distancia de Levenshtein es ampliamente utilizada en:

* **Corrección Ortográfica:** Sugerir palabras correctas basadas en la cercanía a la palabra mal escrita.
* **Búsqueda Difusa (Fuzzy Search):** Encontrar coincidencias que son similares pero no idénticas a un término de búsqueda (como en Lucene `FuzzyQuery`).
* **Bioinformática:** Comparar secuencias de ADN o proteínas.
* **Detección de Plagio:** Medir la similitud entre textos.
* **Reconocimiento Óptico de Caracteres (OCR):** Corregir errores en texto escaneado.

Este resumen abarca la definición, el algoritmo detallado con ejemplos, la implementación en Java, la normalización y la complejidad de la distancia de Levenshtein, utilizando la información y el código de los archivos proporcionados.