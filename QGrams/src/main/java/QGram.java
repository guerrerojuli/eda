import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que implementa el algoritmo de Q-Grams para comparación de strings.
 * Los Q-Grams son substrings de longitud q que se extraen de un texto.
 * Se utilizan comúnmente para medir la similitud entre dos strings.
 */
public class QGram {
  private final int q;  // Longitud de los Q-Grams

  /**
   * Constructor de la clase QGram
   * @param q Longitud de los Q-Grams a generar
   * @throws IllegalArgumentException si q es menor que 1
   */
  public QGram(int q) {
    if (q < 1) {
      throw new IllegalArgumentException("q must be greater than 0");
    }
    this.q = q;
  }

  /**
   * Genera todos los Q-Grams posibles de un texto dado
   * @param text Texto del cual extraer los Q-Grams
   * @return Lista de Q-Grams encontrados
   */
  private List<String> getQGrams(String text) {
    if (text == null || text.length() < q) {
      return new ArrayList<>();
    }
    
    List<String> qGrams = new ArrayList<>();
    // Genera Q-Grams deslizando una ventana de tamaño q sobre el texto
    for (int i = 0; i <= text.length() - q; i++) {
      qGrams.add(text.substring(i, i + q));
    }
    return qGrams;
  }

  /**
   * Imprime todos los Q-Grams de un texto separados por espacios
   * @param text Texto del cual imprimir los Q-Grams
   */
  public void printTokens(String text) {
    List<String> qGrams = getQGrams(text);
    System.out.println(String.join(" ", qGrams));
  }

  /**
   * Calcula la similitud entre dos textos usando Q-Grams
   * La similitud se calcula como: (total de Q-Grams - Q-Grams no compartidos) / total de Q-Grams
   * @param text1 Primer texto a comparar
   * @param text2 Segundo texto a comparar
   * @return Valor de similitud entre 0.0 y 1.0
   */
  public double similarity(String text1, String text2) {
    if (text1 == null || text2 == null) {
      return 0.0;
    }

    List<String> qGrams1 = getQGrams(text1);
    List<String> qGrams2 = getQGrams(text2);

    // Casos especiales
    if (qGrams1.isEmpty() && qGrams2.isEmpty()) {
      return 1.0;  // Ambos textos vacíos son considerados idénticos
    }
    if (qGrams1.isEmpty() || qGrams2.isEmpty()) {
      return 0.0;  // Si uno está vacío y el otro no, no hay similitud
    }

    // Contar la frecuencia de cada Q-Gram en ambos textos
    Map<String, Integer> freq1 = new HashMap<>();
    Map<String, Integer> freq2 = new HashMap<>();

    for (String qGram : qGrams1) {
      freq1.merge(qGram, 1, Integer::sum);
    }
    for (String qGram : qGrams2) {
      freq2.merge(qGram, 1, Integer::sum);
    }

    // Calcular Q-Grams no compartidos
    int notSharedQGrams = 0;
    
    // Verificar Q-Grams únicos en el primer texto
    for (Map.Entry<String, Integer> entry : freq1.entrySet()) {
      String qGram = entry.getKey();
      if (!freq2.containsKey(qGram)) {
        // Si el Q-Gram no existe en el segundo texto, sumar su frecuencia
        notSharedQGrams += entry.getValue();
      } else {
        // Si existe en ambos, sumar la diferencia en frecuencias
        notSharedQGrams += Math.abs(entry.getValue() - freq2.get(qGram));
      }
    }
    
    // Verificar Q-Grams únicos en el segundo texto
    for (Map.Entry<String, Integer> entry : freq2.entrySet()) {
      if (!freq1.containsKey(entry.getKey())) {
        notSharedQGrams += entry.getValue();
      }
    }

    int totalQGrams = qGrams1.size() + qGrams2.size();
    
    // Calcular y retornar el score de similitud
    return (double) (totalQGrams - notSharedQGrams) / totalQGrams;
  }
}
