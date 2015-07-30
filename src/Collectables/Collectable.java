package Collectables;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import Entity.Animation;
import TileMap.TileMap;

/*
 * @author James Yang
 * The super class for all the items in the game or semi-interactive stuff
 */
public abstract class Collectable {

	//Tile Stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position
	protected double x;
	protected double y;
	
	//dimensions
	protected int width;
	protected int height;
	protected int cwidth;
	protected int cheight;
	
	protected Animation animation;
	public Collectable(TileMap tm) {
		tileMap = tm;
		tileSize = tileMap.getTileSize();
	}
	
	//gets the rectangle of the object
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth / 2,
				(int)y - cheight / 2,
				cwidth,
				cheight
		);
	}
	
	//checks if the object intersects the item
	public boolean intersects(Collectable o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}
	
	//check if the object contains the item
	public boolean contains(Collectable o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}
	
	//set the position of the item
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	//set its map position
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void update(){
		
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
	}
	

}
