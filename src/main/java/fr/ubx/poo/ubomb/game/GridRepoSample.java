package fr.ubx.poo.ubomb.game;

import java.lang.reflect.Field;

import static fr.ubx.poo.ubomb.game.EntityCode.*;

public class GridRepoSample extends GridRepo {

    private final EntityCode[][] level1 = {
            {Stone, Empty, Heart, Empty, Empty, Empty, Empty, Empty, Empty, Empty, BombRangeDec, Empty},
            {Empty, Stone, Stone, Empty, Stone, Empty, Stone, Stone, Stone, Stone, Empty, Empty},
            {Empty, BombNumberInc, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Stone, Stone, Empty, Empty, Empty, Empty, Empty},
            {Empty, Box, Empty, Empty, Empty, Empty, Empty, Key, Empty, Stone, Empty, Empty},
            {Empty, Tree, Empty, Tree, Empty, DoorNextClosed, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Box, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Tree, Tree, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, BombRangeInc, Empty, Empty, Empty, Empty, Empty},
            {Stone, Stone, Stone, Stone, Stone, Empty, Box, Box, Stone, Stone, Box, Stone},
            {Empty, Monster, Empty, Empty, Box, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Box, Empty, Empty, Empty, BombNumberInc, Empty, Empty, Princess}
    };

    private final EntityCode[][] level2 = {
            {Key, Empty, Tree, Empty, Empty, Empty, Monster, Empty},
            {Empty, Empty, Tree, Empty, Tree, Empty, Empty, Empty},
            {Empty, Monster, Tree, Empty, Empty, Empty, Empty, DoorNextClosed},
            {Stone, Empty, Box, Empty, Tree, BombRangeInc, Empty, Empty},
            {Empty, Box, Stone, Box, Tree, Stone, Stone, Tree},
            {Empty, Empty, Empty, Empty, Tree, Tree, Tree, Tree},
            {Tree, Stone, Empty, Empty, Empty, BombRangeDec, Empty, Empty},
            {Heart, Stone, Empty, Monster, Empty, BombNumberDec, Empty, Monster},
            {Box, Box, Stone, Empty, Stone, Empty, Empty, Empty},
            {Box, Box, Box, BombRangeInc, Stone, Empty, Empty, DoorPrevOpened}
    };

    public GridRepoSample(Game game) {
        super(game);
    }

    @Override
    public final Grid load(int level, String name) {
        EntityCode[][] entities = getEntities(name);
        if (entities == null)
            return null;
        int width = entities[0].length;
        int height = entities.length;
        Grid grid = new Grid(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(i, j);
                EntityCode entityCode = entities[j][i];
                grid.set(position, processEntityCode(entityCode, position));
            }
        }

        return grid;
    }

    private final EntityCode[][] getEntities(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            return (EntityCode[][]) field.get(this);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
