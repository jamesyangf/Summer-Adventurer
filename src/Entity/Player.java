package Entity;

import Audio.AudioPlayer;
import TileMap.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * @author James Yang
 * The player class
 */
public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private double fire;
	private double maxFire;
	private boolean dead;
	private boolean reallyDead;
	private boolean flinching;
	private long flinchTimer;
	private long time; // the time of the clock
	
	private int coins;
	
	
	// fireball attack
	private boolean firing;
	private int fireCost; //fireball alos has a cost
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch attack
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	//double jumping
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpSpeed;
	
	//sprinting
	private boolean sprinting;
	private double stamina;
	private double maxStamina;
	private double sprintCost;
	private double sprintSpeed;
	private double maxSprintSpeed;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;//hold all of the sprite animation
	private final int[] numFrames = { 2, 6, 1, 1, 2, 2, 5 }; //the number of frames each action has, ex: idle ahs two frames
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;

	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpSpeed = -3;
		
		stamina = maxStamina = 100;
		sprintCost = 1;
		sprintSpeed = 0.5;
		maxSprintSpeed = 3.4;
		
		
		facingRight = true;
		
		health = maxHealth = 100;
		fire = maxFire = 100;
		time = 0;
		
		fireCost = 10;
		fireBallDamage = 4;
		fireBalls = new ArrayList<FireBall>(); //the creation of the fireballs
		
		scratchDamage = 3; //scratch does 8 damage
		scratchRange = 40; //range of it
		 
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/player1.png"));
			
			//extract each of the animation action from it
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi = new BufferedImage[numFrames[i]];//the images are in the array
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != 6) { //if i is not scratching frame
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else { //if it is the scratching frame, make the width larger, cuz the scratching pixel is larger
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();//creating a new animation
		currentAction = IDLE; 
		animation.setFrames(sprites.get(IDLE));//set the animation to something
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		sfx.put("fire", new AudioPlayer("/SFX/fireball.wav"));
		sfx.put("landing", new AudioPlayer("/SFX/landing.mp3"));
		sfx.put("playerhit", new AudioPlayer("/SFX/playerhit.mp3"));

	}
	
	public void addHealth(){
		health += 5;
		if(health > maxHealth){ //if health is greater than max health
			health = maxHealth;
		}
	}
	
	public void addCoins(int i){
		coins += i;
	}
	
	//getters, for the Hud
	public long getTime() { return time; }
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public double getFire() { return fire; }
	public double getMaxFire() { return maxFire; }
	public double getStamina() { return stamina; }
	public double getMaxStamina() { return maxStamina; }
	public int getCoins(){ return coins; }
	public double getxMapPos(){
		return tileMap.getx();
	}
	public double getyMapPos(){
		return tileMap.gety();
	}
	
	//scratch damage
	public int getScratchDam() { return scratchDamage; }
	public void setScratchDam(int s) { this.scratchDamage = s; }
	//Fire Damage
	public int getFireDam() { return fireBallDamage; }
	public void setFireDam(int fd) { this.fireBallDamage = fd; }
	//the fire cost
	public int getFireCost() { return fireCost; }
	public void setFireCost(int fc) { this.fireCost = fc; }
	
	
	//setters
	public void setHealth(int h){
		this.health = h;
		
	}
	public void setTime(long t){
		this.time = t;
	}
	public void setCoins(int c){
		this.coins = c;
	}
	public void setMaxHealth(int h){
		maxHealth = h;
	}
	public void setMaxFire(double f){
		maxFire = f;
	}
	public void setFire(double f){
		fire = f;
	}
	
	public void setFiring() { 
		firing = true;
	}
	public void setScratching() {
		scratching = true;
	}
	public void setGliding(boolean b) { 
		gliding = b;
	}
	public void setJumping(boolean b){
		//if b is already set and it has not jumped yet and its falling and its not already double jumped
		if(b && !jumping && falling && !alreadyDoubleJump){
			doubleJump = true; //set the double jump to true
		}
		jumping = b; //set the jumping boolean
	}
	public void setSprinting(boolean b){
		sprinting = b;
	}
	
	//get the time as a string
	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	
	//get method for really dead
	public boolean getReallyDead(){
		return reallyDead;
	}
	
	
	//stops actions
	public void stop() {
		left = right = up = down = flinching = firing = scratching = jumping  = false;
		
	}

	//checks if the player is attacking the enemy, or if the enemy is attacking the player
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(scratching) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + scratchRange && 
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - scratchRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
			}
			
			// fireballs
			for(int j = 0; j < fireBalls.size(); j++) { //loops through every fireball
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			
			// check enemy collision, if the enemy touches the player 
			if(intersects(e)) {
				this.hit(e.getDamage()); //player is hit with enemy damage
			}
		}
	}
	
	
	//method for the player getting hit
	public void hit(int damage) {
		if(flinching) return;
		stop();
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
		
	}
	
	
	//this function will determine where the next position of the player is
	private void getNextPosition() {
		
		
		
		// movement
		if(left) { //if we are pressing left
			if(sprinting){
				//stamina   -= sprintCost;
				moveSpeed = sprintSpeed;
				maxSpeed = maxSprintSpeed;
			}else{
				moveSpeed = 0.3;
				maxSpeed = 1.6;
			}
			dx -= moveSpeed; //move to the left side
			if(dx < -maxSpeed) { //cant go past the max speed
				dx = -maxSpeed;
			}
			
		}
		else if(right) {
			if(sprinting){
				//stamina   -= sprintCost;
				moveSpeed = sprintSpeed;
				maxSpeed = maxSprintSpeed;
			}else{
				moveSpeed = 0.3;
				maxSpeed = 1.6;
			}
			dx += moveSpeed; //move to the left side
			if(dx > maxSpeed) { //cant go past the max speed
				dx = maxSpeed;
			}
		}
		else { //if you are not moving left or right
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			//JukeBox.play("jump");
			dy = jumpStart;
			falling = true;
			sprinting = false;
			sfx.get("jump").play();
		}
		
		//when double jump is true
		if(doubleJump){
			sfx.get("jump").play();
			dy = doubleJumpSpeed; //it goes up by that speed
			alreadyDoubleJump = true; //so it doesnt jump three times
			doubleJump = false;
			sprinting = false;
		}
		
		//this is when he not falling again, when he has done it once, he can do it again after he lands
		if(!falling){ //if hes not falling
			alreadyDoubleJump = false; //reset hes double jump so he can do it again
		}
		
		// falling
		if(falling) {
			sprinting = false;
			if(dy > 0 && gliding) dy += fallSpeed * 0.1; //if its falling and gliding
			else dy += fallSpeed; //its just falling
			
			//if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	//the actions
	public void update() {
		
		time++;//time increment
		
		// update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//checks for the player being dead
		//if player jumps off the screen
		if(y > tileMap.getHeight()){
			stop(); //player stops
			reallyDead = true; //he is really dead
		}
		
		//if it is falling and it stops falling
		if(isFalling && !falling){
			sfx.get("landing").play();
		}
		
		
		//checking if stamina ran out
		stamina += 0.1;
		if(stamina > maxStamina){
			stamina = maxStamina;
		}
		if(sprinting){
			if(stamina > sprintCost){
				stamina -= sprintCost;
			}else{
				sprinting = false;
			}
		}
		
		//checks if he has no health
		if(health == 0){
			dead = true;//hes dead
			stop(); //he stops everything
			if(dead && animation.hasPlayedOnce()){
				//plays dead sfx
				reallyDead = true;
			}
		}
		
		// check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// fireball attack
		fire += .03; //mana recover time
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				sfx.get("fire").play();
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		// check done flinching, so it can stop flicnhing
		if(flinching) {
			//the last time we have been flinching
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) { //if flinching has been longer than 1 second
				flinching = false; //we are not flinching anymore
			}
		}
		
		// set animation
		if(scratching) { //if we are scratching
			if(currentAction != SCRATCHING) {
				sfx.get("scratch").play(); //scratch sound plays
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) { //if its a fireball
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dead){
//			if(currentAction != DEAD){
//				currentAction = DEAD;
//				//play dead animation
//			}
		}
		else if(sprinting && left ||  sprinting && right){
			if(currentAction != WALKING) {
				currentAction = WALKING; //its a walking animation
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(1);
				width = 30;
			}
		}
		else if(dy > 0) { //if its falling
			if(gliding) {//if its falling and gliding
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) { //regular falling
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) { //jumping animation
			if(currentAction != JUMPING) { //if its not jumping set it to jumping
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) { //if its left or right 
			if(currentAction != WALKING) {
				currentAction = WALKING; //its a walking animation
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update(); //update the animation
		
		// set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition(); //first thing to get called in any map object that gets called
		
		// draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// draw player
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
