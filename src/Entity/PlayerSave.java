package Entity;

/*
 * @author James Yang
 * This class will keep track of the player progress and save its state from state to state
 */
public class PlayerSave {

	private static int health = 100;
	private static int maxHealth = 100;
	private static long time = 0;
	private static int coins = 0;
	private static double fire = 100;
	private static double maxFire = 100;
	private static int fireCost = 10;
	private static int fireBallDamage = 4;
	private static int scratchDamage = 3;
	
	public PlayerSave() {
		coins = 0;
		health = 100;
		maxHealth = 100;
		time = 0;
		fire = 100;
		maxFire = 100;
		
	}
	
	public static int getCoins() { return coins; }
	public static void setCoins(int i) { coins = i; }
	
	public static int getHealth() { return health; }
	public static void setHealth(int i) { health = i; }
	
	public static int getMaxHealth() { return maxHealth; }
	public static void setMaxHealth(int m) { maxHealth = m; } 
	
	public static long getTime() { return time; }
	public static void setTime(long t) { time = t; }
	
	public static double getFire() { return fire; }
	public static void setFire(double m) { fire = m; } 
	
	public static double getMaxFire() { return maxFire; }
	public static void setMaxFire(double m) { maxFire = m; } 
	
	public static int getFireCost() {return fireCost;}
	public static void setFireCost(int fc) { fireCost = fc; }
	
	public static int getFireDam() {return fireBallDamage;}
	public static void setFireDam(int fc) { fireBallDamage = fc; }
	
	public static int getScratchDam() {return scratchDamage;}
	public static void setScratchDam(int fc) { scratchDamage = fc; }

}
