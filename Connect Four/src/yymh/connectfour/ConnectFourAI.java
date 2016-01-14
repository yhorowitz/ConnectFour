package yymh.connectfour;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConnectFourAI {


	enum AILevel {
		NONE, BEGINNER, INTERMEDIATE, EXPERT
	}
	
	final static int RECALCULATE_FLAG = 42;

	private int moveDepth = 0;
	private AILevel level = AILevel.NONE;
	
	/**
	 * @return the level
	 */
	public AILevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(AILevel level) {
		this.level = level;
		setDefaultMoveDepthBasedOnLevel(level);
	}

	ConnectFourAI() {
		//default constructor. No AI (2 human players)
	}
	
	ConnectFourAI(AILevel level) {
		setLevel(level);
	}
		
	public void setDefaultMoveDepthBasedOnLevel(AILevel level) {
		//set Search depth based on difficult
		switch (this.level) {
		case NONE:
			setMoveDepth(0);
			System.out.println("AI Move depth set to 0"); //logging
			break;
		case BEGINNER:
			setMoveDepth(3);
			System.out.println("AI Move depth set to 3"); //logging
			break;
		case INTERMEDIATE:
			setMoveDepth(5);
			System.out.println("AI Move depth set to 5"); //logging
			break;
		case EXPERT:
			setMoveDepth(7);
			System.out.println("AI Move depth set to 7"); //logging
			break;
		}
	}
	
	public void setMoveDepth(int depth) {
		this.moveDepth = depth;
	}
	
	public int getColumnForMove(ConnectFour game) 
	{
		int column = -1;
		ArrayList<Integer> columnScores;
		int depth = this.moveDepth;
		
		
		//keep looking for the best move until a valid move is made
		while (true)
		{
			//TODO logging
			System.out.println("Calcualting at depth of " + depth);
			columnScores = minimax(depth, game.getCurrentPlayer(), game);
			column = getBestMove(columnScores);
			
			if (column == RECALCULATE_FLAG) {
				if (depth > 1){
					depth--;
				}
				else {
					// keep making a random move until a valid move is made	
					System.out.println("AT LOWEST DEPTH. CHOOSING RANDOM VALID COLUMN");
					while (true)
					{
						column = (int)(Math.random() * 7);
						
						if (game.isValidMove(column))
							break;
					}
					
					break;
				}
			}
			else
				break;
		}
		
		return column;
	}
	
	//TODO add logging
	public int getBestMove(ArrayList<Integer> columnScores) {
		
		int highestScore = Integer.MIN_VALUE;
		int column = -1;
		
		//check if all scores are equal
		//if yes return flag to notify caller to recalculate at a lower depth
		int baseScore = columnScores.get(0);
		boolean allMovesEqual = true;
		
		for (Integer score : columnScores) {
			if (score != baseScore) {
				allMovesEqual = false;
				break;
			}
		}

		if (allMovesEqual) {
			column = RECALCULATE_FLAG;
		}
		else {
			for (int i = 0; i < columnScores.size(); i++) {
				if (columnScores.get(i) > highestScore)
					highestScore = columnScores.get(i);
			}
		}
		
		while (column != RECALCULATE_FLAG) {
	      	column = ((int) (Math.random() * 7)); 
	      	
	      	if (columnScores.get(column) == highestScore) {
	      		return column;
	      	}
	      }
      
		//logging
		if (column == RECALCULATE_FLAG) {
			System.out.println("ALL MOVES ARE EQUAL. RECALCULATE AT A LOWER DEPTH");
		}
		else {
			System.out.println("Best column is column" + column);
		}
		
		
		return column;
	}
	
	public int getHighestScore(List<Integer> list) {
		int max = Integer.MIN_VALUE;
		int indexOfMax = -1;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) > max)
				max = list.get(i);
		}
		
		return max;
	}

	public int getLowestScore(List<Integer> list) {
		int min = Integer.MAX_VALUE;
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) <= min)
				min = list.get(i);
		}
		
		return min;
	}
	
	/**
	 * Need to implement
    * 	- check for 3 in a row (return how many are found)
    * 	- check for open spots around the 3 in a row that have pieces underneath them
    * 	- perhaps a key value pair to show where the 3 in a row starts and begins to know
    * 	which direction it is going to check its edges.
    * 
    * TODO ADD LOGGING
	 */
	public ArrayList<Integer> minimax(int depth, int turn, ConnectFour game) 
	{	
		//holds future game states for simulation
		ConnectFour futureGameState = new ConnectFour();
		
		//holds the scores for each column in current depth
		ArrayList<Integer> scores = new ArrayList<>(); 
		
		//if (not at the lowest depth)
		//recursively call getColumnForMove(depth--, otherTurn) for each column if the column isnt full
		for (int i = 0; i < ConnectFour.NUM_OF_COLUMNS; i++)
		{			
			int scoreToAdd = 0;
			//ArrayList<Integer> futureScores;

			//make a deep copy of the current game state for simulation
			futureGameState = new ConnectFour();
			futureGameState.setCurrentGameState(ConnectFour.deepCopyArray(game.getCurrentGameState()));
			futureGameState.setCurrentPlayer(game.getCurrentPlayer());
			
			if (futureGameState.isValidMove(i))
			{
				//simulate move in copied game
				futureGameState.makeMove(i);
				
				//if the result is a win condition
				if (futureGameState.checkForWin(futureGameState.getCurrentPlayer()))
				{
					//add Integer.MAX_VALUE or Integer.MIN_VALUE respectively based on whose turn it is
					scoreToAdd = ((turn == 2) ? Integer.MAX_VALUE : Integer.MIN_VALUE);
				}
				else if (depth != 0) //if not a win condition and not at the lowest depth, branch to a lower depth
				{
					futureGameState.switchPlayer();
					ArrayList<Integer> futureScores = (minimax(depth - 1, futureGameState.getCurrentPlayer(), futureGameState));
					
					if (turn == 1) {
						scoreToAdd = getHighestScore(futureScores);
					}
					else {
						scoreToAdd = getLowestScore(futureScores);
					}
						
				}
				else //if at the lowest depth
				{
					//else if 3 in a row with an open space on either end
						//return 1000000
//					if (checkForThreeInARowWithOneEmptyAdjoiningSpace(turn, i, futureGameState))
//						scoreToAdd = 200000;
					if (ConnectFour.checkForThreeInARow(turn, i, futureGameState))
						scoreToAdd = 10000;
					//else if 2 in a row 
						//return 1000
					//else
						//return 0
					else 
						scoreToAdd = 0;
					
					if (turn == 1) //if it is the players turn make it a negative number
						scoreToAdd *= -1;
				}
				
				
			}
			else //if move isnt valid return the worst possible value so this column doesnt get chosen
			{
				scoreToAdd = ((turn == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE);
			}
			
			//add the score for the current column to the list
			scores.add(scoreToAdd);

							
		}
				
		//return the list of column scores
		return scores;

	}
}
