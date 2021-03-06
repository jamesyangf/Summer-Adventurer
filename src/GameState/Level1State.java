package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.Clip;
import Entity.PlayerSave;
import GameState.GameStateManager;
import Main.GameLoop;
import Audio.AudioPlayer;
import Collectables.Health;
import Collectables.Collectable;
import Enemies.Baller;
import Enemies.Bombs;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Teleport;
import Entity.UpgradeUI;
import TileMap.Background;
import TileMap.TileMap;



/*
 * @author James Yang
 * This is the level 1 state for our game, first level
 */

public class Level1State extends GameState{
	
	private TileMap tileMap;//the tile map
	private Background bg; //set the background
	private Player player; //the player
	private ArrayList<Enemy> enemies; //array list of enemies
	private Baller baller; //the slugger enemy
	private Bombs bomb;
	private HUD hud;
	private ArrayList<Explosion> explosions; //an arraylist of explosions
	
	private HashMap<String, AudioPlayer> sfx;

	private Teleport teleport; //the teleport object
	public static AudioPlayer bgMusic;
	private UpgradeUI upgrade;
	private Health h;
	private ArrayList<Collectable> c;
	
	//events
	private boolean eventStart; //if the event starts
	private boolean eventDead; //if the event is dead
	private boolean eventFinish; //if the event is finished
	private ArrayList<Rectangle> tb;
	private int eventTime = 0; //the time of the event

	
	public Level1State(GameStateManager gsm) {
		super(gsm);
		this.init();//this init is where we will initialize the tile map
	}

	
	//this init is where we will initialize the tile map
	public void init() {
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("teleport", new AudioPlayer("/SFX/teleport.mp3"));
		
		tileMap = new TileMap(30); //tile size of 30
		tileMap.loadTiles("/Tilesets/iceland.png"); //load the tileMap
		tileMap.loadMap("/Maps/level1-1.map"); //load the map
		tileMap.setPosition(0, 0); //set the position to 0,0
		tileMap.fixBounds();
		
		//initialize the bg
		bg = new Background("/Backgrounds/beach1.jpg", 0.1); 
		
		player = new Player(tileMap);
		player.setPosition(50, 100);//sets the player position on the screen
		
		addingEnemies();
		
		addingCollectables();
		
		upgrade = new UpgradeUI(player);
		hud = new HUD(player); 
		
		//initialize it
		explosions = new ArrayList<Explosion>();
		
		//player.setPosition(3000, 100);
		//teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(3150, 195);
		//teleport.setPosition(300, 195);

		//start the event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		//eventStart();
		

		bgMusic = new AudioPlayer("/Music/bgmusic.mp3");//plays this song
		bgMusic.play();//play the music
		bgMusic.loop(Clip.LOOP_CONTINUOUSLY);

	}

	private void addingEnemies(){
		//enemies.clear();
		
		enemies = new ArrayList<Enemy>(); //initialize enemies
		
		
		
		//the positions of where to spawn the enemies
		Point[] points = new Point[]{
				//new Point(200,100),
				new Point(860,200),
				new Point(865, 200),
				new Point(1043, 200),
				new Point(1525, 200),
				new Point(1680, 200),
				new Point(1800, 200),
				new Point(3000, 200),
				new Point(3005, 200),
				new Point(3010, 200),
				new Point(3015, 200),
				new Point(3020, 200),
				new Point(3025, 200),
				new Point(3030, 200),
				
		};
		
		//for loop to go through the array
		for(int i = 0; i < points.length; i++){
			//initialize slugger enemy
			baller = new Baller(tileMap,player);
			baller.setPosition(points[i].x, points[i].y); //set its position
			enemies.add(baller);//add the slugger into the enemies arraylist
		}
		
		bomb = new Bombs(tileMap, player);
		bomb.setPosition(300, 200);
		enemies.add(bomb);
		
	}
	
	private void addingCollectables(){
		c = new ArrayList<Collectable>();
		
		Point[] p = new Point[]{ 
				new Point(1043, 105),
				new Point(1240, 85),
				new Point(2434, 105)};
		
		for(int i=0; i< p.length; i++){
			h = new Health(tileMap);
			h.setPosition(p[i].x, p[i].y);
			c.add(h);
		}
	}
	
	public void update() {
		
		if(eventStart) eventStart();
		if(eventDead) eventDead(); //if the event is dead, run the eventDead() method
		if(eventFinish) eventFinish();
		
		//if the player is really dead go to main screen
		if(player.getReallyDead()){
			eventDead = true;//set event dead to true
		}
		
		//loops through all the collectables
		for(int i = 0; i < c.size(); i++){
			Collectable col = c.get(i);
			col.update();
			if(col.intersects(player.getRectangle()) && col instanceof Health){ //if it intersects the player and is a coin
				player.addHealth();
				c.remove(i); //remove it from the list
				i--;
			}
		}
		
		if(teleport.contains(player.getRectangle())){
			sfx.get("teleport").play();
			eventFinish = true;
		}
		
		//when all the enemy on the map is killed
		if(enemies.isEmpty()){
			//teleport portal opens up
		}
		
		//update player
		player.update();
		
		//the tile map position will move with the player
		tileMap.setPosition(GameLoop.WIDTH / 2 - player.getx(), GameLoop.HEIGHT / 2 - player.gety());
		
		//attack the enemy
		player.checkAttack(enemies);
		
		//update the enemy
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.getDead()){ //if the enemy is dead
				if(e instanceof Baller){ //if the enemy is a slugger
					player.addCoins(1); //player gets 1 coin
				}
				if(e instanceof Bombs){
					player.addCoins(2);
				}
				enemies.remove(i); //remove the enemy from the list
				i--; //the enemy number decreases
				explosions.add(new Explosion(e.getx(), e.gety()));//add a new explosion to it
			}
		}
		
		//update explosions
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).update(); //have to update it
			if(explosions.get(i).getRemove()){ //check if we need to remove the explosion
				explosions.remove(i); //remove the explosion from the list
				i--;
			}
		}
		
		//update teleport
		teleport.update();
	}

	@Override
	public void draw(Graphics2D g) {
		
		//draw bg
		bg.draw(g);
		
		//draw the tileMap
		tileMap.draw(g);
		
		//draw the player
		player.draw(g);
		
		//draw the coins
		for(int i = 0; i < c.size(); i++){
			c.get(i).draw(g);
		}
		
		//draw each enemy from the list
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++){
			//draw the explosions
			explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
				
		//drawing the hud
		hud.draw(g);
		
		upgrade.draw(g);
		
		//drawing teleport
		teleport.draw(g);
		
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i)); //fills in every box
		}
		
	}
	
	//when the event starts
	private void eventStart(){
		eventTime++;//event time increments
		if(eventTime == 1){ //if event time has reached 1
			tb.clear();//clear existing boxes
			//add the boxes
			tb.add(new Rectangle(0, 0, GameLoop.WIDTH, GameLoop.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GameLoop.WIDTH / 2, GameLoop.HEIGHT));
			tb.add(new Rectangle(0, GameLoop.HEIGHT / 2, GameLoop.WIDTH, GameLoop.HEIGHT / 2));
			tb.add(new Rectangle(GameLoop.WIDTH / 2, 0, GameLoop.WIDTH / 2, GameLoop.HEIGHT));
		}
		if(eventTime > 1 && eventTime < 60){ //if eventTime is greater than 1 and less than 60
			//make the boxes move to the sides
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventTime == 60) { //if event time is 60
			eventStart = false;
			eventTime = 0; //is set to 0
			tb.clear(); //clear then boxes
		}
	}
	
	//when the player dies
	private void eventDead(){
		eventTime++;
		if(eventTime == 60) {
			tb.clear();
			tb.add(new Rectangle(
				GameLoop.WIDTH / 2, GameLoop.HEIGHT / 2, 0, 0));
		}
		else if(eventTime > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventTime >= 120) {
			tb.clear();
			bgMusic.stop();
			//eventTime = 0;
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	//when the event finishes
	private void eventFinish(){
		eventTime++;
	
		player.stop();
		if(eventTime == 1) {
			
			player.stop();//player doesnt move when inside
		}
		else if(eventTime == 120) {
			tb.clear(); //clear existing boxes
			tb.add(new Rectangle(
				GameLoop.WIDTH / 2, GameLoop.HEIGHT / 2, 0, 0));
		}
		else if(eventTime > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
			
		}
		if(eventTime == 180) {
			//save the player info
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setMaxHealth(player.getMaxHealth());
			PlayerSave.setFire(player.getFire());
			PlayerSave.setMaxFire(player.getMaxFire());
			PlayerSave.setCoins(player.getCoins());
			PlayerSave.setTime(player.getTime());
			PlayerSave.setFireCost(player.getFireCost());
			PlayerSave.setFireDam(player.getFireDam());
			PlayerSave.setScratchDam(player.getScratchDam());
			gsm.setState(GameStateManager.LEVEL2STATE);
		}
	}
		

	//what the user presses on the level 1 state
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ESCAPE){
			gsm.setPaused(true);
		}
		if(key == KeyEvent.VK_LEFT){ //if you press the left button
			player.setLeft(true);//set the left to true
		}
		if(key == KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(key == KeyEvent.VK_UP){
			player.setUp(true);
		}
		if(key == KeyEvent.VK_DOWN){
			player.setDown(true);
		}
		if(key == KeyEvent.VK_SPACE){
			player.setJumping(true);
		}
		if(key == KeyEvent.VK_W){
			player.setGliding(true);
		}
		if(key == KeyEvent.VK_R){
			player.setScratching();
		}
		if(key == KeyEvent.VK_E){
			player.setFiring();
		}
		if(key == KeyEvent.VK_Q){
			player.setSprinting(true);
			//player.setLeft(true);
		}
		if(key == KeyEvent.VK_U){
			upgrade.setUpgrade(true);
		}
		
		upgrade.keyPressed(e);
//		if(key == KeyEvent.VK_RIGHT && key == KeyEvent.VK_Q){
//			player.setSprinting(true);
//			player.setRight(true);
//		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT){ //if you press the left button
			player.setLeft(false);//set the left to true
		}
		if(key == KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(key == KeyEvent.VK_UP){
			player.setUp(false);
		}
		if(key == KeyEvent.VK_DOWN){
			player.setDown(false);
		}
		if(key == KeyEvent.VK_SPACE){
			player.setJumping(false);
		}
		if(key == KeyEvent.VK_W){
			player.setGliding(false);
		}
		if(key == KeyEvent.VK_Q){
			player.setSprinting(false);
		}
	}
}
	

