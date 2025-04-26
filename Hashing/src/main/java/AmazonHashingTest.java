import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AmazonHashingTest {
    public static void main(String[] args) {
        String filePath = "src/main/resources/amazon-categories30.txt";
        int collisionsMethod1 = 0;
        int collisionsMethod2 = 0;
        
        // Prehash method 1: ASCII of first character.
        ClosedHashing<String, String> hashMethod1 = new ClosedHashing<>(s -> (int) s.charAt(0));
        // Prehash method 2: Sum of ASCII values of all characters.
        ClosedHashing<String, String> hashMethod2 = new ClosedHashing<>(s -> {
            int sum = 0;
            for (char c : s.toCharArray()) {
                sum += c;
            }
            return sum;
        });
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] parts = line.split("#");
                String title = parts[0];
                try {
                    hashMethod1.insertOrUpdate(title, line);
                } catch (RuntimeException e) {
                    collisionsMethod1++;
                }
                try {
                    hashMethod2.insertOrUpdate(title, line);
                } catch (RuntimeException e) {
                    collisionsMethod2++;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Method 1 collisions: " + collisionsMethod1);
        System.out.println("Method 2 collisions: " + collisionsMethod2);
    }
}
