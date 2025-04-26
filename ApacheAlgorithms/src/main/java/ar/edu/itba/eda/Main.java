package ar.edu.itba.eda;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            ProductSearch productSearch = new ProductSearch("product.txt");
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Enter a product name to search (or 'exit' to quit):");
            String input;
            
            while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
                List<ProductSearch.SearchResult> results = productSearch.findSimilarProducts(input, 5);
                System.out.println("\nTop 5 similar products:");
                results.forEach(System.out::println);
                System.out.println("\nEnter another product name (or 'exit' to quit):");
            }
            
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading products file: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 