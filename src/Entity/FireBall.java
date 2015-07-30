package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(TileMap tm, boolean right) {
		
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/fireball.gif"
				)
			);
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}
			
			animation = new Animation();
			animation.setFrames(sprites); //initially the fire is the normal fireball sprite
			animation.setDelay(70);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setHit() { //if set to hit
		if(hit) return;
		hit = true; 
		animation.setFrames(hitSprites); //set hit sprite animation plays
		animation.setDelay(70);
		dx = 0; //it will stop the fireball
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) { //if its not moving and its not hit, if its not moving then it has hit a wall or an object
			setHit(); //set it to hit, when you set it to hit the fireball will go to hit sprite and will finish animation and disappear
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) { //if it has been hit and and played once
			remove = true; //remove the fireball
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g); //if its not right, draw it left
		
	}
	
}


















