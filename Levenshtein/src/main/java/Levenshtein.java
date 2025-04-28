public class Levenshtein {

  // Método auxiliar para calcular el mínimo entre tres valores
  private static int min(int a, int b, int c) {
    return a < b ? (a < c ? a : c) : (b < c ? b : c);
  }

  // Método para imprimir la matriz de distancias de Levenshtein
  private static void printMatrix(int[][] matrix, String s1, String s2) {
    System.out.println("\nLevenshtein Distance Matrix:");
    // Imprime la cabecera con los caracteres de la segunda cadena
    System.out.print("    ");
    System.out.print("  ");
    for (char c : s2.toCharArray()) {
      System.out.print(c + "  ");
    }
    System.out.println();

    // Imprime la matriz con los caracteres de la primera cadena como encabezado de filas
    for (int i = 0; i <= s1.length(); i++) {
      if (i == 0) {
        System.out.print("  ");
      } else {
        System.out.print(s1.charAt(i-1) + " ");
      }
      for (int j = 0; j <= s2.length(); j++) {
        System.out.printf("%2d ", matrix[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  // Implementación de Levenshtein usando una matriz completa
  private static int distance1(String s1, String s2) {
    int length1 = s1.length();
    int length2 = s2.length();
    int[][] memo = new int[length1 + 1][length2 + 1];
    
    // Inicializa la primera fila (transformar cadena vacía en prefijos de s2)
    for (int j = 0; j <= length2; j++) {
      memo[0][j] = j;
    }

    // Inicializa la primera columna (transformar prefijos de s1 en cadena vacía)
    for (int i = 1; i <= length1; i++) {
      memo[i][0] = i;
    }

    // Llena el resto de la matriz usando la fórmula de recurrencia
    for (int i = 1; i <= length1; i++) {
      for (int j = 1; j <= length2; j++) {
        // Si los caracteres son iguales, no hay costo; de lo contrario, el costo es 1
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          memo[i][j] = memo[i - 1][j - 1];
        } else {
          // Calcula el mínimo entre eliminar, insertar o sustituir
          memo[i][j] = min(memo[i - 1][j - 1], memo[i - 1][j], memo[i][j - 1]) + 1;
        }
      }
    }
    
    // Imprime la matriz de distancias (opcional para depuración)
    printMatrix(memo, s1, s2);
    return memo[length1][length2]; // Retorna la distancia de Levenshtein
  }

  // Implementación optimizada en espacio (usa solo un arreglo)
  private static int distance2(String s1, String s2) {
    int length1 = s1.length();
    int length2 = s2.length();
    
    int[] memo = new int[length2 + 1];

    // Inicializa la primera fila (transformar cadena vacía en prefijos de s2)
    for (int j = 0; j <= length2; j++) {
      memo[j] = j;
    }

    for (int i = 1; i <= length1; i++) {
      int last = i; // Representa el valor de la celda anterior en la fila actual
      for (int j = 1; j <= length2; j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          int temp = last;
          last = memo[j-1]; // No hay costo si los caracteres son iguales
          memo[j-1] = temp;
        } else {
          int temp = last;
          // Calcula el mínimo entre eliminar, insertar o sustituir
          last = min(memo[j-1], memo[j], last) + 1;
          memo[j-1] = temp;
        }
        if (j == length2) {
          memo[j] = last; // Actualiza el último valor de la fila
        }
      }
    }

    return memo[length2]; // Retorna la distancia de Levenshtein
  }

  // Método público para calcular la distancia, elige entre las dos implementaciones
  public static int distance(String s1, String s2, boolean useMatrix) {
    return useMatrix ? distance1(s1, s2) : distance2(s1, s2);
  }

  // Método público por defecto que usa la implementación optimizada
  public static int distance(String s1, String s2) {
    return distance2(s1, s2);  // Por defecto usa el método optimizado
  }

  // Calcula la similitud normalizada entre dos cadenas
  public static double normalizedSimilarity(String s1, String s2) {
    int distance = distance(s1, s2); // Calcula la distancia de Levenshtein
    return 1 - (double) distance / Math.max(s1.length(), s2.length()); // Normaliza el resultado
  }
}
