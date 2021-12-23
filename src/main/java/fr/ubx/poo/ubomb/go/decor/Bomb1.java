package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb1 extends Bomb {
    public Bomb1(Position position) {
        super(position);
    }

    public boolean isWalkable(Player player) {
        return true;}
}

