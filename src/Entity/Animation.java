package Entity;

import java.awt.image.BufferedImage;

/*
 * @author James Yang
 * This class will handle the sprite animation for us
 */

public class Animation {

	//using an array of buffered images to hold all the frames or scenes
	private BufferedImage[] frames;
	private int currentFrame; //the current frame we are on
	private int numFrames; //the number of frames we have
	
	private long startTime;
	private long delay;//how long between each frames
	
	private boolean playedOnce; //
	
	public Animation() {
		this.playedOnce = false;
	}
	
	//setting the frames
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
		numFrames = frames.length;//the number of frames
	}
	
	public void setDelay(long d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }
	public void setNumFrames(int i) { numFrames = i; }

	
	public void update() {
		
		if(delay == -1) return; //no animation
		
		long elapsed = (System.nanoTime() - startTime) / 1000000; //in seconds
		if(elapsed > delay) { //if the seconds is greater than delay time
			currentFrame++; //go to next frame
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {//we don't go past the current frames there are
			currentFrame = 0;
			playedOnce = true; //the animation is played once
		}
		
	}
	
	//getters
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }

}
