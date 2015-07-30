package Main;
import javax.swing.JFrame;

/*
 * @author James Yang
 */


public class Main {

	public static void main(String[] args){
		JFrame window = new JFrame("Summer Adventurer"); //title of the window
		//window.setContentPane(new GameLoop());
		window.add(new GameLoop());//adds the gamePanel 
		window.pack();//computes the size of the window
		window.setLocationRelativeTo(window);//puts the frame in the center
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false); //not resizable
		window.setVisible(true);
	}
	
}
