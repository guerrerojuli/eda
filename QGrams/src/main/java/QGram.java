import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QGram {
  private final int q;

  public QGram(int q) {
    if (q < 1) {
      throw new IllegalArgumentException("q must be greater than 0");
    }
    this.q = q;
  }

  private List<String> getQGrams(String text) {
    if (text == null || text.length() < q) {
      return new ArrayList<>();
    }
    
    List<String> qGrams = new ArrayList<>();
    for (int i = 0; i <= text.length() - q; i++) {
      qGrams.add(text.substring(i, i + q));
    }
    return qGrams;
  }

  public void printTokens(String text) {
    List<String> qGrams = getQGrams(text);
    System.out.println(String.join(" ", qGrams));
  }

  public double similarity(String text1, String text2) {
    if (text1 == null || text2 == null) {
      return 0.0;
    }

    List<String> qGrams1 = getQGrams(text1);
    List<String> qGrams2 = getQGrams(text2);

    if (qGrams1.isEmpty() && qGrams2.isEmpty()) {
      return 1.0;
    }
    if (qGrams1.isEmpty() || qGrams2.isEmpty()) {
      return 0.0;
    }

    // Count frequencies of q-grams in both strings
    Map<String, Integer> freq1 = new HashMap<>();
    Map<String, Integer> freq2 = new HashMap<>();

    for (String qGram : qGrams1) {
      freq1.merge(qGram, 1, Integer::sum);
    }
    for (String qGram : qGrams2) {
      freq2.merge(qGram, 1, Integer::sum);
    }

    // Calculate not shared q-grams
    int notSharedQGrams = 0;
    
    // Check q-grams unique to freq1
    for (Map.Entry<String, Integer> entry : freq1.entrySet()) {
      String qGram = entry.getKey();
      if (!freq2.containsKey(qGram)) {
        notSharedQGrams += entry.getValue();
      } else {
        // If present in both, count the difference in frequencies
        notSharedQGrams += Math.abs(entry.getValue() - freq2.get(qGram));
      }
    }
    
    // Check q-grams unique to freq2
    for (Map.Entry<String, Integer> entry : freq2.entrySet()) {
      if (!freq1.containsKey(entry.getKey())) {
        notSharedQGrams += entry.getValue();
      }
    }

    int totalQGrams = qGrams1.size() + qGrams2.size();
    
    // Return similarity score using (total - notShared)/total
    return (double) (totalQGrams - notSharedQGrams) / totalQGrams;
  }
}
