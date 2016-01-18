package yymh.connectfour;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import yymh.connectfour.ConnectFourAI.AILevel;

public class ConnectFourGUIDriver 
{
	//colors
	final static Color YELLOW = new Color(245, 230, 0);
	final static Color PLAYER_1_TOKEN_COLOR = Color.RED;
	final static Color PLAYER_2_TOKEN_COLOR = Color.BLACK;
	final static Color MAIN_COLOR = YELLOW;
	final static Color SECONDARY_COLOR = Color.BLUE;
	
	//Action commands
	final static String ACT_CMD_EXIT = "EXIT";
	final static String ACT_CMD_NEW_GAME = "NEW GAME";
	final static String ACT_CMD_TOGGLE_SOUND = "TOGGLE SOUND";
	final static String ACT_CMD_CHANGE_AI_TO_EASY = "AI TO EASY";
	final static String ACT_CMD_CHANGE_AI_TO_MEDIUM = "AI TO MEDIUM";
	final static String ACT_CMD_CHANGE_AI_TO_HARD = "AI TO HARD";
	final static String ACT_CMD_CHANGE_AI_TO_OFF = "AI TO OFF";
	final static String ACT_CMD_SHOW_RULES = "SHOW RULES";
	final static String ACT_CMD_AI_MAKE_MOVE = "MAKE AI MOVE";
	
	/*
	 * timer that controls AI
	 * A timer is used to ensure that it the AI doesn't move to quickly
	 */
	AIMoveTimer timer = new AIMoveTimer(3000, new AIMoveAction());
	
	String workingDir = System.getProperty("user.dir");
	private static final String TIE_SOUND_URL = "audio/tie_game_sound.wav";
	private static final String WIN_SOUND_URL = "audio/win_sound.wav";
	private static final String LOSE_SOUND_URL = "audio/tie_game_sound.wav";
	private static final String DROP_SOUND_URL = "audio/token_drop.wav";
	private Clip tieGameSoundClip;
	private Clip loseGameSoundClip;
	private Clip winGameSoundClip;
	private Clip tokenDropSoundClip;
	private AudioInputStream tieGameSound;
	private AudioInputStream dropSound;
	private AudioInputStream winGameSound;
	private AudioInputStream loseGameSound;
	Clip clip;

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
	public Clip getTieGameSound() { return tieGameSoundClip; }
	public Clip getWinGameSound() { return winGameSoundClip; }
	public Clip getLoseGameSound() { return loseGameSoundClip; }
	public Clip getTokenDropSound() { return tokenDropSoundClip; }
	
	ConnectFourGUIDriver()
	{
		mouseListener = new ConnectFourMouseListener(this);
		actionListener = new ConnectFourActionListener(this);

		loadSoundFiles();
		//System.out.println(this.getClass().getClassLoader().getResource(TIE_SOUND_URL));
		newGame();
	}
	
	private void loadSoundFiles() {
		
		try {
			tieGameSoundClip = AudioSystem.getClip();
			winGameSoundClip = AudioSystem.getClip();
			loseGameSoundClip = AudioSystem.getClip();
			tokenDropSoundClip = AudioSystem.getClip();
			
			tieGameSound = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(TIE_SOUND_URL));
			dropSound = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(DROP_SOUND_URL));
			winGameSound = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(WIN_SOUND_URL));
			loseGameSound = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(LOSE_SOUND_URL));
			
			tieGameSoundClip.open(tieGameSound);
			winGameSoundClip.open(winGameSound);
			loseGameSoundClip.open(loseGameSound);
			tokenDropSoundClip.open(dropSound);
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			tieGameSoundClip = null;
			winGameSoundClip = null;
			loseGameSoundClip = null;
			tokenDropSoundClip = null;
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			AILevel aiLevel = game.getAILevel();
			game = new ConnectFour();
			game.setAILevel(aiLevel);
		}
		else
		{
			game = new ConnectFour();
		}
		
		board = new ConnectFourGUIBoard();
		board.getGrid().setGame(game);

		if (game.getAILevel() != AILevel.NONE && game.isAITurn()) {
			System.out.println("AI goes first");
			setHeaderLabel();
			timer.start();
		}
		else {
			System.out.println("Human player goes first");
		}
		
		addActionListeners();
	}
	
	public boolean makeMove(int column)
	{
		//figure out which row to change
		for (int row = ConnectFour.NUM_OF_ROWS - 1; row >= 0; row--)
		{
			
			if (game.getCurrentGameState()[row][column] == 0)
			{
				
				game.makeMove(column, true);

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
			boolean isHumanPlayersTurn = game.isAITurn();
			
			if(isHumanPlayersTurn){
				board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_AI_WIN);
				playSound(getLoseGameSound());
				
			}
			else {
				board.setHeaderLabelText(ConnectFourGUIBoard.HEADER_PLAYER_WIN);
				playSound(getWinGameSound());	
			}
						
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
		else if(game.isAITurn())
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
			rules.append(rulesFile.nextLine() + "\n");
		}
		
		rulesFile.close();
		
		return rules.toString();
	}
	
	public void playSound(Clip sound)
	{
		
		if (isSoundEnabled() && sound != null) {
			if (sound.isRunning())
				sound.stop();
			
			sound.setFramePosition(0);
			sound.start();
		}

	}
	
	public void performAIMove() {
		timer.start();
	}
	
	private class AIMoveTimer extends Timer
	{

		public AIMoveTimer(int interval, ActionListener listener) 
		{
			super(interval, listener);
			this.setInitialDelay(2000);
		}
		
	}
	
	private class AIMoveAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			while (true)
			{
				int move = getGame().makeAIMove();
				
				if (getGame().isValidMove(move))
				{
					makeMove(move);
					timer.stop();
					break;
				}
				
			}

		}
	}
	
}
