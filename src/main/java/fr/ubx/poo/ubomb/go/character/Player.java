/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import com.sun.marlin.DRenderer;
import fr.ubx.poo.ubomb.game.*;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;


public class Player extends GameObject implements Movable {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;

    public Player(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
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

    public void takeKey() {}

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
