package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel; //see the window with JPanel
import GameState.GameStateManager;

//this class also has a keyListener
/*
 * @author James yang
 * This is the Game Loop of our game and it is connected to our main class
 * This class runs a thread and updates the gameStaes 60 FPS
 * 
 */

public class GameLoop extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	
	//dimensions
	public static final int WIDTH = 340;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	//game Thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS; //in mili sec
	
	//image
	private BufferedImage img;
	private Graphics2D graphics2D;
	
	// game state manager
	private GameStateManager gsm;
	
	public GameLoop() {
		
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));//construct a dimension
		setFocusable(true);//sets focusable to false
		requestFocus();//if you press any kind of key or give any input, the input is heard by the respective Listener for that component.
		
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	//this initializes everything
	private void init(){
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		graphics2D = (Graphics2D) img.getGraphics();
		running = true;//sets the running to true
		gsm = new GameStateManager(); //initialize it
		gsm.init();
	}
	
	//the game loop there
	//implemented form Runnable
	@Override
	public void run() {
		init();
			
		//three time
		long start;
		long elapse;
		long wait;
		
		while(running) { //while the game runs
			
			start = System.nanoTime();
			
			update(); //it updates
			draw(); //draws
			drawToScreen(); //and draws to the screen
			
			elapse = System.nanoTime() - start;
			
			wait = targetTime - elapse / 1000000;
			
			if(wait < 0){
				wait = 5;
			}
			
			try{
				Thread.sleep(wait);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		
			
	}
	
	//will update the gamestates
	private void update(){
		gsm.update();
	}
	
	//drawing the game states
	private void draw(){
		gsm.draw(graphics2D);
	}
	 
	//drawing stuff to the screen
	private void drawToScreen(){
		Graphics g = getGraphics();
		g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	

}
