/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubomb.view.ImageResource.*;


public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        if (gameObject instanceof Stone)
            return new Sprite(layer, STONE.getImage(), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, TREE.getImage(), gameObject);
        if (gameObject instanceof Key)
            return new Sprite(layer, KEY.getImage(), gameObject);
        if (gameObject instanceof Heart)
            return new Sprite(layer, HEART.getImage(), gameObject);
        if (gameObject instanceof Box)
            return new Sprite(layer, BOX.getImage(), gameObject);
        if (gameObject instanceof Monster)
            return new Sprite(layer, MONSTER_DOWN.getImage(), gameObject);
        if (gameObject instanceof BombRangeInc)
            return new Sprite(layer, BONUS_BOMB_RANGE_INC.getImage(), gameObject);
        if (gameObject instanceof BombRangeDec)
            return new Sprite(layer, BONUS_BOMB_RANGE_DEC.getImage(), gameObject);
        if (gameObject instanceof Princess)
            return new Sprite(layer, PRINCESS.getImage(), gameObject);
        if (gameObject instanceof BombNumberDec)
            return new Sprite(layer, BONUS_BOMB_NB_DEC.getImage(), gameObject);
        if (gameObject instanceof BombNumberInc)
            return new Sprite(layer, BONUS_BOMB_NB_INC.getImage(), gameObject);
        if (gameObject instanceof DoorNextClosed)
            return new Sprite(layer, DOOR_CLOSED.getImage(), gameObject);
        if (gameObject instanceof DoorNextOpened)
            return new Sprite(layer, DOOR_OPENED.getImage(), gameObject);
        if (gameObject instanceof DoorPrevOpened)
            return new Sprite(layer, DOOR_OPENED.getImage(), gameObject);

        /// autre bombes
        if (gameObject instanceof Explosion)
            return new Sprite(layer, EXPLOSION.getImage(), gameObject);
        if (gameObject instanceof Bomb)
            return new Sprite(layer, getBomb(((Bomb) gameObject).getState()) , gameObject);
        if (gameObject instanceof Bomb1)
            return new Sprite(layer, BOMB_1.getImage(), gameObject);
        if (gameObject instanceof Bomb0)
            return new Sprite(layer, BOMB_0.getImage(), gameObject);
        if (gameObject instanceof Bomb2)
            return new Sprite(layer, BOMB_2.getImage(), gameObject);
        if (gameObject instanceof Bomb3)
            return new Sprite(layer, BOMB_3.getImage(), gameObject);

        throw new RuntimeException("Unsupported sprite for decor " + gameObject);
    }
}
