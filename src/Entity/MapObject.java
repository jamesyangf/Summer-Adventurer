package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GameLoop;
import TileMap.Tile;
import TileMap.TileMap;

/*
 * @author James Yang
 * The super class of all game objects in the game
 * This checks for collision from the game objects
 * players, enemies, projectiles, etc
 */
public abstract class MapObject {

	//tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap; //map positions
	protected double ymap;
	
	//position and vector
	protected double x;//the position
	protected double y;
	protected double dx;//the direction it is going
	protected double dy;
	
	//dimensions
	protected int width; //just for reading in the sprite sheet
	protected int height;
	
	//collision box 
	protected int cwidth; //these will be used to determine collision with tiles and other enemies
	protected int cheight;
	
	//collision
	protected int currRow; //current 
	protected int currCol;
	protected double xdest;//where we will be going
	protected double ydest;
	protected double xtemp;//Temporary position
	protected double ytemp;
	protected boolean topLeft;//using the four corners to see if you are hitting a tile or block
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//animation stuff
	protected Animation animation;
	protected int currentAction; //what animation we are currently on
	protected int previousAction; //our prev action
	protected boolean facingRight; //should a sprite be facing right or facing left
	
	//movement, boolean to determine what the object is actually doing
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes, the physics
	protected double moveSpeed; //how fast does the object accelerate
	protected double maxSpeed; //its maximum speed
	protected double stopSpeed;
	protected double fallSpeed; //like gravity
	protected double maxFallSpeed; //terminal velocity
	protected double jumpStart;//how high the object can jump
	protected double stopJumpSpeed;
	
	public MapObject(TileMap tileMap) {
		this.tileMap = tileMap;
		this.tileSize = tileMap.getTileSize();
		//animation = new Animation();
		//facingRight = true;
	}
	
	//the get rectangle function, this gets the rectangle
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth / 2, (int)y - cheight / 2, cwidth, cheight);
	}
	
	//check if it can collide with other map object, to see if you are hitting them
	public boolean intersects(MapObject o){
		Rectangle r1 = this.getRectangle();//the first object
		Rectangle r2 = o.getRectangle();//the second object
		return r1.intersects(r2);//check if these two intersect
	}
	
//	public boolean intersects(Rectangle r) {
//		return getRectangle().intersects(r); //checks if this object intersects that object
//	}
	
//	public boolean contains(MapObject o) {
//		Rectangle r1 = getRectangle();
//		Rectangle r2 = o.getRectangle();
//		return r1.contains(r2);
//	}
//	
//	public boolean contains(Rectangle r) {
//		return getRectangle().contains(r);
//	}
	
	//four corner method to determine the tile collision
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		//if you do go out of bound of the tile//falling off
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
			leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	
	//check if we have run in to a block tile or a normal tile
	public void checkTileMapCollision(){
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) { //if we are not falling
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) { //we are no longer falling on solid ground
				falling = true; //we are faling
			}
		}
		
	}
	
	//the getters
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	public boolean isFacingRight() { return facingRight; }
	
	//the setters
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	//every map object has two positions, the global position and the local position
	public void setMapPosition() { //where to actually draw the character
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	//setting the action of the object
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	//use this method to determine if we have to draw the method at all, we don't have to draw the entire map
	public boolean notOnScreen() {
		//if the object is beyond the left side of the screen or the right side of the screen, or top or on bottom of the screen
		return x + xmap + width < 0 ||
			x + xmap - width > GameLoop.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GameLoop.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
 		//if its facing Right
		if(facingRight) {
			g.drawImage( //draw the image of it facing right
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
		// draw collision box
		//Rectangle r = getRectangle();
		//r.x += xmap;
		//r.y += ymap;
		//g.draw(r);
	}

}
