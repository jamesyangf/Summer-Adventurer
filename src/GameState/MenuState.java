package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;

import Audio.AudioPlayer;
import TileMap.Background;

/*
 * @author James Yang
 * The menu state of the game, all game states will take in parameter GameStateManager in their contructor
 */

public class MenuState extends GameState {//menu state is a part of game state

	private AudioPlayer bgMusic;
	private Background bg;//the background
	
	private int currentChoice = 0; // the current choice or button the user is on
	//the choices or buttons you can press on the menu
	private String[] options = { 
			"Start",
			"Help",
			"Quit"
	};
	
	private Color titleColor; //color for the title
	private Font titleFont; //font for the title
	
	private Font currentFont;
	
	private Font font; //font for the rest of the words
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		
		//tries to get the menu picture and set the title
		try{
			
			bg = new Background("/Backgrounds/menubg.jpg", 1);
			//bg.setVector(0.1, 0); //want the bg moving to the left
			
			titleColor = new Color(40, 180, 255);
			titleFont = new Font("Century Gothic", Font.BOLD, 30);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
			currentFont = new Font("Arial", Font.PLAIN, 18);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		bgMusic = new AudioPlayer("/Music/menuMusic.mp3");//plays this song
		bgMusic.volume(-4);
		bgMusic.play();//play the music
		bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
	}

	@Override
	public void init() {
		
		
	}

	@Override
	public void update() {
		bg.update(); //need to update the bg
		
	}

	@Override //drawing the menu
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT); //making the text better quality
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Summer Adventurer", 90, 70);
		
		//draw menu options
		
		//use a loop to go through the options or choices
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){ //if this is the button you are on
				g.setColor(Color.BLACK); //the button is black
				g.setFont(currentFont);
			}else{
				g.setFont(font);
				g.setColor(Color.BLACK);//the button the user is not on is red
			} 
			g.drawString(options[i], 20, 140 + i * 18);//draw the choices out one by one
		}
	}

	private void select(){
		if(currentChoice == 0){ //if the current choice is the "Start" option
			//start the game
			bgMusic.stop();
			gsm.setState(GameStateManager.LEVEL1STATE);//sets the state to level 1
		}
		if(currentChoice == 1){ //if the current choice is the "Help" option
			//go to help page
		}
		if(currentChoice == 2){//if the current choice is the "Quit" option
			bgMusic.stop();
			System.exit(0);//quits the game
		}
	}
	


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); //get the int of the Key pressed;
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
	public void keyReleased(KeyEvent e) {
		
		
	}

	
	

}
