package TileMap;

import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	private int type;
	
	//Tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
		
	}
	
	//a way to get the images
	public BufferedImage getImage(){
		return image;
	}
	
	//get the type of the images
	public int getType(){
		return type;
	}

}
