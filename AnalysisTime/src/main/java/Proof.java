public class Proof {
    public static void main(String[] args) {
        int start = 10;

        MyTimer myCrono = new MyTimer(start);

        myCrono.stop(start + 93623040);

        System.out.println(myCrono);
    }
}