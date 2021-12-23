package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb3 extends Bomb {
    public Bomb3(Position position) {
        super(position);
    }

    public boolean isWalkable(Player player) {
        return true;}
}

