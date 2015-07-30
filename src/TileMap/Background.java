package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GameLoop;

/*
 * @author James Yang
 * The background of the game
 */
public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale; //scale that the background moves
	
	public Background(String s, double ms) {
		//checks if there is an image to read
		try{
			//reads the images from the resources file
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//sets the position of the bg
	public void setPosition(double x, double y){
		this.x = (x * moveScale) % GameLoop.WIDTH; //if it goes past the screen we want it to reset
		this.y = (y * moveScale) % GameLoop.HEIGHT;
	}
	
	//setting the vectors
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	//this moves the screen
	public void update() {
		x += dx;
		y += dy;
	}
	
	//draws the background
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);//draws the bg
		
		//since this is a scrolling bg we need to keep in consideration that the bg will always be on the screen
		if(x < 0) {
			g.drawImage(image,(int)x + GameLoop.WIDTH,(int)y,null);
		}
		if(x > 0) {
			g.drawImage(image,(int)x - GameLoop.WIDTH,(int)y,null);
		}
	}
	
}
