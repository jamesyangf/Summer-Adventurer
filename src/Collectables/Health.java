package Collectables;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import TileMap.TileMap;

public class Health extends Collectable {
	
	BufferedImage[] sprites;
	
	public Health(TileMap tm) {
		super(tm);
		//coin size
		width = 30;
		height = 30;
		cwidth = 10;
		cheight = 10;
		
		// load sprites
				try {
					
					BufferedImage spritesheet = ImageIO.read(
						getClass().getResourceAsStream(
							"/Sprites/Player/health.png"
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
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				animation = new Animation();
				animation.setFrames(sprites); //set to slugger animation
				animation.setDelay(300); //how fast the animation goes
	}
	
	public void update(){
		animation.update();
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
		
		g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
	}

}
