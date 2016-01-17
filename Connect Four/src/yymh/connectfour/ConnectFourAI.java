package yymh.connectfour;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectFourAI {

	static final int DEFAULT_BEGINNER_MOVE_DEPTH = 2;
	static final int DEFAULT_INTERMEDIATE_MOVE_DEPTH = 4;
	static final int DEFAULT_EXPERT_MOVE_DEPTH = 7;
	static final int DEFAULT_NO_AI_MOVE_DEPTH = 0;

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
		//set search for next move depth based on difficulty
		switch (this.level) {
		case NONE:
			setMoveDepth(DEFAULT_NO_AI_MOVE_DEPTH);
			break;
		case BEGINNER:
			setMoveDepth(DEFAULT_BEGINNER_MOVE_DEPTH);
			break;
		case INTERMEDIATE:
			setMoveDepth(DEFAULT_INTERMEDIATE_MOVE_DEPTH);
			break;
		case EXPERT:
			setMoveDepth(DEFAULT_EXPERT_MOVE_DEPTH);
			break;
		}
	}
	
	public void setMoveDepth(int depth) {
		this.moveDepth = depth;
		System.out.println("AI Move depth set to " + depth); //TODO logging
	}
	
	public int getColumnForMove(ConnectFour game) {
		
		int column = -1;
		ArrayList<Integer> columnScores;
		
		//TODO ADD TO logging
		System.out.println("AI is calcualting next move at depth of " + this.moveDepth + " future moves");
		
		columnScores = minimax(this.moveDepth, game);
		
		while (true) {
			column = getBestMove(columnScores);
			
			if (game.isValidMove(column))
				break;
		}
		
		//TODO ADD TO logging
		//print score of each column
		for (int i = 0; i < columnScores.size(); i++ ) {
			System.out.println("Score for column " + (i + 1) + " is " + columnScores.get(i));
		}
		
		return column;
		
	}
	
	public int getBestMove(ArrayList<Integer> columnScores) {
		
		int maxScore = Integer.MIN_VALUE;
		int column = -1;
		
		for (int i = 1; i < columnScores.size(); i++) {
			if (columnScores.get(i) > maxScore)
				maxScore = columnScores.get(i);
		}
		
		/*
		 * select a random column until a column with the maxScore is selected.
		 * this creates some sense of randomness when multiple columns have the same score
		 */
		Random rand = new Random();
		while (true) {
			column = rand.nextInt(7);
			  	
			if (columnScores.get(column) == maxScore) {
				return column;
			}
		}
		
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
    * 	  or using 4 loops to check the ones around the area of the most current move
    * 	which direction it is going to check its edges.
    *   - add aplha beta pruning
    * 
    * TODO ADD LOGGING
	 */
	public ArrayList<Integer> minimax(int depth, ConnectFour game) 
	{	
		
		//holds clone of game for simulation
		ConnectFour gameClone = new ConnectFour();
		
		//holds the scores for each column in current depth
		ArrayList<Integer> scores = new ArrayList<>(); 
		
		// depthDiff is used to make wins/losses performed at a later depth less valuable 
		// so that there is no delay if all moves lead to a terminal condition
		int depthDiff = 1000 - (depth * 10);
		
		//get score for each column 
		for (int i = 0; i < ConnectFour.NUM_OF_COLUMNS; i++)
		{			
			int scoreToAdd = 0;
			//ArrayList<Integer> futureScores;

			//make a deep copy of the current game state for simulation
			gameClone = game.clone();
			
			if (gameClone.isValidMove(i))
			{
				//simulate move in copied game
				gameClone.makeMove(i, false);
				
				//TODO ADD TO LOGGING
				//ConnectFourConsoleDriver.printCurrentBoard(gameClone.getCurrentGameState());
			
				//if the result is a win condition
				if (gameClone.checkForWin(gameClone.getCurrentPlayer()))
				{
					//add Integer.MAX_VALUE or Integer.MIN_VALUE respectively based on whose turn it is
					scoreToAdd = (game.isAITurn() ? Integer.MAX_VALUE - depthDiff : Integer.MIN_VALUE + depthDiff);
				}
				else if (depth != 0) //if not a win condition and not at the lowest depth, branch to a lower depth
				{
					gameClone.switchPlayer();
					ArrayList<Integer> futureScores = (minimax(depth - 1, gameClone));
					
					if (game.isAITurn()) {
						scoreToAdd = getLowestScore(futureScores);
					}
					else {
						scoreToAdd = getHighestScore(futureScores);
					}

				}
				else {
	
					if (ConnectFour.checkForThreeInARow(game.getCurrentPlayer(), i, gameClone))
						scoreToAdd = 10000;
					else 
						scoreToAdd = 0;
									
					scoreToAdd += depthDiff;
					
					if (!game.isAITurn()) //if it is the players turn make it a negative number
						scoreToAdd *= -1;
					}
			}
			else //if move isnt valid return the worst possible value so this column doesnt get chosen
			{
				scoreToAdd = ((game.isAITurn()) ? Integer.MIN_VALUE + depthDiff : Integer.MAX_VALUE - depthDiff);
			}
			
			//TODO ADD LOGGING 
			System.out.println("adding Score: " + scoreToAdd + " for column: " + i + " at depth: " + depth);
			
			//add the score for the current column to the list
			scores.add(scoreToAdd);

							
		}
				
		//return the list of column scores
		return scores;

	}
}
