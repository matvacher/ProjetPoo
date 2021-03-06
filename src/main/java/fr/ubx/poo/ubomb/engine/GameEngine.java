/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;
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

import javax.swing.plaf.synth.SynthOptionPaneUI;
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
    private int actualLevel = 1;

    //bombe
    /*
    private Timer time = new Timer(0);
    private boolean DisparitionExplosion = false;
    private boolean debutExplosion = true;
    private Position posBomb;
    private Decor[] tabExplosion;
    private Decor actualBomb = new Bomb(null , time);
    private int count_bomb = 0 ;

     */
    private Bomb[] tabBomb;
    private int sizeTabBomb = 0;


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.stage = stage;
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        // Bombe
        this.tabBomb = new Bomb[game.bombBagCapacity];
        //
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
                //checkExplosions();
                for (int i = 0; i < sizeTabBomb; i++) {
                    tabBomb[i].setState(now);
                }
                if(game.getGrid().get(player.getPosition()) instanceof Monster){
                    player.checkPlayerlive(now);
                }

                /*for(int i = 0 ; i < game.getGrid().getWidth(); i++){
                    for(int j = 0 ; j < game.getGrid().getHeight(); j++){
                        Position pos = new Position(i,j);
                        Decor dec = game.getGrid().get(pos);
                        if(dec instanceof Monster){
                            Monster monster = (Monster) dec;
                            monster.moveRequested(now);
                            monster.updatePos(now, game);
                        }
                    }
                }*/

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);

                if( actualLevel == player.getActualLevel() - 1){
                    actualLevel = player.getActualLevel();
                    for(int i = 0 ; i < game.getGrid().getWidth(); i++){
                        for(int j = 0 ; j < game.getGrid().getHeight(); j++){
                            Position pos = new Position(i,j);
                            Decor dec = game.getGrid().get(pos);
                            if(dec instanceof DoorPrevOpened){
                                player.setPosition(pos);
                            }
                        }
                    };
                    //initialize(); //si on initialise , on obtient la map du level voulu mais le player n'est plus affiché
                    //sprites.add(new SpritePlayer(layer, game.getPlayer()));

                }
                if( actualLevel == player.getActualLevel() + 1){
                    actualLevel = player.getActualLevel();
                    for(int i = 0 ; i < game.getGrid().getWidth(); i++){
                        for(int j = 0 ; j < game.getGrid().getHeight(); j++){
                            Position pos = new Position(i,j);
                            Decor dec = game.getGrid().get(pos);
                            if(dec instanceof DoorNextOpened){
                                player.setPosition(pos);
                            }
                        }
                    }
                    initialize();
                    //sprites.add(new SpritePlayer(layer, game.getPlayer()));

                }
            }
        };
    }
    /*
    private void checkExplosions(Position pos,long now) {

        if(pos.equals(player.getPosition())){
            player.checkPlayerlive(now);
        }
    }

    private void createNewBombs(long now) {


        actualBomb.setPosition(posBomb);
        Bomb0 bomb0 = new Bomb0(posBomb);
        Bomb1 bomb1 = new Bomb1(posBomb);
        Bomb2 bomb2 = new Bomb2(posBomb);
        Bomb3 bomb3 = new Bomb3(posBomb);
        long sec = 1000000000;

        if(DisparitionExplosion) {

            if( time.delay(now ) > 0 && count_bomb == 0){ // affichage evolution bombe
                actualBomb = bomb3;
                sprites.add(SpriteFactory.create(layer, actualBomb));
                count_bomb ++;
            }
            if( time.delay(now ) >=  sec && count_bomb == 1){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb2;
                //new Sprite(layer,  getBomb(3 - count_bomb), actualBomb);
                sprites.add(SpriteFactory.create(layer, actualBomb));
                count_bomb ++;
            }
            if( time.delay(now ) >= 2 * sec && count_bomb == 2){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb1;
                sprites.add(SpriteFactory.create(layer, actualBomb));
                count_bomb ++;
            }
            if( time.delay(now ) >= 3 * sec && count_bomb == 3){ // affichage evolution bombe
                actualBomb.remove();
                actualBomb = bomb0;
                sprites.add(SpriteFactory.create(layer, actualBomb));
                count_bomb ++;
            }



            if( (time.delay(now) >= 4 * sec)  && debutExplosion ) {
                debutExplosion = false;
                int size = 4* player.getBombRange();
                tabExplosion = new Decor[size];
                //sprites.add(SpriteFactory.create(layer, bomb0)); //affichage de la bombe
                int cpt = 0;

                checkExplosions(posBomb,now);
                // affichage range explosion
                for (int i = 1; i < player.getBombRange() + 1; i++) {
                    //Left
                    Position pos0 = new Position(posBomb.getX() - i, posBomb.getY());
                    Decor dec0 = this.game.getGrid().get(pos0);
                    Explosion explosionLeft = new Explosion(pos0);

                    if(dec0 != null){
                        dec0.explode();
                        i = player.getBombRange();
                        tabExplosion[cpt] = dec0;
                    }
                    else{

                        sprites.add(SpriteFactory.create(layer, explosionLeft));
                        tabExplosion[cpt] = explosionLeft;
                    }
                    checkExplosions(pos0,now);
                    cpt++;
                }
                    //Right
                for (int i = 1; i < player.getBombRange() + 1; i++) {
                    Position pos1 = new Position(posBomb.getX() + i, posBomb.getY());
                    Decor dec1 = this.game.getGrid().get(pos1);
                    if(dec1 != null){
                        dec1.explode();
                        i = player.getBombRange();
                        tabExplosion[cpt] = dec1;
                    }
                    else{
                        Explosion explosionRight = new Explosion(pos1);
                        sprites.add(SpriteFactory.create(layer, explosionRight));
                        tabExplosion[cpt] = explosionRight;
                    }
                    checkExplosions(pos1,now);
                    cpt ++;
                }
                    //Up
                for (int i = 1; i < player.getBombRange() + 1; i++) {
                    Position pos2 = new Position(posBomb.getX(), posBomb.getY() - i);
                    Decor dec2 = this.game.getGrid().get(pos2);

                    if(dec2 != null){
                        dec2.explode();
                        i = player.getBombRange();
                        tabExplosion[cpt] = dec2;
                    }
                    else{
                        Explosion explosionUp = new Explosion(pos2);
                        sprites.add(SpriteFactory.create(layer, explosionUp));
                        tabExplosion[cpt] = explosionUp;
                    }
                    checkExplosions(pos2,now);
                    cpt ++;
                }
                    //Down
                for (int i = 1; i < player.getBombRange() + 1; i++) {
                    Position pos3 = new Position(posBomb.getX(), posBomb.getY() + i);
                    Decor dec3 = this.game.getGrid().get(pos3);

                    if(dec3 != null){
                        dec3.explode();
                        i = player.getBombRange();
                        tabExplosion[cpt] = dec3;
                    }
                    else{
                          Explosion explosionDown = new Explosion(pos3);
                          sprites.add(SpriteFactory.create(layer, explosionDown));
                          tabExplosion[cpt] = explosionDown;
                    }
                    checkExplosions(pos3,now);
                    cpt ++;
                }
            }

            //efface les explosions
            if (time.delay(now) >= 5*sec) {
                DisparitionExplosion = false;
                if(tabExplosion[0] != null){
                    Position position;
                    actualBomb.remove();
                    player.setNbBomb(player.getNbBomb() + 1);
                    for (int i = 0; i < 4* player.getBombRange(); i++) {

                        position = tabExplosion[i].getPosition();

                        if (game.getGrid().get(position) != null){
                            Decor obj = game.getGrid().get(position);
                            Decor dec = new Stone(position);
                            //tabExplosion[i].remove();
                            game.getGrid().set(position,dec);
                            obj.setPosition(position);

                            //sprites.add(SpriteFactory.create(layer, obj));
                        }
                        else{
                            tabExplosion[i].remove();
                        }
                    }
                }

            }
        }
    }
    */

    private void addBomb(long now){
        if(sizeTabBomb < tabBomb.length) {
            Bomb newBomb = new Bomb(player.getPosition());
            newBomb.getTime().setTime(now);
            sizeTabBomb++;
            tabBomb[sizeTabBomb - 1] = newBomb;
            newBomb.setTabImageBomb();
            newBomb.setRange(player.getBombRange());
        }
    }

    private void checkExplosions(Position pos,long now) {
        if(pos.equals(player.getPosition())){
            player.checkPlayerlive(now);
        }
    }

    private Decor tmp = new Bomb(null);

    private void createNewBombs(long now) {
            for (int i = 0; i < sizeTabBomb; i++) {
                if(tabBomb[i].getDisplay() == false){
                    tabBomb[i].getImageBomb(0).remove();
                }
                if (tabBomb[i] != null && tabBomb[i].getDisplay() && !tabBomb[i].getModified()) {
                    int state = tabBomb[i].getState();
                    if (state != 4){
                        tabBomb[i].getImageBomb(state).setStatus(state);
                        if(state != 3){
                            tabBomb[i].getImageBomb(state+1).remove();
                        }
                        sprites.add(SpriteFactory.create(layer, tabBomb[i].getImageBomb(state)));
                        tabBomb[i].setModified();
                    }
                }
            }
            /*
            if(tabBomb[0].isExplosionReady()){
                System.out.println("toto");
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
            if(player.getNbBomb() > 0) {
                /*
                player.setNbBomb(player.getNbBomb() - 1);
                posBomb = player.getPosition();
                time.setTime(now);
                DisparitionExplosion = true;
                debutExplosion = true;
                count_bomb = 0;
                */
                addBomb(now);


                /*
                System.out.println("tableau bombe :");
                for (int i = 0; i < tabBomb.length; i++) {
                    System.out.printf("Bombe num " + i + " soit : " + tabBomb[i] );
                    if (tabBomb[i] != null) {
                        System.out.printf(" En position " + tabBomb[i].getPosition());
                    }
                    System.out.println("");
                }
                 */
            }
        } else if (input.isKey()) {
            if(player.openDoor()) {
                Position nextPos = player.getDirection().nextPosition(player.getPosition());
                DoorNextOpened door = new DoorNextOpened(nextPos);
                sprites.add(SpriteFactory.create(layer, door));
            }
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
