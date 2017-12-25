package octy;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;

public class OctyDisplay extends JFrame
{
	//declaring class constants
	private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 13);
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
	private static final Color DEFAULT_TEXT_COLOR = Color.ORANGE;
	
	
	public OctyDisplay(boolean fullscreen) 
	{
		//init frame
		super();
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
		this.getContentPane().setForeground(DEFAULT_TEXT_COLOR);
		
		
		
		//set visible
		if (fullscreen)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			this.setUndecorated(true);
		}
		this.setVisible(true);
	}
}
