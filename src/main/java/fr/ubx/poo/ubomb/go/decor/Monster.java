package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.*;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.geometry.Pos;
import fr.ubx.poo.ubomb.engine.Input;

public class Monster extends Decor implements Movable{

    private boolean moveRequested;
    private Direction direction;
    private Timer time = new Timer(0);
    public int velocity ;

    public Monster(Position position) {
        super(position);
    }

    public boolean isWalkable() {
        return true;
    }

    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor obj = this.game.getGrid().get(nextPos);
        if(obj == null || obj.isWalkable() && this.game.inside(nextPos)) {
            return true;
        }
        return false;
    }

    public void doMove(Direction direction){
        direction = direction.random();
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        moveRequested = false;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void moveRequested(long now){
        long sec = 1000000000;
        if(time.delay(now) >= sec){
            moveRequested = true;
        }
    }





    @Override
    public void explode(){
        remove();
    }
}

