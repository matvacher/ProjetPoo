package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.engine.Timer;

public class Bomb extends Decor {

    private Timer time = new Timer(0);
    private int state = 4;
    private boolean displayComplete = true;
    private boolean stateModified;
    private Bomb[] tabImageBomb = new Bomb[4];
    private int range;
    private Explosion[] tabExplosion;
    long sec = 1000000000;

    public Bomb(Position position) {
        super(position);
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public Timer getTime() {
        return time;
    }

    public int getState() {
        return state;
    }

    public void setStatus(int i){
        this.state = i;
    }

    public void stopDisplay(){
        this.displayComplete = false;
    }

    public boolean getDisplay() {
        return displayComplete;
    }
    public boolean getModified(){
        return stateModified;
    }

    public void setModified(){
        this.stateModified = true;
    }

    public void setTabImageBomb( ) {
        Bomb0 bomb0 = new Bomb0(this.getPosition());
        Bomb1 bomb1 = new Bomb1(this.getPosition());
        Bomb2 bomb2 = new Bomb2(this.getPosition());
        Bomb3 bomb3 = new Bomb3(this.getPosition());
        tabImageBomb[0] = bomb0;
        tabImageBomb[1] = bomb1;
        tabImageBomb[2] = bomb2;
        tabImageBomb[3] = bomb3;
    }

    public Bomb getImageBomb(int i) {
        return tabImageBomb[i];
    }
    public void removeIamgeTab(int i){
        tabImageBomb[i].remove();
    }

    public void setState(long now) {



        if( time.delay(now ) > 0 && state == 4){
            state --;
            System.out.println("Etat 3");
            stateModified = false;
        }
        if( time.delay(now ) >=  sec && state == 3) {
            state--;
            System.out.println("Etat 2");
            stateModified = false;
        }
        if( time.delay(now ) >= 2 * sec && state == 2){
            state --;
            System.out.println("Etat 1");
            stateModified = false;
        }
        if( time.delay(now ) >= 3 * sec && state == 1 ){
            state--;
            System.out.println("Etat 0");
            stateModified = false;
        }
        if( time.delay(now ) >= 4 * sec && state == 0 && displayComplete){
            stateModified = false;
            stopDisplay();
        }
        if (!displayComplete)
            setExplosion(now);
    }


    private boolean tatatutu = true;
    private boolean explosionReady = false;

    public boolean isExplosionReady(){
        return explosionReady;
    }

    public void setExplosion( long now){

        int range = this.getRange();
        Position pos = this.getPosition();
        tabExplosion = new Explosion[4 * range];
        int cpt = 0;


        for (int i = 0; i < range; i++) {
            Position posLeft = new Position(pos.getX()-i, pos.getY());
            Explosion explosionLeft = new Explosion(posLeft);
            tabExplosion[cpt] = explosionLeft;
            cpt++;
            Position posRight = new Position(pos.getX()+ i, pos.getY());
            Explosion explosionRight = new Explosion(posRight);
            tabExplosion[cpt] = explosionRight;
            cpt++;
            Position posUp = new Position(pos.getX(), pos.getY() + i);
            Explosion explosionUp = new Explosion(posRight);
            tabExplosion[cpt] = explosionRight;
            cpt++;
            Position posDown = new Position(pos.getX(), pos.getY() - i);
            Explosion explosionDown = new Explosion(posDown);
            tabExplosion[cpt] = explosionRight;
            cpt++;

        }
        explosionReady = true;
        if(tatatutu){
            for (int i = 0; i < tabExplosion.length; i++) {
                System.out.println(tabExplosion[i]);
            }
            tatatutu = false;
        }




    }

    public void info(){
        System.out.println("Info bombe :");
        System.out.println("Timer = " + getTime());
        System.out.println("Status = " + state);
    }

    public boolean isWalkable(Player player) {
        return true;}
}
