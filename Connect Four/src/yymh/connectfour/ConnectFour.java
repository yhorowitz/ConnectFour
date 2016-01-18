package yymh.connectfour;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import yymh.connectfour.ConnectFourAI.AILevel;

public class ConnectFour 
{
	
	final static int NUM_OF_ROWS = 6;
	final static int NUM_OF_COLUMNS = 7;
	
	//array to keep track of what is in each currentGameState
	private int[][] currentGameState = new int[NUM_OF_ROWS][NUM_OF_COLUMNS];
	
	//store the move history of the game in format [move_number][player][move]
	private TreeMap<Integer, int[]>gameHistory = new TreeMap<Integer, int[]>();
	
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	
	private ConnectFourAI ai = new ConnectFourAI();

	private int currentPlayer = 1;
	private int humanPlayerNumber;

	ConnectFour() {
		//default constructor
	}
	
	public void setCurrentGameState(int[][] currentGameState) { this.currentGameState = currentGameState; }
	public int[][] getCurrentGameState() { return currentGameState; }
	
	public void setPlayer1Name(String name) { this.player1Name = name; }
	public void setPlayer2Name(String name) { this.player2Name = name; }
	public String getPlayer1Name() { return player1Name; }
	public String getPlayer2Name() { return player2Name; }
	public String getCurrentPlayerName() { return getCurrentPlayer() == 1 ?  getPlayer1Name() : getPlayer2Name(); }
	
	public void setAILevel(ConnectFourAI.AILevel level) 
	{ 
		ai.setLevel(level);
		
		if (level != AILevel.NONE) {
			int rand = new Random().nextInt(2) + 1;
			humanPlayerNumber = rand;
		}
	}
	
	public boolean isAITurn() {
		return currentPlayer != humanPlayerNumber;
	}

	public ConnectFourAI.AILevel getAILevel() 
	{ 
		return ai.getLevel();
	}
	
	public int makeAIMove() {
		while (true)
		{
			int column = ai.getColumnForMove(this);
			
			//TODO add logging
			System.out.println("AI moves in column " + (column + 1));
			
			if (this.isValidMove(column))
				return column;
		}
	}
	
	public void setCurrentPlayer(int player) { this.currentPlayer = player; }
	public int getCurrentPlayer() { return this.currentPlayer; }
	public int getHumanPlayerNumber() { return humanPlayerNumber; }
	public void setHumanPlayerNumber(int humanPlayerNumber) { this.humanPlayerNumber = humanPlayerNumber; }
	
	public boolean isValidMove(int column) {
		return this.currentGameState[0][column] == 0;
	}
	
	public void makeMove(int column, boolean saveHistory) {
		int row;
		
		//get the first empty row starting from the bottom
		for (row = NUM_OF_ROWS - 1; row >= 0; row--)
		{
			if (this.currentGameState[row][column] == 0)
				break;
		}
		this.currentGameState[row][column] = this.currentPlayer;
		
		if (saveHistory) {
			addMoveToGameHistory(this.currentPlayer, column);
			printGameHistory();
		}
	}
	
	public TreeMap<Integer, int[]> getGameHistory(){
		return gameHistory;
	}
	
	private void addMoveToGameHistory(int player, int column) {
		//find first space in array that is null (it will be the next move)
		int moveNumber = gameHistory.size() + 1;
		
		gameHistory.put(moveNumber, new int[] {player, column});
	}
	
	public void printGameHistory() {
		int moveNumber = -1;
		int player = -1;
		int column = -1;
		//TODO add to log
		System.out.println("Game History:");
		for (int i = 0; i < gameHistory.size(); i++) {
			moveNumber = i + 1;
			player = gameHistory.get(moveNumber)[0];
			column = gameHistory.get(moveNumber)[1];
			System.out.println("Move " + moveNumber + ": player " + player + " moved in column " + (column + 1));
			
		}
		System.out.println();
	}
	
	public void switchPlayer() {
		switch (currentPlayer)
		{
			case 1: setCurrentPlayer(getCurrentPlayer() + 1); break;
			case 2: setCurrentPlayer(getCurrentPlayer() - 1); break;
		}
		
	}
	
	public boolean checkForWin(int playerToCheck) {
		boolean isWin = false;

		//check for horizontal win
		for (int row = 0; row < currentGameState.length; row++)
		{
			for (int column = 0; column < currentGameState[row].length - 3; column++)
			{
					if (currentGameState[row][column] == playerToCheck && currentGameState[row][column + 1] == playerToCheck && currentGameState[row][column + 2] == playerToCheck && currentGameState[row][column + 3] == playerToCheck)
						isWin = true;
				
			}
		}
		
		//check for vertical win
		for (int column = 0; column < currentGameState[0].length; column++)
		{
			for (int row = 0; row < currentGameState.length - 3; row++)
			{
					if (currentGameState[row][column] == playerToCheck && currentGameState[row + 1][column] == playerToCheck && currentGameState[row + 2][column] == playerToCheck && currentGameState[row + 3][column] == playerToCheck)
						isWin = true;
				
			}
		}
	
		//check for diagonal win
		for (int row = 0; row < currentGameState.length - 3; row++)
		{
			for (int column = 0; column < currentGameState[row].length - 3; column++)
			{
			
					if (currentGameState[row][column] == playerToCheck && currentGameState[row + 1][column + 1] == playerToCheck && currentGameState[row + 2][column + 2] == playerToCheck && currentGameState[row + 3][column + 3] == playerToCheck)
						isWin = true;	
			}
		}
		
		for (int row = 0; row < currentGameState.length - 3; row++)
		{
			for (int column = 3; column < currentGameState[row].length; column++)
			{
				if (currentGameState[row][column] == playerToCheck && currentGameState[row + 1][column - 1] == playerToCheck && currentGameState[row + 2][column - 2] == playerToCheck && currentGameState[row + 3][column - 3] == playerToCheck)
					isWin = true;
				
			}
			
		}
		
		return isWin;
	
	}
	
	public boolean checkForTie()
	{
		
		for (int column = 0; column < NUM_OF_COLUMNS; column++)
			if (currentGameState[0][column] == 0)
				return false;
		
		return true;
			
	}
	
	public ConnectFour clone() {
		ConnectFour clone = new ConnectFour();
		clone.setCurrentGameState(deepCopyArray(this.getCurrentGameState()));
		clone.setCurrentPlayer(this.getCurrentPlayer());
		clone.setHumanPlayerNumber(this.getHumanPlayerNumber());
		
		return clone;
	}
	
	public static int[][] deepCopyArray(int array[][])
	{
		int[][] newArray = new int[array.length][array[0].length];
		
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array[0].length; j++)
				newArray[i][j] = array[i][j];
		}
		
		return newArray;
	}
	
}
