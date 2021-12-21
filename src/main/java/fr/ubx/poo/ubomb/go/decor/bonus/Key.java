/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Key extends Bonus {
    public Key(Position position) {
        super(position);
    }

    public void takenBy(Player player) {
        player.takeKey(this.getPosition());
    }

    @Override
    public void explode() {
    }
}
