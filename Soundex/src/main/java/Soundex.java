public class Soundex {
  // Array de valores para cada letra del alfabeto (A-Z)
  // Cada letra se mapea a un dígito del 0 al 6 según su sonido
  // 0 representa sonidos que se ignoran (vocales y algunas consonantes)
  private static final char[] VALUES = {'0', '1', '2', '3', '0', '1', '2', '0', '0', '2', '2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '1', '0', '2', '0', '2'};

  public static String representation(String word) {
    // Convertir la palabra a array de caracteres
    char[] IN = word.toCharArray();
    // Array de salida inicializado con ceros (código Soundex de 4 dígitos)
    char[] OUT = {'0', '0', '0', '0'};

    // Agregar la primera letra (siempre se mantiene)
    int count = 0;
    OUT[count++] = Character.toUpperCase(IN[0]);
    char last = Character.toUpperCase(IN[0]);

    // Procesar los caracteres restantes
    for (int i = 1; i < IN.length && count < 4; i++) {
      char c = Character.toUpperCase(IN[i]);
      // Solo procesar letras del alfabeto
      if (c >= 'A' && c <= 'Z') {
        // Obtener el valor Soundex para la letra actual
        char value = VALUES[c - 'A'];
        // Solo agregar el valor si no es 0 y es diferente al último valor agregado
        if (value != '0' && last != value) {
          OUT[count++] = value;
          last = value;
        }
      }
    }

    return new String(OUT);
  }

  // Método para comparar dos palabras usando sus códigos Soundex
  public static double compare(String word1, String word2) {
    // Obtener los códigos Soundex de ambas palabras
    String soundex1 = representation(word1);
    String soundex2 = representation(word2);
    
    // Contar cuántos dígitos coinciden en la misma posición
    int count = 0;
    for (int i = 0; i < 4; i++) {
      if (soundex1.charAt(i) == soundex2.charAt(i)) {
        count++;
      }
    }
    
    // Retornar la similitud como un valor entre 0 y 1
    return (double) count / 4;
  }
}
