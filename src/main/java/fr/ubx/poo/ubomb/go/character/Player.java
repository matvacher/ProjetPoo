/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import com.sun.marlin.DRenderer;
import fr.ubx.poo.ubomb.game.*;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.geometry.Pos;
import fr.ubx.poo.ubomb.engine.Input;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;


public class Player extends GameObject implements Movable {

     private Direction direction;
     private boolean moveRequested = false;
     private int lives;
     private int nbKey;
     private int bombRange;
     private int nbBomb;
     private int actualLevel;

    public Player(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = lives;
        this.nbKey = 0;
        this.bombRange = 1;
        this.nbBomb = game.bombBagCapacity;
        this.actualLevel = 1;
        }

    public int getLives() {
        return lives;
    }

    public void setLives(int i) {
        this.lives = i;
    }

    public void setNbBomb(int i) {
        this.nbBomb = i;
    }


    public int getNbKey() {
        return nbKey;
    }

    public int getBombRange() {
        return bombRange;
    }

    public int getNbBomb(){
        return this.nbBomb;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getActualLevel(){
        return this.actualLevel;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor obj = this.game.getGrid().get(nextPos);
        if(obj instanceof Box  ) {
            Box box = (Box) obj;
            return box.moveBox(game, direction);
        }
        if(this.game.inside(nextPos) && (obj == null || obj.isWalkable(this)) ) {
            return true;
        }
        return false;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void doMove(Direction direction) {
        // Check if we need to pick something up
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);

        Decor dec = this.game.getGrid().get(nextPos);
        if(dec instanceof Monster){
            this.lives = this.lives -1;
            //System.out.println(this.lives);
        }
        if(dec instanceof Key){
            this.takeKey(nextPos);
        }
        if(dec instanceof Heart){
            this.lives = this.lives + 1;
            this.game.getGrid().get(nextPos).remove();
            this.game.getGrid().remove(nextPos);
            }
        if(dec instanceof BombNumberDec && this.nbBomb > 1){
            this.nbBomb = this.nbBomb - 1;
            this.game.getGrid().get(nextPos).remove();
            this.game.getGrid().remove(nextPos);
            }
        if(dec instanceof BombNumberInc){
            this.nbBomb = this.nbBomb + 1;
            this.game.getGrid().get(nextPos).remove();
            this.game.getGrid().remove(nextPos);
            }
        if(dec instanceof BombRangeDec){
            if(this.bombRange >= 2 ){
                this.bombRange = this.bombRange - 1;
                this.game.getGrid().get(nextPos).remove();
                this.game.getGrid().remove(nextPos);
                }
            }
        if(dec instanceof BombRangeInc){
            this.bombRange = this.bombRange + 1;
            this.game.getGrid().get(nextPos).remove();
            this.game.getGrid().remove(nextPos);
            }
        if(dec instanceof DoorNextOpened){
            takeDoor(getActualLevel()+1);
            }
        if(dec instanceof DoorPrevOpened){
            takeDoor(getActualLevel()-1);
        }

    }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }

    @Override
    public void explode() {
    }

    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {
        String path = getClass().getResource("/sample").getFile();
        Game nextGame = new Game(path);
    }

    public void openDoor(){
        Position nextPos = direction.nextPosition(getPosition());
        if(this.game.getGrid().get(nextPos) instanceof DoorNextClosed && this.getNbKey() > 0 ){
            DoorNextOpened  d = new DoorNextOpened(nextPos);
            this.game.getGrid().remove(nextPos);
            this.game.getGrid().set(nextPos, d);
            this.nbKey  = this.nbKey - 1;
            //il faut update l'affichage
        }
    }

    public void takeKey(Position pos) {
        this.nbKey = this.nbKey + 1;
        this.game.getGrid().get(pos).remove();
        this.game.getGrid().remove(pos);
        //System.out.println(this.game.getGrid().get(pos));
    }

    public void takeBonus(){}

    public boolean isDoor() {
        Decor obj = this.game.getGrid().get(this.game.getPlayer().getPosition());
        if (obj instanceof Door) {
            return true;
        }
        return false;
    }

    public boolean isWinner() {
        Decor obj = this.game.getGrid().get(this.game.getPlayer().getPosition());
        if (obj instanceof Princess) {
            return true;
        }
        return false;
    }
}
