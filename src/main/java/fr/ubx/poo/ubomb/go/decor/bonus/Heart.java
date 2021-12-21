/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Heart extends Bonus {
    public Heart(Position position) {
        super(position);
    }

    @Override
    public void takenBy(Player player) {
        player.takeBonus();
    }
}

