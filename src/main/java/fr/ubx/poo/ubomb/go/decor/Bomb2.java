package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb2 extends Decor {
    public Bomb2(Position position) {
        super(position);
    }

    public boolean isWalkable(Player player) {
        return true;}
}

