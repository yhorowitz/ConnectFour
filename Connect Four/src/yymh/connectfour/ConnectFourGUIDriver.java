package yymh.connectfour;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ConnectFourGUIDriver 
{
	//Action commands
	final static String ACT_CMD_EXIT = "EXIT";
	final static String ACT_CMD_NEW_GAME = "NEW GAME";
	final static String ACT_CMD_TOGGLE_SOUND = "TOGGLE SOUND";
	final static String ACT_CMD_CHANGE_AI_TO_EASY = "AI TO EASY";
	final static String ACT_CMD_CHANGE_AI_TO_MEDIUM = "AI TO MEDIUM";
	final static String ACT_CMD_CHANGE_AI_TO_HARD = "AI TO HARD";
	final static String ACT_CMD_CHANGE_AI_TO_OFF = "AI TO OFF";
	final static String ACT_CMD_SHOW_RULES = "SHOW RULES";
	
	//TODO fix audio
	private AudioClip tieGameSound = null;//Applet.newAudioClip(this.getClass().getResource("audio/tie_game_sound.wav"));
	private AudioClip dropSound = null;//Applet.newAudioClip(this.getClass().getResource("audio/token_drop.wav"));
	private AudioClip winGameSound = null;//Applet.newAudioClip(this.getClass().getResource("audio/win_sound.wav"));
	
	private ConnectFourGUIBoard board;
	private ConnectFour game;
	private ConnectFourMouseListener mouseListener;
	private ConnectFourActionListener actionListener;
	private boolean soundEnabled = true;
	
	public ConnectFourGUIBoard getBoard() { return board; }
	public ConnectFour getGame () { return game; }
	public boolean isSoundEnabled() { return soundEnabled; }
	public void setBoard(ConnectFourGUIBoard board) { this.board = board; }
	public void setGame(ConnectFour game) { this.game = game; }
	public void toggleSound() { this.soundEnabled = soundEnabled ? false : true; }
	public AudioClip getTieGameSound() { return tieGameSound; }
	public AudioClip getWinGameSound() { return winGameSound; }
	public AudioClip getTokenDropSound() { return dropSound; }
	
	ConnectFourGUIDriver()
	{
		mouseListener = new ConnectFourMouseListener(this);
		actionListener = new ConnectFourActionListener(this);

		newGame();
	}
	
	private void addActionListeners()
	{
		//add action listeners
		board.getGrid().addMouseListener(mouseListener);
		board.getExitButton().addActionListener(actionListener);
		board.getNewGameButton().addActionListener(actionListener);
		board.getRulesButton().addActionListener(actionListener);
		ConnectFourMenuBar menu = (ConnectFourMenuBar) board.getMenu();
		menu.getNewGameItem().addActionListener(actionListener);
		menu.getToggleSoundItem().addActionListener(actionListener);
		menu.getExitItem().addActionListener(actionListener);
		menu.getRulesItem().addActionListener(actionListener);
		menu.getAINoneOption().addActionListener(actionListener);
		menu.getAIEasyOption().addActionListener(actionListener);
		menu.getAIMediumOption().addActionListener(actionListener);
		menu.getAIHardOption().addActionListener(actionListener);
	}
	
	public void newGame()
	{
		
		if (board != null)
			board.dispose();
		if (game != null)
		{
			ConnectFourAI.AILevel aiLevel = game.getAILevel();
			game = new ConnectFour();
			game.setAILevel(aiLevel);
		}
		else
		{
			game = new ConnectFour();
		}
		
		board = new ConnectFourGUIBoard();
		board.getGrid().setGame(game);

		addActionListeners();
	}

	
	public boolean makeMove(int column)
	{
		//figure out which row to change
		for (int row = ConnectFour.NUM_OF_ROWS - 1; row >= 0; row--)
		{
			
			if (game.getCurrentGameState()[row][column] == 0)
			{
				
				game.makeMove(column);

				//animate the drop
				board.getGrid().animate(row, column);
				
				board.revalidate();
				board.repaint();
				
				playSound(getTokenDropSound());
				
				//check if the game is over
				if (game.checkForWin(game.getCurrentPlayer()))
					processWin();
				else if (game.checkForTie())
					processTie();
				else
				{
					game.switchPlayer();
					setHeaderLabel();
				}
				
				return true;

			}
		}
		
		//if the column is full
		JOptionPane.showMessageDialog(null, "That slot is full. Please choose another slot", "Invalid Move", JOptionPane.ERROR_MESSAGE);
		return false; //no valid move was made
	}
	
	public void processWin()
	{
		if (game.getAILevel() == ConnectFourAI.AILevel.NONE)
		{
				board.setHeaderLabelText("                  Congratulations " + game.getCurrentPlayerName() + " has won!");
				playSound(getWinGameSound());
		}
		else
		{
			
			switch (game.getCurrentPlayer())
			{
				case 1: 
					board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_PLAYER_WIN);
					break;
				case 2: 
					board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_AI_WIN); 
					break;
			}
			
			playSound(getTieGameSound());
			
		}
		
		board.getGrid().removeMouseListener(mouseListener);
	}
	
	public void processTie()
	{
		playSound(getTieGameSound());
		board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_TIE_GAME);
		board.getGrid().removeMouseListener(mouseListener);
	}
	
	public void setHeaderLabel()
	{
		if (game.getAILevel() == ConnectFourAI.AILevel.NONE) //if 2 player game
			board.setHeaderLabelText("                  " + game.getCurrentPlayerName() + " it is your turn");
		else if(game.getCurrentPlayer() == 2)
			board.setHeaderLabelText("                  " + "The computer is thinking...");
		else if (game.getAILevel() == ConnectFourAI.AILevel.BEGINNER)
			board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_EASY_AI);
		else if (game.getAILevel() == ConnectFourAI.AILevel.INTERMEDIATE)
			board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_MEDIUM_AI);
		else if (game.getAILevel() == ConnectFourAI.AILevel.EXPERT)
			board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_HARD_AI);

	}
	
	public String getRules() throws FileNotFoundException
	{
		Scanner rulesFile = new Scanner(new File("rules.txt"));
		StringBuilder rules = new StringBuilder("");
		
		while (rulesFile.hasNext())
		{
			rules.append(rulesFile.nextLine());
			rules.append("\n");
		}
		
		rulesFile.close();
		
		return rules.toString();
	}
	
	public void playSound(AudioClip sound)
	{
		//TODO FIX SOUND
		//if (isSoundEnabled())
			//sound.play();
		
	}
	
}
