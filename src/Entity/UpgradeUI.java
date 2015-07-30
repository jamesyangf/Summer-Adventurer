package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import Audio.AudioPlayer;
import Main.GameLoop;

/*
 * @author James Yang
 * This is the UI screen that the player can upgrade their attack
 */
public class UpgradeUI {

	private Player player;
	private Font font;
	private Font currentFont;
	private Font importFont;
	private boolean upgrade;
	private int currentChoice = 0;
	private String[] options = { 
			"+ Upgrade Attack : 5 coins",
			"+ Upgrade FireBall: 5 coins",
			"+ Upgrade Mana: 5 coins",
			"+ Full Health: 3 coins",
			"+ Full Mana: 2 coins"
	};
	
	private HashMap<String, AudioPlayer> sfx;
	
	public UpgradeUI(Player p) {
		this.player = p;
		font = new Font("SANS_SERIF", Font.PLAIN, 11);
		currentFont = new Font("Arial", Font.BOLD, 14);
		importFont = new Font("DIALOG", Font.BOLD, 17);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("buying", new AudioPlayer("/SFX/buying.mp3"));
		sfx.put("error", new AudioPlayer("/SFX/error.mp3"));

	}

	
	public void setUpgrade(boolean b){
		upgrade = b;
	}
	
	public void draw(Graphics2D g){
		if(upgrade){ //if upgrade is true, draw the shop window
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //making the text better quality
			g.setFont(importFont);
			g.setColor(Color.BLACK);
			//g.drawRect(GameLoop.WIDTH - 200, 20, 170, 100);
			g.drawRect(GameLoop.WIDTH -250, 40, 230, 130);
			g.drawString("SHOP: ", GameLoop.WIDTH - 247, 55);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //making the text better quality
			g.setFont(new Font("SANS_SERIF", Font.PLAIN, 9));
			g.drawString("Press Y to close", GameLoop.WIDTH - 90, 165);
			
			//use a loop to go through the options or choices
			for(int i = 0; i < options.length; i++){
				if(i == currentChoice){ //if this is the button you are on
					g.setColor(Color.BLACK); //the button is black
					g.setFont(currentFont);
				}else{
					g.setFont(font);
					g.setColor(Color.BLACK);//the button the user is not on is red
				}
				g.drawString(options[i], 93, 95 + i * 18);//draw the choices out one by one
			}
		}
		
	}
	
	private void select(){
		if(currentChoice == 0){ 
			if(player.getCoins() >= 5){
				player.setScratchDam(player.getScratchDam() + 2);
				sfx.get("buying").play();
				player.setCoins(player.getCoins() - 5);
			}else{
				sfx.get("error").play();
			}
		}
		if(currentChoice == 1){ 
			if(player.getCoins() >= 5){
				player.setFireDam(player.getFireDam() + 2);
				sfx.get("buying").play();
				player.setCoins(player.getCoins() - 5);
			}else{
				sfx.get("error").play();
			}
		}
		if(currentChoice == 2){
			if(player.getCoins() >= 5){
				if(player.getFireCost() <= 2){
					sfx.get("error").play();
				}
				player.setFireCost(player.getFireCost() - 2);
				sfx.get("buying").play();
				player.setCoins(player.getCoins() - 5);
			}else{
				sfx.get("error").play();
			}
		}
		if(currentChoice == 3){
			if(player.getCoins() >= 3){
				player.setHealth(100);
				sfx.get("buying").play();
				player.setCoins(player.getCoins() - 3);
			}else{
				sfx.get("error").play();
			}
		}
		if(currentChoice == 4){
			if(player.getCoins() >= 2){
				player.setFire(100);
				sfx.get("buying").play();
				player.setCoins(player.getCoins() - 2);
			}else{
				sfx.get("error").play();
			}
		}
	}
	
	
	public void keyPressed(KeyEvent k){
		int key = k.getKeyCode();
		if(upgrade){
			player.stop();//player cant move if the window if opened
			if(key == KeyEvent.VK_Y){
				upgrade = false;
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
	}

}
