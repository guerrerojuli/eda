import org.junit.Test;
import static org.junit.Assert.*;

public class LevenshteinTest {
  @Test
  public void testDistance() {
    assertEquals(0, Levenshtein.distance("test", "test"));
    assertEquals(1, Levenshtein.distance("test", "testa"));
    assertEquals(2, Levenshtein.distance("test", "testbb"));
    assertEquals(1, Levenshtein.distance("test", "testc"));
    assertEquals(1, Levenshtein.distance("test", "testd"));
    assertEquals(1, Levenshtein.distance("test", "teste"));
    assertEquals(1, Levenshtein.distance("test", "testf"));
    assertEquals(2, Levenshtein.distance("big data", "bigdaa"));
  }

  @Test
  public void testNormalizedSimilarity() {
    assertEquals(0.75, Levenshtein.normalizedSimilarity("big data", "bigdaa"), 0.0001);
  }

  @Test
  public void testExerciseCases() {
    // Test cases from the exercise
    System.out.println("\nTest Cases Analysis:");
    
    // Case 1: brooklin vs brugleen
    System.out.println("\n1. 'brooklin' vs 'brugleen':");
    int dist1 = Levenshtein.distance("brooklin", "brugleen");
    double sim1 = Levenshtein.normalizedSimilarity("brooklin", "brugleen");
    System.out.printf("Distance: %d\nNormalized Similarity: %.4f\n", dist1, sim1);
    
    // Case 2: brooklin vs brooclean
    System.out.println("\n2. 'brooklin' vs 'brooclean':");
    int dist2 = Levenshtein.distance("brooklin", "brooclean");
    double sim2 = Levenshtein.normalizedSimilarity("brooklin", "brooclean");
    System.out.printf("Distance: %d\nNormalized Similarity: %.4f\n", dist2, sim2);
    
    // Case 3: brooklin vs bluclean
    System.out.println("\n3. 'brooklin' vs 'bluclean':");
    int dist3 = Levenshtein.distance("brooklin", "bluclean");
    double sim3 = Levenshtein.normalizedSimilarity("brooklin", "bluclean");
    System.out.printf("Distance: %d\nNormalized Similarity: %.4f\n", dist3, sim3);
    
    // Case 4: brooklin vs clean
    System.out.println("\n4. 'brooklin' vs 'clean':");
    int dist4 = Levenshtein.distance("brooklin", "clean");
    double sim4 = Levenshtein.normalizedSimilarity("brooklin", "clean");
    System.out.printf("Distance: %d\nNormalized Similarity: %.4f\n", dist4, sim4);
  }
}
