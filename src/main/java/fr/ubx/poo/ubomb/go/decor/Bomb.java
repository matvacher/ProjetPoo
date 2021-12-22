package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.engine.Timer;

public class Bomb extends Decor {

    public int state;
    public Timer time;
    public final long sec = 1000000000;

    public Bomb(Position position, Timer time) {
        super(position);
        this.state = 0;
        this.time = time;
    }

    public void stateBomb(long now){
            time.delay(now);
            if(time.delay(now) >= 1 ){
                this.state = this.state + 1 ;
                time.setTime(now);
            }

    }

    public boolean isWalkable(Player player) {
        return true;
    }
}
