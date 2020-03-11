import java.awt.Graphics2D;

public abstract class GameEntity {

    // fields
    protected GamePanel game;
    protected double xPos;
    protected double yPos;
    protected boolean dead;

    // constructors
    public GameEntity(GamePanel game, double xPos, double yPos) {
        this.game = game;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // abstract methods
    public abstract void passiveUpdate(Graphics2D g); // called on repaint()
    public abstract void logicalUpdate();
    public abstract void onClickUpdate(); // called on mouse click

    // getters
    public double getxPos() {return xPos;}
    public double getyPos() {return yPos;}
    public boolean isDead() {return dead;}

    // setters
    public void setxPos(double xPos) {this.xPos = xPos;}
    public void setyPos(double yPos) {this.yPos = yPos;}
    public void setDead(boolean dead) {this.dead = dead;}
}
