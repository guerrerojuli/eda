import java.util.function.Function;

public class MyBag<T> {
    private final IndexParametricService<T, Integer> hashTable;
    
    public MyBag(Function<? super T, Integer> mappingFn) {
        hashTable = new OpenHashing<>(mappingFn);
    }

    public void add(T value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");

        Integer currentCount = hashTable.find(value);
        if (currentCount == null) {
            hashTable.insertOrUpdate(value, 1);
        } else {
            hashTable.insertOrUpdate(value, currentCount + 1);
        }
    }

    public boolean remove(T value) {
        if (value == null)
            return false;

        Integer currentCount = hashTable.find(value);
        if (currentCount != null) {
            if (currentCount == 1) {
                return hashTable.remove(value);
            } else {
                hashTable.insertOrUpdate(value, currentCount - 1);
                return true;
            }
        }
        return false;
    }

    public int getCount(T value) {
        if (value == null)
            return 0;

        Integer count = hashTable.find(value);
        return count == null ? 0 : count;
    }

    public void dump() {
        hashTable.dump();
    }

    public static void main(String[] args) {
        MyBag<String> bag = new MyBag<>(String::hashCode);
        bag.add("apple");
        bag.add("banana");
        bag.add("apple");
        bag.add("orange");
        bag.add("apple");
        
        System.out.println("Initial state:");
        bag.dump();
        
        System.out.println("\nCount of 'apple': " + bag.getCount("apple"));
        
        System.out.println("\nRemoving one 'apple':");
        bag.remove("apple");
        bag.dump();
        
        System.out.println("\nCount of 'apple': " + bag.getCount("apple"));
    }
} 