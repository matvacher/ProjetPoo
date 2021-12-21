/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Bomb0;
import fr.ubx.poo.ubomb.go.decor.Bomb1;
import fr.ubx.poo.ubomb.go.decor.Bomb2;
import fr.ubx.poo.ubomb.go.decor.Bomb3;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Explosion;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;

    //bombe
    private Timer time = new Timer(0);
    private boolean DisparitionExplosion = false;
    private boolean debutExplosion = true;
    private Explosion[] tabExplosion;
    private int count_bomb = 0;
    private Decor actualBomb;


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.stage = stage;
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        for (Decor decor : game.getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }
        sprites.add(new SpritePlayer(layer, player));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);
                createNewBombs(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {
    }

    private void createNewBombs(long now) {
        Position pos = player.getPosition();

        Bomb0 bomb0 = new Bomb0(pos);
        Bomb1 bomb1 = new Bomb1(pos);
        Bomb2 bomb2 = new Bomb2(pos);
        Bomb3 bomb3 = new Bomb3(pos);
        long sec = 1000000000;

        if(DisparitionExplosion) {

            if( time.delay(now ) < sec && count_bomb == 0){ // affichage evolution bombe
                actualBomb = bomb3;
                sprites.add(SpriteFactory.create(layer, bomb3));
                count_bomb ++;
            }
            if( time.delay(now ) < 2 * sec && count_bomb == 1){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb2;
                sprites.add(SpriteFactory.create(layer, bomb2));
                count_bomb ++;
            }
            if( time.delay(now ) < 3 * sec && count_bomb == 2){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb1;
                sprites.add(SpriteFactory.create(layer, bomb1));
                count_bomb ++;
            }
            if( time.delay(now ) < 4 * sec && count_bomb == 3){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb0;
                sprites.add(SpriteFactory.create(layer, bomb3));
                count_bomb ++;
            }



            if( (time.delay(now) >= 1 * sec)  && debutExplosion ) {
                debutExplosion = false;
                int size = 4* player.getBombRange();
                tabExplosion = new Explosion[size];
                sprites.add(SpriteFactory.create(layer, bomb0)); //affichage de la bombe
                int cpt = 0;

                // affichage range explosion
                for (int i = 1; i < player.getBombRange() + 1; i++) {
                    //Left
                    Explosion explosionLeft = new Explosion(new Position(pos.getX() - i, pos.getY()));
                    sprites.add(SpriteFactory.create(layer, explosionLeft));
                    tabExplosion[cpt] = explosionLeft;
                    cpt ++;
                    //Right
                    Explosion explosionRight = new Explosion(new Position(pos.getX() + i, pos.getY()));
                    sprites.add(SpriteFactory.create(layer, explosionRight));
                    tabExplosion[cpt] = explosionRight;
                    cpt ++;
                    //Up
                    Explosion explosionUp = new Explosion(new Position(pos.getX(), pos.getY() - i));
                    sprites.add(SpriteFactory.create(layer, explosionUp));
                    tabExplosion[cpt] = explosionUp;
                    cpt ++;
                    //Down
                    Explosion explosionDown = new Explosion(new Position(pos.getX(), pos.getY() + i));
                    sprites.add(SpriteFactory.create(layer, explosionDown));
                    tabExplosion[cpt] = explosionDown;
                    cpt ++;
                }
            }

            //efface les explosions
            if (time.delay(now) >= 2*sec) { // possible de multiplier sec pour avoir 0.5s ou 2s de delay ect ...
                DisparitionExplosion = false;
                if(tabExplosion[0] != null){
                    Position position;
                    bomb0.remove();
                    for (int i = 0; i < 4* player.getBombRange(); i++) {

                        /*
                        position = tabExplosion[i].getPosition();

                        if (game.getGrid().get(position) != null){
                            Decor obj = game.getGrid().get(position);
                            tabExplosion[i].remove();
                            game.getGrid().set(position,obj);
                            obj.setPosition(position);
                        }

                         */

                        tabExplosion[i].remove();
                    }
                }

            }
        }
    /*
    //Timer ---> affiche "disparition toute les secondes
    if (time.delay(now) >= sec) {
        time.setTime(now);

        System.out.println("Disparition");

    }
    */
    }

    private void checkCollision(long now) {
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
            input.clear();
        } else if (input.isBomb()){
            System.out.println("Bombe ");
            time.setTime(now);
            DisparitionExplosion = true;
            debutExplosion = true;
            count_bomb = 0;
        }

        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        if (player.isDoor()) {
            gameLoop.start();
            player.takeDoor(2);
            //showMessage("Porte Prise", Color.PURPLE);
        }


    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.getGrid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }
}
