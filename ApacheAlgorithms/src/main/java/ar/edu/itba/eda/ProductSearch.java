package ar.edu.itba.eda;

import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.text.similarity.LevenshteinDistance;
import info.debatty.java.stringsimilarity.Jaccard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ProductSearch {
    private final List<Product> products;
    private final Soundex soundex;
    private final Metaphone metaphone;
    private final LevenshteinDistance levenshtein;
    private final Jaccard qGram;

    public ProductSearch(String productFilePath) throws IOException {
        this.products = loadProducts(productFilePath);
        this.soundex = new Soundex();
        this.metaphone = new Metaphone();
        this.levenshtein = new LevenshteinDistance();
        this.qGram = new Jaccard(3);
    }

    private List<Product> loadProducts(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(Product::new)
                .collect(Collectors.toList());
    }

    public List<SearchResult> findSimilarProducts(String input, int limit) {
        String normalizedInput = normalizeString(input);
        List<SearchResult> results = new ArrayList<>();

        for (Product product : products) {
            double maxSimilarity = 0;
            String bestAlgorithm = "";

            // Check for exact match first
            if (normalizedInput.equals(product.getNormalizedName())) {
                results.add(new SearchResult(product, 1.0, "Exact Match"));
                continue;
            }

            // Soundex similarity
            double soundexSimilarity = calculateSoundexSimilarity(normalizedInput, product.getNormalizedName());
            if (soundexSimilarity > maxSimilarity) {
                maxSimilarity = soundexSimilarity;
                bestAlgorithm = "Soundex";
            }

            // Metaphone similarity
            double metaphoneSimilarity = calculateMetaphoneSimilarity(normalizedInput, product.getNormalizedName());
            if (metaphoneSimilarity > maxSimilarity) {
                maxSimilarity = metaphoneSimilarity;
                bestAlgorithm = "Metaphone";
            }

            // Levenshtein similarity
            double levenshteinSimilarity = calculateLevenshteinSimilarity(normalizedInput, product.getNormalizedName());
            if (levenshteinSimilarity > maxSimilarity) {
                maxSimilarity = levenshteinSimilarity;
                bestAlgorithm = "Levenshtein";
            }

            // QGram similarity
            double qGramSimilarity = calculateQGramSimilarity(normalizedInput, product.getNormalizedName());
            if (qGramSimilarity > maxSimilarity) {
                maxSimilarity = qGramSimilarity;
                bestAlgorithm = "QGram";
            }

            results.add(new SearchResult(product, maxSimilarity, bestAlgorithm));
        }

        return results.stream()
                .sorted(Comparator.comparingDouble(SearchResult::getSimilarity).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private String normalizeString(String input) {
        return input.toLowerCase()
                .replaceAll("á", "a")
                .replaceAll("é", "e")
                .replaceAll("í", "i")
                .replaceAll("ó", "o")
                .replaceAll("ú", "u")
                .replaceAll("ñ", "n")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private double calculateSoundexSimilarity(String s1, String s2) {
        try {
            return (double) soundex.difference(s1, s2) / 4;
        } catch (Exception e) {
            System.err.println("Error calculating Soundex similarity: " + e.getMessage());
            return 0.0;
        }
    }

    private double calculateMetaphoneSimilarity(String s1, String s2) {
        String metaphone1 = metaphone.encode(s1);
        String metaphone2 = metaphone.encode(s2);

        // Calculate proportion of matching characters
        int matchingChars = 0;
        int maxLength = Math.max(metaphone1.length(), metaphone2.length());
        int minLength = Math.min(metaphone1.length(), metaphone2.length());

        for (int i = 0; i < minLength; i++) {
            if (metaphone1.charAt(i) == metaphone2.charAt(i)) {
                matchingChars++;
            }
        }

        return (double) matchingChars / maxLength;
    }

    private double calculateLevenshteinSimilarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0)
            return 1.0;
        int distance = levenshtein.apply(s1, s2);
        return 1.0 - ((double) distance / maxLength);
    }

    private double calculateQGramSimilarity(String s1, String s2) {
        return qGram.distance(s1, s2);
    }

    public static class SearchResult {
        private final Product product;
        private final double similarity;
        private final String algorithm;

        public SearchResult(Product product, double similarity, String algorithm) {
            this.product = product;
            this.similarity = similarity;
            this.algorithm = algorithm;
        }

        public Product getProduct() {
            return product;
        }

        public double getSimilarity() {
            return similarity;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        @Override
        public String toString() {
            return String.format("Product: %s, Similarity: %.4f, Algorithm: %s",
                    product.getName(), similarity, algorithm);
        }
    }
}