package ar.edu.itba.eda;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.codec.language.Metaphone;
import info.debatty.java.stringsimilarity.QGram;

public class ApachAlgorithms {
  public Soundex soundex = new Soundex();
  public LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
  public Metaphone metaphone = new Metaphone();
  public QGram qGram = new QGram();
  public static void main(String[] args) {
    ApachAlgorithms apachAlgorithms = new ApachAlgorithms();
    System.out.println(apachAlgorithms.soundex.encode("Hello"));
    System.out.println(apachAlgorithms.levenshteinDistance.apply("Hello", "Hella"));
    System.out.println(apachAlgorithms.metaphone.encode("Hello"));
    System.out.println(apachAlgorithms.qGram.distance("Hello", "Hella"));
    double cmp = apachAlgorithms.levenshteinDistance.apply(
      apachAlgorithms.metaphone.encode("Brooklin"),
      apachAlgorithms.metaphone.encode("Clean")
    );
    System.out.println(cmp);
  }

  
}
