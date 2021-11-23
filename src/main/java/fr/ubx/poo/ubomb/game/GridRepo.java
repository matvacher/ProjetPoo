package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;


public abstract class GridRepo {

    private final Game game;

    GridRepo(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Grid load(int level, String name);

    Decor processEntityCode(EntityCode entityCode, Position pos) {
        switch (entityCode) {
            case Empty:
                return null;
            case Stone:
                return new Stone(pos);
            case Heart:
                return new Heart(pos);
            case Tree:
                return new Tree(pos);
            case Key:
                return new Key(pos);
            case Box:
                return new Box(pos);
            case Monster:
                return new Monster(pos);
            case BombRangeDec:
                return new BombRangeDec(pos);
            case BombRangeInc:
                return new BombRangeInc(pos);
            case Princess:
                return new Princess(pos);
            case BombNumberDec:
                return new BombNumberDec(pos);
            case BombNumberInc:
                return new BombNumberInc(pos);
            case DoorNextClosed:
                return new Door(pos);
            default:
                return null;
                //throw new RuntimeException("EntityCode " + entityCode.name() + " not processed");
        }
    }
}
