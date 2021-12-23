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

    public boolean canMove(Direction direction){ return true;};
    public void doMove(Direction direction){};

    public boolean isWalkable() {
        return true;
    }

    public void setMoveRequested(boolean b){
        this.moveRequested = b;
    }

    public Timer getTime(){
        return this.time;
    }

    public void setTime(long now){
        this.time = new Timer(now);
    }

    public void resetTimer(){
        this.time = new Timer(0);
    }

    public boolean getMoveRequested(){
        return this.moveRequested;
    }


    public void moveMonster(Direction direction, Game game){
        Position actualPos = getPosition();
        Position nextPos = direction.nextPosition(actualPos);
        if (!game.inside(nextPos) || game.getGrid().get(nextPos).isWalkable()){
            game.getGrid().remove(getPosition());
            setPosition(nextPos);
            game.getGrid().set(nextPos, this);
            setMoveRequested(false);
        }
    }


    public void updatePos(long now , Game game) {
        while (getMoveRequested() != false){
            direction = direction.random();
            moveMonster(direction, game);
        }
    }


    public void moveRequested(long now){
        long sec = 1000000000;
        if(time.delay(now) >= sec){
            setMoveRequested(true);
            setTime(now);
        }
    }





    @Override
    public void explode(){
        remove();
    }
}

