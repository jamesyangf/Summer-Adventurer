package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public abstract class GameState{

	protected GameStateManager gsm;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
		
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	
	//these methods are needed for the users to pick an option from the menu state
	public abstract void keyPressed(KeyEvent e);
	public abstract void keyReleased(KeyEvent e);

}
