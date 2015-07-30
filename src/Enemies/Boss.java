package Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
/*
 * @author James Yang
 * The boss of the game
 */
public class Boss extends Enemy {
	
	private BufferedImage[] sprites;
	private BufferedImage[] boss2;
	private BufferedImage[] flinch;
	private Player player;
	private boolean stop;
	private int attackTime;
	private boolean health1;
	private int h1;
	
	public Boss(TileMap tm, Player player) {
		
		super(tm);
		this.player = player;
		
		moveSpeed = 0.3;
		maxSpeed = 0.8;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		attackTime = 0;
		
		width = 50;
		height = 50;
		cwidth = 34;
		cheight = 40;
		
		health1 = true;
		h1 = 300;
		health = 300;
		maxHealth = 300;
		damage = 5;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/Boss.png"
				)
			);
			
			BufferedImage flinchSheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/BossFlinch.png"
						)
					);
			BufferedImage AngrySheet =  ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/Boss2.png"
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
			
			boss2 = new BufferedImage[2];
			for(int i = 0; i < boss2.length; i++) {
				boss2[i] = AngrySheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
			
			flinch = new BufferedImage[1];
			flinch[0] = flinchSheet.getSubimage(0, 0, 50, 50);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites); //set to slugger animation
		animation.setDelay(300);
		
		left = true;
		facingRight = false;
		
	}
	
	public void hit(int damage) {
		if(dead || flinching) return;
		if(health1 == true){
			h1 -= damage - 2;
			if(h1 < 0) h1 = 0;
			if(h1 == 0) health1 = false; 
			flinching = true;
			flinchTimer = System.nanoTime();
			flinchCount = 0;
		}else{
			health -= damage;
			if(health < 0) health = 0;
			if(health == 0) dead = true; //if health is zero set dead to true
			flinching = true;
			flinchTimer = System.nanoTime();
			flinchCount = 0;
		}
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
		
		if(health1 == false){
			moveSpeed = 0.5;
			maxSpeed = 1.2;
			damage = 10;
		}
		
		
		// check flinching
		if(flinching) {
			animation.setFrames(flinch);
			animation.setDelay(100);
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 50) {
				flinching = false;
			}
		}else{ 
			if(health1 == true){
				animation.setFrames(sprites);
				animation.setDelay(300);
			}else{
				animation.setFrames(boss2);
				animation.setDelay(300);
			}
		}
		
		
		if(player.getx() < x){
			attackTime++;
			if(attackTime == 50){
				left = true;
				right = false;
				facingRight = false;
				attackTime = 0;
			}
		}else if(player.getx() > x){
			attackTime++;
			if(attackTime == 50){
				right = true;
				left = false;
				facingRight = true;
				attackTime = 0;
			}
		}
		
		// if it hits a wall, go other direction
//		if(right && dx == 0) {
//			right = false; //it is not moving right
//			left = true; // it is moving left
//			facingRight = false; //it is not facing right
//		}
//		else if(left && dx == 0) {
//			right = true;
//			left = false;
//			facingRight = true;
//		}
		
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
		
		//health
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //making the text better quality
		g.fill3DRect(10, 230, maxHealth ,5,false);
		g.setColor(Color.green);
		g.fill3DRect(10, 230, health,5,true);
		g.setColor(Color.red);
		g.fill3DRect(10, 230, h1,5,true);
//		g.setColor(Color.black);
//		g.drawRect((int)x, (int)y, maxHealth,5);
				
		
	}
	
}












