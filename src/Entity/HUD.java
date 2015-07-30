package Entity;

import java.awt.*;

public class HUD {
	
	private Player player;
	
	//private BufferedImage image;
	private Font font;
	private Font profile;
	
	public HUD(Player p) {
		player = p;
		profile = new Font("ARIAL", font.PLAIN, 7);
//		try {
//			image = ImageIO.read(
//				getClass().getResourceAsStream(
//					"/HUD/hud.gif"
//				)
//			);
			font = new Font("SANS_SERIF", Font.PLAIN, 9);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void draw(Graphics2D g) {
		
		//health
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //making the text better quality
		g.setColor(Color.gray);
		g.fill3DRect(10, 4, player.getMaxHealth() ,5,false);
		g.setColor(Color.green);
		g.fill3DRect(10, 4, player.getHealth(),5,true);
		g.setColor(Color.black);
		g.drawRect(10, 4, player.getMaxHealth(),5);
		g.setFont(profile);
		g.setColor(Color.white);
		g.drawString(player.getHealth()+" / "+ player.getMaxHealth(), 3, 8);
		
		//mana
		g.setColor(Color.gray);
		g.fill3DRect(10,11,(int) player.getMaxFire() ,5, false);
		g.setColor(Color.blue);
		g.fill3DRect(10, 11,(int) player.getFire(),5,true);
		g.setColor(Color.black);
		g.drawRect(10, 11, (int)player.getMaxFire(),5);
		g.setColor(Color.white);
		g.drawString((int)player.getFire()+" / "+ (int)player.getMaxFire(), 3, 16);
		
		//stamina
		g.setColor(Color.gray);
		g.fill3DRect(10, 18,(int) player.getMaxStamina() ,3, false);
		g.setColor(Color.RED);
		g.fill3DRect(10, 18,(int) player.getStamina(),3,true);
		g.setColor(Color.black);
		g.drawRect(10, 18, (int) player.getMaxStamina(),3);

		//the timer
		g.setFont(font);
		g.setColor(java.awt.Color.BLACK);
		g.drawString(player.getTimeToString(), 290, 15);
		
		//the coins
		g.setFont(font);
		g.setColor(java.awt.Color.BLACK);
		g.drawString("$: "+player.getCoins(), 290, 25);
		
		g.setFont(font);
		g.setColor(java.awt.Color.BLACK);
		g.drawString(""+(int)player.getxMapPos()+", "+(int)player.getyMapPos(), 100, 35);
	}
	
}













