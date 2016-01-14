package yymh.connectfour;

import java.util.*;

public class ConnectFour 
{
	
	final static int NUM_OF_ROWS = 6;
	final static int NUM_OF_COLUMNS = 7;
	
	//array to keep track of what is in each currentGameState
	private int[][] currentGameState = new int[NUM_OF_ROWS][NUM_OF_COLUMNS];
	
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	
	//TODO REMOVE private AILevel currentAILevel = new AILevelNone(); 	//keeps track of which AI level is chosen
	private ConnectFourAI ai = new ConnectFourAI();

	private int currentPlayer = 1;
	
	ConnectFour()
	{
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
	}
	public ConnectFourAI.AILevel getAILevel() 
	{ 
		return ai.getLevel();
	}
	
	public int makeAIMove()
	{
		while (true)
		{
			int returnValue = ai.getColumnForMove(this);
			
			if (this.isValidMove(returnValue))
				return returnValue;
			//TODO add logging
		}
	}
	
	public void setCurrentPlayer(int player) { this.currentPlayer = player; }
	public int getCurrentPlayer() { return this.currentPlayer; }
	
	public boolean isValidMove(int column)
	{
		return this.currentGameState[0][column] == 0;
	}
	public void makeMove(int column)
	{
		int row;
		
		//get the first empty row starting from the bottom
		for (row = NUM_OF_ROWS - 1; row >= 0; row--)
		{
			if (this.currentGameState[row][column] == 0)
				break;
		}
		this.currentGameState[row][column] = this.currentPlayer;
		
	}
	
	public void switchPlayer()
	{
		switch (currentPlayer)
		{
			case 1: setCurrentPlayer(getCurrentPlayer() + 1); break;
			case 2: setCurrentPlayer(getCurrentPlayer() - 1); break;
		}
		
	}
	
	public boolean checkForWin(int playerToCheck)
	{
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
	
	public static boolean checkForThreeInARow(int player, int columnOfMove, ConnectFour game)
	{
		//get row
		int row = 0;
		int column = columnOfMove;
		int[][] state = game.getCurrentGameState();
		
		for (int i = 0; i < ConnectFour.NUM_OF_ROWS; i++)
		{
			if (state[i][column] != 0)
			{
				row = i;
				break;
			}
		}		
		
		//check horizontals
		if (column + 2 < ConnectFour.NUM_OF_COLUMNS)
		{
			if ( (state[row][column + 1] == player)&&(state[row][column + 2] == player) )
				return true;
		}
		if (column - 2 >= 0)
		{
			if ( (state[row][column - 1] == player)&&(state[row][column - 2] == player) )
					return true;
		}
		if (column + 1 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0)
		{
			if ( (state[row][column - 1] == player)&&(state[row][column + 1] == player) )
					return true;
		}
			
		//check vertical 
		//Note: There can only be 3 in a row if the 2 below it are the same since the physics of the game wouldn't
		//allow chips above the one being placed.
		if (row + 2 < ConnectFour.NUM_OF_ROWS)
		{
			if ( (state[row + 1][column] == player)&&(state[row + 2][column] == player) )
				return true;
		}
		
		//check diagonals
		
		//check for 2 in front going toward bottom right
		if (row + 2 < ConnectFour.NUM_OF_ROWS && column + 2 < ConnectFour.NUM_OF_COLUMNS)
		{
			if ( (state[row + 1][column + 1] == player)&&(state[row + 2][column + 2] == player) )
				return true;
		}
		//check for 2 in front going toward bottom left
		if (row + 2 < ConnectFour.NUM_OF_ROWS && column - 2 >= 0)
		{
			if ( (state[row + 1][column - 1] == player)&&(state[row + 2][column - 2] == player) )
				return true;
		}
		//check for 2 behind going to upper left
		if (row - 2 >= 0 && column - 2 >= 0)
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row - 2][column - 2] == player) )
				return true;
		}
		//check for 2 behind going to upper right
		if (row - 2 >= 0 && column + 2 < ConnectFour.NUM_OF_COLUMNS)
		{
			if ( (state[row - 1][column + 1] == player)&&(state[row - 2][column + 2] == player) )
				return true;
		}
		//check for 1 each side going both ways
		if ((row + 1 < ConnectFour.NUM_OF_ROWS && row - 1 >= 0) && (column + 1 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0))
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row + 1][column + 1] == player) 
					|| (state[row - 1][column + 1] == player)&&(state[row + 1][column - 1] == player)  )
				return true;
		}
		
		//by default return false
		return false;
	}
	
	/**
	 * Checks for three in a row with at least one empty adjoining space on either end
	 * 
	 * @param player
	 * @param columnOfMove
	 * @param game
	 * @return
	 */
	public boolean checkForThreeInARowWithOneEmptyAdjoiningSpace(int player, int column, ConnectFour game)
	{
		//get row
		int row = 0;
		int[][] state = game.getCurrentGameState();
		
		for (int i = 0; i < ConnectFour.NUM_OF_ROWS; i++)
		{
			if (state[i][column] != 0)
			{
				row = i;
				break;
			}
		}		
		
		//check horizontal's
		if (column + 2 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0)
		{
			if ( (state[row][column + 1] == player)&&(state[row][column + 2] == player) && state[row][column - 1] == 0 )
				return true;
		}
		else if (column - 2 >= 0 && column + 1 < ConnectFour.NUM_OF_COLUMNS)
		{
			if ( (state[row][column - 1] == player)&&(state[row][column - 2] == player) && state[row][column + 1] == 0 )
					return true;
		}
		else if (column + 2 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0)
		{
			if ( (state[row][column - 1] == player)&&(state[row][column + 1] == player) && state[row][column + 2] == 0 )
					return true;
		}

		else if (column + 1 < ConnectFour.NUM_OF_COLUMNS && column - 2 >= 0)
		{
			if ( (state[row][column - 1] == player)&&(state[row][column + 1] == player) && state[row][column - 2] == 0 )
					return true;
		}
		
		
		//check vertical
		if (row + 2 < ConnectFour.NUM_OF_ROWS && row - 1 >= 0)
		{
			if ( (state[row + 1][column] == player)&&(state[row + 2][column] == player) )
				return true;
		}
		
		//check diagonals
		
		//check for 2 in front going toward bottom right
		else if ( (row + 3 < ConnectFour.NUM_OF_ROWS && column + 3 < ConnectFour.NUM_OF_COLUMNS) )
		{
			if ( (state[row + 1][column + 1] == player)&&(state[row + 2][column + 2] == player) && state[row + 3][column + 3] == 0 )
				return true;
		}
		//check for 2 in front going toward bottom left
		if (row + 3 < ConnectFour.NUM_OF_ROWS && column - 3 >= 0)
		{
			if ( (state[row + 1][column - 1] == player)&&(state[row + 2][column - 2] == player) && state[row + 3][column - 3] == 0 )
				return true;
		}
		//check for 2 behind going to upper left
		if (row - 3 >= 0 && column - 3 >= 0)
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row - 2][column - 2] == player) && state[row - 3][column - 3] == 0 )
					return true;
		}
		//check for 2 behind going to upper right
		if (row - 3 >= 0 && column + 3 < ConnectFour.NUM_OF_COLUMNS)
		{
			if ( (state[row - 1][column + 1] == player)&&(state[row - 2][column + 2] == player) && state[row - 3][column + 3] == 0 )
					return true;
		}
		//check for 1 each side going both ways
		if ((row + 2 < ConnectFour.NUM_OF_ROWS && row - 1 >= 0) && (column + 2 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0))
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row + 1][column + 1] == player) && state[row + 2][column + 2] == 0 )
					return true;
		}
		if ((row + 1 < ConnectFour.NUM_OF_ROWS && row - 2 >= 0) && (column + 1 < ConnectFour.NUM_OF_COLUMNS && column - 2 >= 0))
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row + 1][column + 1] == player) && state[row - 2][column - 2] == 0 )
					return true;
		}
		if ((row + 1 < ConnectFour.NUM_OF_ROWS && row - 2 >= 0) && (column + 2 < ConnectFour.NUM_OF_COLUMNS && column - 1 >= 0))
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row + 1][column + 1] == player) && state[row - 2][column + 2] == 0 )
					return true;
		}
		if ((row + 2 < ConnectFour.NUM_OF_ROWS && row - 1 >= 0) && (column + 1 < ConnectFour.NUM_OF_COLUMNS && column - 2 >= 0))
		{
			if ( (state[row - 1][column - 1] == player)&&(state[row + 1][column + 1] == player) && state[row + 2][column - 2] == 0 )
					return true;
		}
		
		//by default return false
		return false;
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
	
	public ConnectFour getCopy()
	{
		ConnectFour copy = new ConnectFour();
		
		return null;
		
	}
	
}
