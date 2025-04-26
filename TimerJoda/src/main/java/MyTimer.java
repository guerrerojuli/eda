import org.joda.time.*;

public class MyTimer {
    private Instant startTime, endTime;
    private Period elapsedTime;

    public MyTimer(){
        startTime = new Instant();
    }

    public MyTimer(long start){
        startTime = new Instant(start);
    }

    public void stop(){
        endTime = Instant.now();
        checkDuration();
    }

    public void stop(long stop){
        endTime = new Instant(stop);
        checkDuration();
    }

    public void checkDuration(){
        if(endTime.isBefore(startTime)){
            throw new RuntimeException("Rompio");
        }
        elapsedTime = new Period(startTime, endTime);
    }

    public long getElapsedTime(){
        return elapsedTime.toStandardDuration().getMillis();
    }

    public long getDays(){
        return Days.standardDaysIn(elapsedTime).getDays();
    }

    public long getHs(){
        return elapsedTime.getHours();
    }

    public long getMins(){
        return elapsedTime.getMinutes();
    }

    public long getSecs(){
        return elapsedTime.getSeconds();
    }

    @Override
    public String toString(){
        //return "(%l ms) %d día %d hs %d min %f seg".formatted();
        return "(%d ms) %d día(s) %d horas %d mins %d seg".formatted(getElapsedTime(), getDays(), getHs(), getMins(), getSecs());
    }


    public static void main(String[] args) {
        MyTimer t1= new MyTimer();
        MyTimer t2= new MyTimer(6000);

        // bla bla bla
        t1.stop();

        // bla bla bla
        t2.stop(7000);

        System.out.println(t1);
        System.out.println(t2);


        t1= new MyTimer();

        // bla bla bla

        t1.stop(6000);

        t2= new MyTimer(6000);

        // bla bla bla

        t2.stop();
    }

}