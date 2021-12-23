 /*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Game {

    public final int bombBagCapacity;
    public final int monsterVelocity;
    public final int playerLives;
    public final int levels;
    public final long playerInvisibilityTime;
    public final long monsterInvisibilityTime;
    //private final Grid [] grid;
    private final Grid grid;
    private final Player player;

    public Game(String worldPath) {
        try (InputStream input = new FileInputStream(new File(worldPath, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            bombBagCapacity = Integer.parseInt(prop.getProperty("bombBagCapacity", "3"));
            monsterVelocity = Integer.parseInt(prop.getProperty("monsterVelocity", "10"));
            levels = Integer.parseInt(prop.getProperty("levels", "3"));
            playerLives = Integer.parseInt(prop.getProperty("playerLives", "3"));
            playerInvisibilityTime = Long.parseLong(prop.getProperty("playerInvisibilityTime", "4000"));
            monsterInvisibilityTime = Long.parseLong(prop.getProperty("monsterInvisibilityTime", "1000"));

            // Load the world
            String prefix = prop.getProperty("prefix");
            GridRepo gridRepo = new GridRepoSample(this);
            this.grid = gridRepo.load(1, prefix + 1);
            /*this.grid = new Grid[levels];
            for(int i = 1 ; i < levels + 1 ; i++){
                this.grid[i-1] = gridRepo.load(i, prefix + i);
                System.out.println(this.grid[i-1]);
            }*/
            // Create the player
            String[] tokens = prop.getProperty("player").split("[ :x]+");
            if (tokens.length != 2)
                throw new RuntimeException("Invalid configuration format");
            Position playerPosition = new Position(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            player = new Player(this, playerPosition, playerLives);



        } catch (IOException ex) {
            System.err.println("Error loading configuration");
            throw new RuntimeException("Invalid configuration format");
        }
    }

    public Grid getGrid() {
        //return grid[getPlayer().getActualLevel()-1];
        return grid;
    }

    // Returns the player, monsters and bombs at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (getPlayer().getPosition().equals(position))
            gos.add(player);
        return gos;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean inside(Position position) {

        // Dans le quadrillage
        int x = position.getX();
        int y = position.getY();
        /*int max_x = grid[player.getActualLevel()].getWidth() - 1;
        int max_y = grid[player.getActualLevel()].getHeight() - 1;*/
        int max_x = grid.getWidth() - 1;
        int max_y = grid.getHeight() - 1;
        return x <= max_x && y <= max_y && x >= 0 && y >= 0;

    }

}
