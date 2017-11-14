package ui.dialogs;


//import libraries
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.IllegalFormatException;
import javax.swing.SwingConstants;



public class AddStopDialog extends JDialog implements ActionListener
{
	//declaring static class constants
	public static final int CLOSE_MODE_OKAY = 0;
	public static final int CLOSE_MODE_CANCEL = -1;
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 175;
	private static final Font INPUT_FONT = new Font("Tahoma", Font.PLAIN, 13);
	private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	//declaring local constants
	private final String WINDOW_NAME;
	
	//declaring local instance variables
	private JTextField inputName, inputStopNum;
	private JButton btnOkay, btnCancel;
	private int closeMode;
	private int stopNum;
	private String stopName;

	
	//generic constructor
	public AddStopDialog(JFrame callingFrame, String windowName)
	{
		//set up dialog box
		super(callingFrame, true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle(windowName);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setResizable(false);
		this.setType(Type.POPUP);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//init non-gui
		this.WINDOW_NAME = windowName;
		this.closeMode = CLOSE_MODE_CANCEL;
		
		//set up panels for buttons/inputs
		JPanel inputPanel = new JPanel();
		getContentPane().add(inputPanel);
		inputPanel.setLayout(new GridLayout(2, 0, 35, 3));
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel buffer = new JPanel();
		buffer.setPreferredSize(new Dimension(0,23));
		getContentPane().add(buffer, BorderLayout.NORTH);
		
		
		//add label for stop name
		JTextField labelStopName = new JTextField();
		labelStopName.setHorizontalAlignment(SwingConstants.CENTER);
		labelStopName.setEditable(false);
		labelStopName.setText("Stop Name");
		labelStopName.setFont(LABEL_FONT);
		labelStopName.setColumns(10);
		labelStopName.setBorder(null);
		labelStopName.setBackground(this.getBackground());
		inputPanel.add(labelStopName);
		
		//add input for stop name
		inputName = new JTextField();
		inputName.setText("");
		inputName.setFont(INPUT_FONT);
		inputPanel.add(inputName);
		
		
		//add label for stop name
		JTextField labelStopNum = new JTextField();
		labelStopNum.setHorizontalAlignment(SwingConstants.CENTER);
		labelStopNum.setEditable(false);
		labelStopNum.setText("Stop Number");
		labelStopNum.setFont(LABEL_FONT);
		labelStopNum.setColumns(10);
		labelStopNum.setBorder(null);
		labelStopNum.setBackground(this.getBackground());
		inputPanel.add(labelStopNum);
		
		//add input for stop name
		inputStopNum = new JTextField();
		inputStopNum.setText("");
		inputStopNum.setFont(INPUT_FONT);
		inputPanel.add(inputStopNum);
		
		//add button to change defaults
		btnOkay = new JButton("Okay");
		btnOkay.setPreferredSize(new Dimension(89,23));
		btnOkay.addActionListener(this);
		buttonPanel.add(btnOkay);

		//add button to cancel
		btnCancel = new JButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(89,23));
		btnCancel.addActionListener(this);
		buttonPanel.add(btnCancel);
		
		
		//set visible + tab order
		this.setLocationRelativeTo(callingFrame);
		this.setVisible(true);
	}
	
	
	//generic accessors
	public String getStopName()
	{
		return stopName;
	}
	public int getStopNumber()
	{
		return stopNum;
	}
	public int getCloseMode()
	{
		return closeMode;
	}
	
	
	@Override
	//handle button presses
	public void actionPerformed(ActionEvent ae) 
	{
		//determine source
		Object src = ae.getSource();
		try
		{
			if(src == btnOkay)
			{	
				//set states
				stopName = inputName.getText();
				stopNum = Integer.parseInt(inputStopNum.getText());
				if (stopName == null)
				{
					throw new IllegalArgumentException(); //just hijacking this to exit try block
				}
				else if(stopName.equals(""))
				{
					throw new IllegalArgumentException(); //just hijacking this to exit try block
				}
				
				//set close mode
				closeMode = CLOSE_MODE_OKAY;
				
				//exit
				this.dispose();
			}
			else
			{
				//set states
				stopNum = -1;
				stopName = null;
				
				//exit
				this.dispose();
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, "Invalid Stop Number\nMust be between 0 and 2147483647", WINDOW_NAME, JOptionPane.ERROR_MESSAGE);
		}
		catch (IllegalArgumentException e)
		{
			JOptionPane.showMessageDialog(this, "Invalid Name\nName cannot be blank", WINDOW_NAME, JOptionPane.ERROR_MESSAGE);
		}
	}
}
