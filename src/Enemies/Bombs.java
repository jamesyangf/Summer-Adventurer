package Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
/*
 * @author James Yang
 * The second enemy of the game
 */
public class Bombs extends Enemy {

	private Player player;
	private BufferedImage[] sprites;
	private Random rand;
	private boolean stop;
	
	public Bombs(TileMap tm, Player player) {
		super(tm);
		this.player = player;
		
		rand = new Random();
		moveSpeed = rand.nextDouble();
		maxSpeed = rand.nextDouble();
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 40;
		height = 40;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 20;
		damage = 7;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/Spirit.gif"
				)
			);
			
			sprites = new BufferedImage[2];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites); //set to slugger animation
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
		
	}
	
	private void stop(){
		if(stop){
			dx = 0;
		}
	}
	
	private void getNextPosition() {
		
		// movement
		if(this.getRectangle().intersects(player.getRectangle())){
			stop = true;
		}else{
			stop = false;
		}
		if(left) {
			
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
			
		}
		else if(right) {
			
			
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
			
		}
		
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		stop();
		
		// check flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false; //it is not moving right
			left = true; // it is moving left
			facingRight = false; //it is not facing right
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(flinching) { //if you get hit, it blinking
			//checks the last time you have been flinching
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	

}
