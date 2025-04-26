public class Soundex {
  private static final char[] VALUES = {'0', '1', '2', '3', '0', '1', '2', '0', '0', '2', '2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '1', '0', '2', '0', '2'};

  public static String representation(String word) {
    char[] IN = word.toCharArray();
    char[] OUT = {'0', '0', '0', '0'};

    // Add first letter
    int count = 0;
    OUT[count++] = Character.toUpperCase(IN[0]);
    char last = Character.toUpperCase(IN[0]);

    // Process remaining characters
    for (int i = 1; i < IN.length && count < 4; i++) {
      char c = Character.toUpperCase(IN[i]);
      if (c >= 'A' && c <= 'Z') {
        char value = VALUES[c - 'A'];
        if (value != '0' && last != value) {
          OUT[count++] = value;
          last = value;
        }
      }
    }

    return new String(OUT);
  }

  public static double compare(String word1, String word2) {
    String soundex1 = representation(word1);
    String soundex2 = representation(word2);
    int count = 0;
    for (int i = 0; i < 4; i++) {
      if (soundex1.charAt(i) == soundex2.charAt(i)) {
        count++;
      }
    }
    return (double) count / 4;
  }
}
