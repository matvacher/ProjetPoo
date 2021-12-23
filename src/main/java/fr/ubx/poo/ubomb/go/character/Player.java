/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import com.sun.marlin.DRenderer;
import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.*;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.geometry.Pos;
import fr.ubx.poo.ubomb.engine.Input;
import fr.ubx.poo.ubomb.engine.*;
import javafx.stage.Stage;

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
     private Timer time = new Timer(0);
     private boolean invulnerability = false;

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

    public void setActualLevel(int i){
        this.actualLevel = i ;
    }

    public void setTime(long now){
        this.time.setTime(now);
    }


    public void setInvulnerability(boolean state){
        this.invulnerability = state;
    }

    public boolean getInvulnerability(){
        return invulnerability;
    }

    public void checkPlayerlive(long now){
        long sec = 1000000000;

        if (time.delay(now) >=  sec){
            setInvulnerability(false);
            time.setTime(0);
        }

        if (!getInvulnerability()){
            setLives(getLives() - 1);
            setInvulnerability(true);
            setTime(now);
        }
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
        if(this.game.inside(nextPos) && (obj == null || obj.isWalkable()) ) {
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
        if(dec instanceof Key){
            this.takeKey(nextPos);
        }
        if(dec instanceof Heart){
            this.lives = this.lives + 1;
            this.game.getGrid().get(nextPos).remove();
        }
        if(dec instanceof BombNumberDec && this.nbBomb > 1){
            this.nbBomb = this.nbBomb - 1;
            this.game.getGrid().get(nextPos).remove();
            }
        if(dec instanceof BombNumberInc){
            this.nbBomb = this.nbBomb + 1;
            this.game.getGrid().get(nextPos).remove();
            }
        if(dec instanceof BombRangeDec){
            if(this.bombRange >= 2 ){
                this.bombRange = this.bombRange - 1;
                this.game.getGrid().get(nextPos).remove();
                }
            }
        if(dec instanceof BombRangeInc){
            this.bombRange = this.bombRange + 1;
            this.game.getGrid().get(nextPos).remove();
            }
        if(dec instanceof DoorNextOpened){
             setActualLevel(this.actualLevel + 1);
            }
        if(dec instanceof DoorPrevOpened){
            setActualLevel(this.actualLevel - 1 );
        };

    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public void explode() {
    }

    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {

    }

    /*
    public void openDoor(){
        Position nextPos = direction.nextPosition(getPosition());
        Decor dec = this.game.getGrid().get(nextPos);
        if(dec instanceof DoorNextClosed && this.getNbKey() > 0){
            this.game.getGrid().remove(nextPos);
            DoorNextClosed d = new DoorNextClosed(nextPos);
            d.openDoor(nextPos);
            this.nbKey  = this.nbKey - 1;
        }


        if(this.game.getGrid().get(nextPos) instanceof DoorNextClosed && this.getNbKey() > 0 ){
            DoorNextOpened  d = new DoorNextOpened(nextPos);
            d.setPosition(nextPos);
            this.game.getGrid().remove(nextPos);
            this.game.getGrid().set(nextPos, d);
            System.out.println(game.getGrid().get(nextPos));;
            this.nbKey  = this.nbKey - 1;
            //il faut update l'affichage

        }
    }
     */

    public boolean openDoor(){
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getGrid().get(nextPos);

        if(decor instanceof DoorNextClosed && this.getNbKey() > 0 ){
            DoorNextOpened  d = new DoorNextOpened(nextPos);
            d.setPosition(nextPos);
            this.game.getGrid().remove(nextPos);
            this.game.getGrid().set(nextPos, d);
            this.nbKey  = this.nbKey - 1;
            return true;
        }
        return false;
    }


    public void takeKey(Position pos) {
        this.nbKey = this.nbKey + 1;
        this.game.getGrid().get(pos).remove();
    }

    public void takeBonus(){
    }

    public int isDoor() {/*
        Decor obj = this.game.getGrid().get(this.game.getPlayer().getPosition());
        if (obj instanceof DoorNextOpened) {
            return 1;
        }
        else if (obj instanceof DoorPrevOpened){
            return -1;
        }
        else{
            return 0;
        }*/
        return 0;
    }

    public boolean isWinner() {
        Decor obj = this.game.getGrid().get(this.game.getPlayer().getPosition());
        if (obj instanceof Princess) {
            return true;
        }
        return false;
    }
}
