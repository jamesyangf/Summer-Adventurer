package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	
	protected boolean flinching;
	protected long flinchTimer;
	protected long flinchCount;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}
	
	//this method gets the boolean dead,  if the enemy is dead
	public boolean getDead() { return dead; }
	
	//this method gets the damage of the enemy
	public int getDamage() { return damage; }
	
	//if they get hit
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true; //if health is zero set dead to true
		flinching = true;
		flinchTimer = System.nanoTime();
		flinchCount = 0;
	}
	
	public void update() {}
	
}














