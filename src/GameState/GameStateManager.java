package GameState;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/*
 * @author James Yang
 * This class manages the game states
 */

public class GameStateManager  {

	//we are using an Array to hold all of our game states
	private GameState[] gameStates;
	private int index;//the index of the array list, the current state of the list
	
	//you dont load pause state, it opens during the game
	private PausedState pauseState;
	private boolean paused;
	

	public static final int NUMSTATES = 4; //there are two states
	public static final int MENUSTATE = 0; //menu
	public static final int LEVEL1STATE = 1; //level 1
	public static final int LEVEL2STATE = 2;
	public static final int BOSSSTATE = 3;
	
	public GameStateManager() {
		//initialize the array
		gameStates = new GameState[NUMSTATES];
		
		
		pauseState = new PausedState(this);
		paused = false;
				
		index = MENUSTATE; //the current state is the Menu
		loadState(index);//pass in the current state which is the menu state, starts with menu state
	}
	
	//this will load the needed gamestate
	private void loadState(int state){
		if(state == MENUSTATE){ //if its menustate
			gameStates[state] = new MenuState(this); //takes in this gsm
		}
		if(state == LEVEL1STATE){
			gameStates[state] = new Level1State(this);
		}
		if(state == LEVEL2STATE){
			gameStates[state] = new Level2State(this);
		}
		if(state == BOSSSTATE){
			gameStates[state] = new BossState(this);
		}
	}
	
	//a way to unload the state so we wont use it anymore
	private void unloadState(int states){
		gameStates[states] = null; //makes that gamestate null
	}
	//able to change the states, sets the state to this and takes in what state to use
	public void setState(int state){
		unloadState(index); //unloading the current state
		index = state;
		loadState(index);//and load the current state to the one passed in the parameter
		//gameStates[index].init();//it initializes the current state at that index
	}
	
	public int getState(){
		return index;
	}
	
	public void setPaused(boolean b) { paused = b; }

	
	public void update(){
		
		try{
			gameStates[index].update();
		}catch(Exception e){
			
		}
	}
	public void draw(Graphics2D g){
		
		if(paused) { //if paused is true 
			pauseState.draw(g); //draw the paused page
			return;
		}
		
		try{
			gameStates[index].draw(g);
		}catch(Exception e){
			
		}
	}
	
	public void init(){
		try{
			gameStates[index].init();
		}catch(Exception e){
			
		}
	}

	
	public void keyPressed(KeyEvent k) {
		
		if(paused){
			pauseState.keyPressed(k);
			return; 
		}
		
		try{
			gameStates[index].keyPressed(k);//gets the gamestate where the key is pressed, in this case its menu state
		}catch(Exception e){
			
		}
	}

	
	public void keyReleased(KeyEvent k) {
		try{
			gameStates[index].keyReleased(k);
		}catch(Exception e){
			
		}	
	}

	
}
