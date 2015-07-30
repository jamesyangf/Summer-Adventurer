package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;





/*
 * @author James Yang
 * The gameState for the pause screen when ever the player presses the esc button
 */
public class PausedState extends GameState {

	private Font font;//the font 
	private int currentChoice; //current choice of the player
	private String[] options = {"Back", "Control", "Restart", "Exit"};
	
	public PausedState(GameStateManager gsm) {
		super(gsm);
		
		currentChoice = 0;
		
		//font
		font = new Font("Arial", Font.PLAIN, 17);
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //making the text better quality
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 100, 80);
		
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){ //if this is the button you are on
				g.setColor(Color.BLACK); //the button is black
			}else{
				g.setColor(new Color(64, 141, 255));//the button the user is not on is red
			}
			g.drawString(options[i], 20, 140 + i * 18);//draw the choices out one by one
		}
		
	}
	
	private void select(){
		if(currentChoice == 0){ //if the current choice is the "Back" option
			gsm.setPaused(false);
		}
		if(currentChoice == 1){ //if the current choice is the "Control" option
			//go to control page
		}
		if(currentChoice == 2){
			if(gsm.getState() == GameStateManager.BOSSSTATE){
				BossState.bgMusic.stop();
				gsm.setPaused(false);
				gsm.setState(GameStateManager.LEVEL1STATE);
			}else{
				Level1State.bgMusic.stop();
				gsm.setPaused(false);
				gsm.setState(GameStateManager.LEVEL1STATE);
			}
			
		}
		if(currentChoice == 3){//if the current choice is the "Exit" option
			if(gsm.getState() == GameStateManager.BOSSSTATE){
				BossState.bgMusic.stop();
				gsm.setPaused(false);
				gsm.setState(GameStateManager.MENUSTATE);// go to main menu game
			}else{
				gsm.setPaused(false);
				gsm.setState(GameStateManager.MENUSTATE);// go to main menu game
				Level1State.bgMusic.stop();
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); //get the int of the Key pressed;
		if(key == KeyEvent.VK_ESCAPE){ //if the button pressed is escape
			gsm.setPaused(false);
		}
		if(key == KeyEvent.VK_ENTER){ //if the key pressed is enter key
			select(); //select method runs
		}
		if(key == KeyEvent.VK_UP){ //if the key pressed is up
			currentChoice--; //goes up an option, ex: from help to start
			if(currentChoice == -1){ //if it goes out of bounds
				currentChoice = options.length - 1; //bring it back to the bottom
			}
		}
		if(key == KeyEvent.VK_DOWN){ //if the key pressed is up
			currentChoice++; //goes down an option, ex: from start to help
			if(currentChoice == options.length){ //if it goes out of bounds
				currentChoice = 0; //bring it back to the top
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
