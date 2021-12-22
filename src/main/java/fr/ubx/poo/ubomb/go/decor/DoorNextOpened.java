package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;


public class DoorNextOpened extends Decor{
    public DoorNextOpened(Position position) {
        super(position);
    }
    @Override
    public boolean isWalkable() {
        return true;
    }
}