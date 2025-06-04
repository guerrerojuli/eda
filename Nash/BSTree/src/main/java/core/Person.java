package core;

public class Person implements Comparable<Person> {

    private final String name;
    private final Integer id;

    public Person(Integer id, String name){
        this.name = name;
        this.id = id;
    }

    @Override
    public int compareTo(Person o) {
        int c = id.compareTo(o.id);
        if(c == 0){
            c = name.compareTo(o.name);
        }
        return c;
    }

    @Override
    public String toString() {
        return name + ": " + id;
    }
}
