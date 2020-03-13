/*
 * Programmer: Wyatt Rose
 *
 * Last Edited: 3/12/20
 *
 * Created: 2/5/20
 *
 * Description: com.stuff.GamePanel is a class that helps making games easier by handling
 * keyboard and mouse inputs as well as graphical updates. It is fairly easy to
 * use and is much more tuned to game creation than DrawingPanel. com.stuff.GamePanel is
 * also viable when trying to create animated images, as there is an easy way
 * to create Entities that can exist on the Panel.
 *
 * KeyCodes: https://docs.oracle.com/en/java/javase/12/docs/api/constant-values.html#java.awt.event.KeyEvent.VK_0
 * Graphics2D documentation: https://docs.oracle.com/en/java/javase/12/docs/api/java.desktop/java/awt/Graphics2D.html
 */

// IMPORTS..............................................................................................................

package com.main;
import com.entitygroups.*;
import com.itemgroups.*;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

// GAMEPANEL............................................................................................................
public class GamePanel extends JPanel implements MouseMotionListener {

    // CLASS CONSTANTS..................................................................................................
    private static final long serialVersionUID = 1L; // used for serialization
    private static final String frameName = "com.stuff.GamePanel"; // the name of the window
    private static final int panelSizeX = 800; // length of the panel
    private static final int panelSizeY = 800; // width of the panel
    private static final int tickSpeed = 10; // delay in milliseconds between each game tick
    private static final boolean suspendFrameVisibility = true;

    // ENTITY MANIPULATION FIELDS.......................................................................................
    private CopyOnWriteArrayList<GameEntity> entities = new CopyOnWriteArrayList<>(); // stores all entities

    // MOUSE AND KEY FIELDS.............................................................................................
    private int mouseX; // current position of the mouse
    private int mouseY; // current position of the mouse
    private ArrayList<Integer> mouseButtonsDown = new ArrayList<>(); // current buttons being pressed down
    private ArrayList<Integer> keysDown = new ArrayList<>(); // current keys being pressed down

    // CONSTRUCTORS
    public GamePanel() { // default constructor

        setFocusable(true); // allows for keyboard input

        addMouseMotionListener(this); // starts mouse motion listener on this component

        addMouseListener(new MouseAdapter() { // starts a mouse listener on this component
            @Override
            public void mouseClicked(MouseEvent e) { } // called on mouse click

            @Override
            public void mousePressed(MouseEvent e) { // called on mouse press
                updateMousePosition(e);
                updateMousePressed(e);
                onClickUpdateAll();
            }

            @Override
            public void mouseReleased(MouseEvent e) { // called on mouse release
                updateMousePosition(e);
                updateMouseReleased(e);
            }
        });

        addKeyListener(new KeyListener() { // starts a key listener on this component
            @Override
            public void keyTyped(KeyEvent e) { } // called on key click

            @Override
            public void keyPressed(KeyEvent e) { updateKeyPressed(e); } // called on key press

            @Override
            public void keyReleased(KeyEvent e) { updateKeyReleased(e); } // called on key release
        });

        initializeStartingEntities(); // adds all starting entities to the entities array

        Timer timer = new Timer("tick"); // creates a new timer

        timer.schedule(new TimerTask() { // calls run every 'n' milliseconds, where 'n' is tickSpeed
            @Override
            public void run() {
                logicalUpdateAll(); // calls all entities' logical methods
            }
        }, 0, tickSpeed); // starts this timer immediately

        Thread paintThread = new Thread(() -> { // thread devoted to calling repaint() for animation
            while(true) {
                repaint(); // calls paintComponent(Graphics g)
            }
        });

        Thread purgeThread = new Thread(() -> { // thread devoted to purging dead entities
            while(true) {
                purgeDeadEntities(); // removes all dead entities
            }
        });

        paintThread.start(); // starts execution of the paintThread
        purgeThread.start(); // starts execution of the purgeThread
    }

    // MOUSE MOVEMENT METHODS...........................................................................................
    @Override
    public void mouseMoved(MouseEvent e) { updateMousePosition(e); } // called when mouse changes position

    @Override
    public void mouseDragged(MouseEvent e) { updateMousePosition(e); } // called when position changes while pressed

    // INPUT UPDATE METHODS.............................................................................................
    public void updateMousePosition(MouseEvent e) { // updates com.stuff.GamePanel's fields to match current mousePosition
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void updateMousePressed(MouseEvent e) { // update com.stuff.GamePanel's mouseButtonsDown to match current buttons
        if(!mouseButtonsDown.contains(e.getButton())) {
            mouseButtonsDown.add(e.getButton());
        }
    }

    public void updateMouseReleased(MouseEvent e) { // update com.stuff.GamePanel's mouseButtonsDown to match current buttons
        if(mouseButtonsDown.contains(e.getButton())) {
            mouseButtonsDown.remove(Integer.valueOf(e.getButton()));
        }
    }

    public void updateKeyPressed(KeyEvent e) { // update com.stuff.GamePanel's keysDown to match current buttons
        if(!keysDown.contains(e.getKeyCode())) {
            keysDown.add(e.getKeyCode());
        }
    }

    public void updateKeyReleased(KeyEvent e) { // update com.stuff.GamePanel's keysDown to match current buttons
        if(keysDown.contains(e.getKeyCode())) {
            keysDown.remove(Integer.valueOf(e.getKeyCode()));
        }
    }

    // ENTITY UPDATE METHODS............................................................................................
    public void graphicalUpdateAll(Graphics2D g) { // calls passiveUpdate on all entities in 'entities'
        for(GameEntity i : entities) {
            i.graphicalUpdate(g);
        }
    }

    public void logicalUpdateAll() { // calls logicalUpdate on all entities in 'entities'
        for(GameEntity i : entities) {
            i.logicalUpdate();
        }
    }

    public void onClickUpdateAll() { // calls onClickUpdate on all entities in 'entities'
        for(GameEntity i : entities) {
            i.onClickUpdate();
        }
    }

    public void purgeDeadEntities() { // removes all entities flagged dead in 'entities'
        for(GameEntity i : entities) {
            if(i.isDead()) {
                entities.remove(i);
            }
        }
    }

    // PUT YOUR STARTING ENTITIES HERE..................................................................................
    public void initializeStartingEntities() { // adds all starting entities at the beginning of execution
        
    }

    // GRAPHICAL UPDATES................................................................................................
    @Override
    public void paintComponent(Graphics graphics) { // updates graphics on panel
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D)graphics; // casting graphics to graphics2D for better graphical methods
        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)); // activates antialiasing
        graphicalUpdateAll(g); // updates all entities
    }

    // ARRAY ACCESS METHODS.............................................................................................
    public boolean isKeyPressed(int keyCode) { return keysDown.contains(keyCode); }
    public boolean isKeyPressed() { return !keysDown.isEmpty(); }
    public boolean isButtonPressed(int button) { return mouseButtonsDown.contains(button); }

    // ACCESSORS
    public int getMouseX() {return mouseX;}
    public int getMouseY() {return mouseY;}
    public ArrayList<Integer> getMouseButtonsDown() {return mouseButtonsDown;}
    public ArrayList<Integer> getKeysDown() {return keysDown;}
    public CopyOnWriteArrayList<GameEntity> getEntities() {return entities;}
    public Dimension getPreferredSize() {return new Dimension(panelSizeX,panelSizeY);} // used for createGUI() in main

    // MAIN METHOD, FRAME CREATION AND EXIT METHOD......................................................................
    public static void exitGame() { // closes the application and stops execution of the program
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { // suspends execution until it is safe to execute
            @Override
            public void run() {
                createGUI(frameName);
            }
        });
    }

    public static void createGUI(String name) { // creates a frame and adds com.stuff.GamePanel to it
        JFrame f = new JFrame(name);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // stops the program when window is closed
        f.add(new GamePanel()); // appends the panel to the frame
        f.pack(); // sets the frame's dimensions to match the Dimension returned by getPreferredSize()
        f.setVisible(suspendFrameVisibility);
    }
}
