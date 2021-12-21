package fr.ubx.poo.ubomb.engine;

public class Timer{

    private long time;

    public Timer(long now) {
        this.time = now;
    }

    public long getTime(){return time;}

    public void setTime(long now){this.time = now;}

    public long delay(long now){
        return now - time;
    }
}
