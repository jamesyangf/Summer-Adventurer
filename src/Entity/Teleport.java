package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Collectables.Collectable;
import TileMap.TileMap;

/*
 * @author James Yang
 * The Teleport portal object
 */

public class Teleport extends Collectable {
	
	private BufferedImage[] sprites;
	
	public Teleport(TileMap tm) {
		super(tm);
		width = height = 40;
		cwidth = 20;
		cheight = 40;
		try {
			//gets the sprite sheet
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Player/Teleport.gif")
			);
			sprites = new BufferedImage[9]; //there are 9 frames to teleport animation
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(50);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
		g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
	}
	
}
