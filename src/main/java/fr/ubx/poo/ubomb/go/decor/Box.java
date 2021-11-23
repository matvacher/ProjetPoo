package fr.ubx.poo.ubomb.go.decor;

import com.sun.source.tree.NewArrayTree;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import javafx.geometry.Pos;

public class Box extends Decor {
    public Box(Position position) {
        super(position);
    }

    public Boolean moveBox(Game game, Direction direction){
        Position posBox = getPosition();
        Position nextPosBox = direction.nextPosition(posBox);

        // condition : La box peut etre déplacée
        if (  !game.inside(nextPosBox) || game.getGrid().get(nextPosBox) != null){
            return false;
        }

        this.setPosition(nextPosBox);        // set affichage
        game.getGrid().remove(posBox);       // remove box grid
        game.getGrid().set(nextPosBox,this); // set new box grid
        return true;
    }
}
