package ar.edu.itba.eda;

public class Product {
    private final String name;
    private final String normalizedName;

    public Product(String name) {
        this.name = name;
        this.normalizedName = normalizeString(name);
    }

    public String getName() {
        return name;
    }

    public String getNormalizedName() {
        return normalizedName;
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
} 