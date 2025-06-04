import java.util.function.Function;

public class HashBag<T> implements Bag<T> {

    ClosedHashing<T, Integer> bag;

    public HashBag(Function<? super T, Integer> function) {
        bag = new ClosedHashing<>(function);
    }

    @Override
    public void add(T value) {
        Integer result = bag.find(value);
        if (result == null) {
            bag.insertOrUpdateClosedHashing(value, 1);
        } else {
            bag.insertOrUpdateClosedHashing(value, result + 1);
        }
    }

    @Override
    public void remove(T value) {
        Integer result = bag.find(value);
        if (result != null) {
            if (result == 1) {
                bag.remove(value);
            } else {
                bag.insertOrUpdate(value, result - 1);
            }
        }
    }

    @Override
    public int getCount(T value) {
        Integer result = bag.find(value);
        return result == null ? 0 : result;
    }

    public void dump(){
        bag.dump();
    }

    public static void main(String[] args) {
        Bag<Integer> myBag = new HashBag<>(x -> x);
        myBag.add(10);
        myBag.add(20);
        myBag.add(10);
        myBag.add(10);
        System.out.println(myBag.getCount(5));
        System.out.println(myBag.getCount(10));
        myBag.dump();
    }
}
