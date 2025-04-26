public class Levenshtein {
  private static int min(int a, int b, int c) {
    return a < b ? (a < c ? a : c) : (b < c ? b : c);
  }

  private static void printMatrix(int[][] matrix, String s1, String s2) {
    System.out.println("\nLevenshtein Distance Matrix:");
    // Print header with second string characters
    System.out.print("    ");
    System.out.print("  ");
    for (char c : s2.toCharArray()) {
      System.out.print(c + "  ");
    }
    System.out.println();

    // Print matrix with first string characters as row headers
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

  private static int distance1(String s1, String s2) {
    int length1 = s1.length();
    int length2 = s2.length();
    int[][] memo = new int[length1 + 1][length2 + 1];
    
    // Fill first row
    for (int j = 0; j <= length2; j++) {
      memo[0][j] = j;
    }

    // Fill first column
    for (int i = 1; i <= length1; i++) {
      memo[i][0] = i;
    }

    // Fill rest of the table
    for (int i = 1; i <= length1; i++) {
      for (int j = 1; j <= length2; j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          memo[i][j] = memo[i - 1][j - 1];
        } else {
          memo[i][j] = min(memo[i - 1][j - 1], memo[i - 1][j], memo[i][j - 1]) + 1;
        }
      }
    }
    
    printMatrix(memo, s1, s2);
    return memo[length1][length2];
  }

  private static int distance2(String s1, String s2) {
    int length1 = s1.length();
    int length2 = s2.length();
    
    int[] memo = new int[length2 + 1];

    // Fill first row
    for (int j = 0; j <= length2; j++) {
      memo[j] = j;
    }

    for (int i = 1; i <= length1; i++) {
      int last = i;
      for (int j = 1; j <= length2; j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          int temp = last;
          last = memo[j-1];
          memo[j-1] = temp;
        } else {
          int temp = last;
          last = min(memo[j-1], memo[j], last) + 1;
          memo[j-1] = temp;
        }
        if (j == length2) {
          memo[j] = last;
        }
      }
    }

    return memo[length2];
  }

  public static int distance(String s1, String s2, boolean useMatrix) {
    return useMatrix ? distance1(s1, s2) : distance2(s1, s2);
  }

  public static int distance(String s1, String s2) {
    return distance2(s1, s2);  // Default to method 2 as requested
  }

  public static double normalizedSimilarity(String s1, String s2) {
    int distance = distance(s1, s2);
    return 1 - (double) distance / Math.max(s1.length(), s2.length());
  }
}