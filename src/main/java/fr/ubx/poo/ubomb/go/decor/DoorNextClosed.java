package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.go.character.Player;


public class DoorNextClosed extends Decor {
    public DoorNextClosed(Position position) {
        super(position);
    }
    /*
    public void openDoor(Position nextPos){
        if(this.game.getGrid().get(nextPos) instanceof DoorNextClosed ){
            DoorNextOpened  d = new DoorNextOpened(nextPos);
            this.game.getGrid().set(nextPos, d);
            //il faut update l'affichage
        }
    }

     */
}