package TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import Main.GameLoop;

import java.io.*;

/*
 * @author James Yang
 * This gets the tile resources 
 * this also detects collision and for graphics
 */
public class TileMap {

	//position of tilemap
	private double x;
	private double y;
	
	//bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double panel; //smoothly scrolling the camera towards the camera
	
	//map stuff
	private int[][] map; //2d integer array
	private int tileSize; //the tile size
	private int numRows; //the number of rows
	private int numCols; // the number of columns
	private int width;
	private int height;
	
	//something to hold the tileset, a tileset is a buffered image
	private BufferedImage tileset;
	private int numTileAcross;
	private Tile[][] tiles; //put all the tile images in the 2d array
	
	//drawing, drawing the tiles on the screen only instead of loading everything
	private int rowOffset; //which row to start drawing 
	private int colOffset; //which column to start drawing
	private int numRowsToDraw; //we need to know how many rows to draw
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GameLoop.HEIGHT / tileSize + 2;
		numColsToDraw = GameLoop.WIDTH / tileSize + 2;
		panel = 0.07;
	}

	//loads the tile set into memory
	public void loadTiles(String s){
		
		try{
			tileset = ImageIO.read(getClass().getResource(s)); //reads the resources file for the image
			numTileAcross = tileset.getWidth() / tileSize; //size of tile set across
			tiles = new Tile[2][numTileAcross]; //two rows and the col is numTileAcross
			
			BufferedImage subimage;
			for(int col = 0; col < numTileAcross; col++){
				//the first row
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				//the second row
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//loads the map file into memory
	public void loadMap(String s){
		//parsing through the .map file 
		try {
			
			InputStream in = getClass().getResourceAsStream(s); //first get the .map file
			BufferedReader br = new BufferedReader(new InputStreamReader(in));//then begin reading it
			
			numCols = Integer.parseInt(br.readLine());//the first line is the number of cols
			numRows = Integer.parseInt(br.readLine());//the second line is the number of rows
			map = new int[numRows][numCols]; //put that in the array
			width = numCols * tileSize; //the width in pixels is num of cols * tileSize
			height = numRows * tileSize;
			
			//setting the borders
			xmin = GameLoop.WIDTH - width;
			xmax = 0;
			ymin = GameLoop.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();//read that line
				String[] tokens = line.split(delims); //get all the strings there 
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//creating getters
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public int getType(int row, int col) {
		int rc = map[row][col]; //row and col
		int r = rc / numTileAcross;//the row
		int c = rc % numTileAcross;//the col
		return tiles[r][c].getType();
	}
	
	//set the position, where to start the tile map
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * panel;
		this.y += (y - this.y) * panel;
		
		fixBounds();//to check if bounds arent being passed
		
		//we dont want to draw the entire tile map so we have to find where to start drawing
		colOffset = (int)-this.x / tileSize; //which col to start drawing
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	//makes sure that the bounds arent being passed
	public void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	//sets the bounds
	public void setBounds(int i1, int i2, int i3, int i4) {
		xmin = GameLoop.WIDTH - i1;
		ymin = GameLoop.WIDTH - i2;
		xmax = i3;
		ymax = i4;
	}
	
	//We are drawing
	public void draw(Graphics2D g){
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				if(map[row][col] == 0) continue;
				
				//find out which tile to draw
				int rc = map[row][col];
				int r = rc / numTileAcross;
				int c = rc % numTileAcross;
				
				//draw the tiles here	
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				
			}
			
		}
	}

	
}
