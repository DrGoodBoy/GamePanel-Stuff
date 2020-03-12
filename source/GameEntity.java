/*
 * Programmer: Wyatt Rose
 *
 * Last Edited: 3/11/20
 *
 * Created: 2/16/20
 *
 * Description: The GameEntity class provides building blocks for making sprites in a GamePanel. Any sprite that you
 * want to create should extend this class to get all of the benefits of GamePanel! You can alter access modifiers to
 * your liking and it will have no effect on program execution. You can append your entities to the GamePanel by using
 * initializeStartingEntities() inside GamePanel. This class is not as viable if you aren't using a GamePanel.
 */

import java.awt.Graphics2D;

public abstract class GameEntity {

    // FIELDS (make them protected if you want)
    private GamePanel game; // current game this entity is in
    private boolean dead; // if the sprite is ready to be terminated
    private double xPos; // current x position of the entity
    private double yPos; // current y position of the entity

    // CONSTRUCTORS
    public GameEntity() { // for testing purposes
        this.game = null;
        this.xPos = 0;
        this.yPos = 0;
        this.dead = false;
    }

    public GameEntity(GamePanel game) {
        this();
        this.game = game;
    }

    public GameEntity(GamePanel game, double xPos, double yPos) {
        this(game);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // ABSTRACT METHODS
    public abstract void graphicalUpdate(Graphics2D g); // graphical Updates in GamePanel
    public abstract void logicalUpdate(); // game tick updates in GamePanel
    public abstract void onClickUpdate(); // on click updates in GamePanel

    // GETTERS
    public GamePanel getGame() {return game;};
    public boolean isDead() {return dead;}
    public double getxPos() {return xPos;}
    public double getyPos() {return yPos;}

    // SETTERS
    public void setDead(boolean dead) {this.dead = dead;}
    public void setxPos(double xPos) {this.xPos = xPos;}
    public void setyPos(double yPos) {this.yPos = yPos;}
}
