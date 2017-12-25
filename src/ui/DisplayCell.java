package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class DisplayCell extends JPanel
{
	//declaring static constants
	private static final Border DEFAULT_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private static final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 30);
	private static final Font INFO_FONT = new Font("Monospaced", Font.PLAIN, 20);
	private static final Color FONT_COLOR = Color.ORANGE;
	private static final Color BACKGROUND_1 = new Color(2*48, 2*10, 2*36);
	private static final Color BACKGROUND_2 = new Color(48, 10, 36);
	
	//declaring static variables
	private static boolean background = true;
	
	//declaring instance variables
	private JTextArea title, info;
	
	//switch backgrounds
	private static Color getBackgroundColor()
	{
		background = !background;
		if (background)		return BACKGROUND_2;
		else				return BACKGROUND_1;
	}
	
	
	//empty constructor
	public DisplayCell()
	{
		this("","");
	}
	
	
	//generic constructor
	public DisplayCell(String titleStr, String infoStr)
	{
		//init cell
		super(new BorderLayout(0,0));
		this.setBorder(DEFAULT_BORDER);
		title = new JTextArea();
		info = new JTextArea();
		
		Color bg = getBackgroundColor();
		
		title.setBackground(bg);
		title.setForeground(FONT_COLOR);
		title.setBorder(null);
		title.setFont(TITLE_FONT);
		
		info.setBackground(bg);
		info.setForeground(FONT_COLOR);
		info.setBorder(null);
		info.setFont(INFO_FONT);
		
		this.add(title, BorderLayout.NORTH);
		this.add(info, BorderLayout.CENTER);
		
		//add text
		title.setText(titleStr);
		info.setText(infoStr);
	}
	
	
	//set title/info
	public void setText(String titleStr, String infoStr)
	{
		this.title.setText(" "+titleStr);
		this.info.setText("  "+infoStr);
	}
}
