package yymh.connectfour;


import java.awt.*;

import javax.swing.*;

public class ConnectFourGUIBoard extends JFrame
{
	
	//reusable strings for header label
	final static String HEADER_EASY_AI = "                   You Are Playing Against A Beginner Computer Opponent";
	final static String HEADER_MEDIUM_AI = "                  You Are Playing Against An Intermediate Computer Opponent";
	final static String HEADER_HARD_AI = "                  You Are Playing Against An Expert Computer Opponent";
	final static String HEADER_AI_WIN = "                  The computer opponent has won";
	final static String HEADER_PLAYER_WIN = "                  Congratulations you have won!";
	final static String HEADER_TIE_GAME = "                  Tie Game! Please exit or start a new game.";

	final static ImageIcon FRAME_ICON = new ImageIcon("images/frameicon.png");
	
	private Font font = new Font("TimesRoman", Font.BOLD + Font.ITALIC, 16);
	
	//GUI components
	private JLabel headerLabel = new JLabel("             Welcome to Connect Four! Player 1 it is your turn");
	private JButton newGameButton;
	private JButton exitButton;
	private JButton rulesButton;
	private JPanel topPanel;
	private ConnectFourGrid grid = new ConnectFourGrid();
	private JPanel buttonPanel;
	private JMenuBar menuBar = new ConnectFourMenuBar();

	
	public String getHeaderLabelText()
	{
		return headerLabel.getText();
	}
	
	public void setHeaderLabelText(String s)
	{
		headerLabel.setText(s);
	}
	
	public JButton getNewGameButton() { return newGameButton; }
	public JButton getExitButton() { return exitButton; }
	public JButton getRulesButton() { return rulesButton; }
	public ConnectFourGrid getGrid() { return grid; }
	public JMenuBar getMenu() { return menuBar; }
		
	ConnectFourGUIBoard()
	{
		this.setTitle("Connect Four");
		this.setIconImage(FRAME_ICON.getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(ConnectFour.NUM_OF_COLUMNS * 100, ConnectFour.NUM_OF_ROWS * 110);
		this.setJMenuBar(menuBar);
		
		//set up the top panel
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(ConnectFourGUIDriver.SECONDARY_COLOR);
		topPanel.setLayout(new BorderLayout());
		headerLabel.setFont(font);
		headerLabel.setForeground(ConnectFourGUIDriver.MAIN_COLOR);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rulesButton = new JButton("Rules");
		rulesButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_SHOW_RULES);
		topPanel.add(headerLabel, BorderLayout.CENTER);
		topPanel.add(rulesButton, BorderLayout.EAST);
		this.add(topPanel, BorderLayout.NORTH);
		
		this.add(grid, BorderLayout.CENTER);

		//user buttons
		buttonPanel = new JPanel();
		buttonPanel.setBackground(ConnectFourGUIDriver.SECONDARY_COLOR);
		newGameButton = new JButton("New Game");
		newGameButton.setPreferredSize(new Dimension(100, 25));
		newGameButton.setToolTipText("Clears the board and starts the game over");
		newGameButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_NEW_GAME);
		exitButton = new JButton("Exit");
		exitButton.setPreferredSize(new Dimension(100, 25));
		exitButton.setToolTipText("Exits the game");
		exitButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_EXIT);
		buttonPanel.add(newGameButton);
		buttonPanel.add(exitButton);
		this.add(buttonPanel, BorderLayout.SOUTH);
	
		//make the board visible
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
				
	}
	
	
}
