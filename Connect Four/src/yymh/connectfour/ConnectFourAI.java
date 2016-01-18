package yymh.connectfour;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectFourAI {

	static final int DEFAULT_BEGINNER_MOVE_DEPTH = 3;
	static final int DEFAULT_INTERMEDIATE_MOVE_DEPTH = 5;
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
		
		for (int i = 0; i < columnScores.size(); i++) {
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
				//System.out.println("max score is: " + maxScore);
				//System.out.println("returning column #: " + column);
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
		int depthDiff = (this.moveDepth - depth);
		
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
			
				//if the result is a win condition
				if (gameClone.checkForWin(gameClone.getCurrentPlayer()))
				{
					//add Integer.MAX_VALUE or Integer.MIN_VALUE respectively based on whose turn it is
					scoreToAdd = (game.isAITurn() ? Integer.MAX_VALUE - depthDiff : Integer.MIN_VALUE + depthDiff);
				}
				else if (depth > 1) //if not a win condition and not at the lowest depth, branch to a lower depth
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
					//score the state of the board
					scoreToAdd = scoreBoardState(gameClone);
									
					//scoreToAdd += depthDiff;
					
					if (!game.isAITurn()) //if it is the players turn make it a negative number
						scoreToAdd *= -1;
				}
			}
			else //if move isnt valid return the worst possible value so this column doesnt get chosen
			{
				scoreToAdd = ((game.isAITurn()) ? Integer.MIN_VALUE + depthDiff : Integer.MAX_VALUE - depthDiff);
			}
			
			//TODO ADD LOGGING (maybe)
			//System.out.println("adding Score: " + scoreToAdd + " for column: " + i + " at depth: " + depth);
			
			//add the score for the current column to the list
			scores.add(scoreToAdd);

							
		}
				
		//return the list of column scores
		return scores;

	}
	
	public int scoreBoardState(ConnectFour game){
		int totalScore = 0;
		int aiScore = 0;
		int playerScore = 0;
		int[][] boardState = game.getCurrentGameState();
		
		/*
		 * SCORING (for MAX, MIN is score for MAX * -1):
		 * 
		 * WIN CONDITION = 250,000
		 * ---------------------
		 * horizontal & diagonal
		 * ---------------------
		 * 3 IAR + 1 OS each side = 5,000
		 * 3 IAR + 1 OS one side = 200
		 * 3 IAR + 0 OS = 0
		 * 
		 * 2 IAR + 2 OS each side = 1000
		 * 2 IAR + 1 OS each side = 600
		 * 2 IAR + 2 OS one side = 600
		 * 2 IAR + 1 or less OS = 0
		 * 
		 * 1 IAR + 2 OS each side = 300
		 * 1 IAR + 3 OS any sides = 50
		 * 
		 * -----------
		 * vertical
		 * -----------
		 * 3 IAR = 500
		 * 2 IAR = 100
		 */
		
		//SCORES
		final int SCORE_THREE_IAR_TWO_OS = 5000;
		final int SCORE_THREE_IAR_ONE_OS = 2000;
		
		final int SCORE_TWO_IAR_FOUR_OS = 1000;
		final int SCORE_TWO_IAR_THREE_OS = 800;
		final int SCORE_TWO_IAR_TWO_OS = 600;

		final int SCORE_ONE_IAR_FOUR_OS = 300;
		final int SCORE_ONE_IAR_THREE_OS = 50;
		
		final int SCORE_VERT_THREE_IAR = 500;
		final int SCORE_VERT_TWO_IAR = 100;

		
		final int EM = 0;
		final int AI = game.isAITurn() ? game.getCurrentPlayer() : game.getHumanPlayerNumber();
		final int PL = game.getHumanPlayerNumber();
		
		//calculate score for current state of the board in each direction
		
		/*
		 * (horizontal)
		 * check for 3 IAR + 1 OS each side 
		 * or for 3 IAR + 1 OS on one side
		 */
		for (int i = 0; i < boardState.length; i++) {
			for (int j = 0; j < boardState[i].length - 4; j++) {
				
				//look for 3IAR+1OS each side for AI (only if there is enough room left on the row for it to be possible)
				if (j < boardState[i].length - 5 &&
					(boardState[i][j] == EM && boardState[i][j + 1] == AI && boardState[i][j + 2] == AI && boardState[i][j + 3] == AI && boardState[i][j + 4] == EM)) {
						aiScore += SCORE_THREE_IAR_TWO_OS;
				}
				//look for 3IAR+1OS before
				else if (boardState[i][j] == EM && boardState[i][j + 1] == AI && boardState[i][j + 2] == AI && boardState[i][j + 3] == AI) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
				//look for 3IAR+1OS after
				else if (boardState[i][j] == AI && boardState[i][j + 1] == AI && boardState[i][j + 2] == AI && boardState[i][j + 3] == EM) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
					
			}
		}
		
		//check for 2 or 3 in a row vertically
		for (int i = 0; i < boardState[0].length; i++) {
			for (int j = 0; j < boardState.length - 3; j++) {
				
				//check for 3 in a row
				if (boardState[j][i] == EM && boardState[j + 1][i] == AI && boardState[j + 2][i] == AI && boardState[j + 3][i] == AI) {
					aiScore += SCORE_VERT_THREE_IAR;
				}	
				//check for 2 in a row
				else if (boardState[j][i] == EM && boardState[j + 1][i] == EM && boardState[j + 2][i] == AI && boardState[j + 3][i] == AI) {
					aiScore += SCORE_VERT_TWO_IAR;
				}
			}
		}
		
		//diagonal upper left -> bottom right
		for (int i = 0; i < boardState.length - 4; i++) {
			for (int j = 0; j < boardState[i].length - 4; j++) {
				
				//look for 3IAR+1OS each side for AI (only if there is enough room left on the row for it to be possible)
				if (i < boardState.length - 5 && j < boardState[i].length - 5 && 
					(boardState[i][j] == EM && boardState[i + 1][j + 1] == AI && boardState[i + 2][j + 2] == AI && boardState[i + 3][j + 3] == AI && boardState[i + 4][j + 4] == EM)) {
						aiScore += SCORE_THREE_IAR_TWO_OS;
					
				}
				//look for 3IAR+1OS before
				else if (boardState[i][j] == EM && boardState[i + 1][j + 1] == AI && boardState[i + 2][j + 2] == AI && boardState[i + 3][j + 3] == AI) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
				//look for 3IAR+1OS after
				else if (boardState[i][j] == AI && boardState[i + 1][j + 1] == AI && boardState[i + 2][j + 2] == AI && boardState[i + 3][j + 3] == EM) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
				
			}
		}
		
		//diagonal upper right -> bottom left
		for (int i = 0; i < boardState.length - 4; i++) {
			for (int j = 3; j < boardState[i].length; j++) {
				
				//check for how many in a row. if found skip past it (dont double count)
				//check if it there are open spaces around it
				if (i < boardState.length && j > 3 &&
					(boardState[i][j] == EM && boardState[i + 1][j - 1] == AI && boardState[i + 2][j - 2] == AI && boardState[i + 3][j - 3] == AI && boardState[i + 4][j - 4] == EM)) {
						aiScore += SCORE_THREE_IAR_TWO_OS;
				}
				//look for 3IAR+1OS before
				else if (boardState[i][j] == EM && boardState[i + 1][j - 1] == AI && boardState[i + 2][j - 2] == AI && boardState[i + 3][j - 3] == AI) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
				//look for 3IAR+1OS after
				else if (boardState[i][j] == AI && boardState[i + 1][j - 1] == AI && boardState[i + 2][j - 2] == AI && boardState[i + 3][j - 3] == EM) {
					aiScore += SCORE_THREE_IAR_ONE_OS;
				}
			}
		}
		
		//calculate disjointed 3IARs
		//ConnectFourConsoleDriver.printCurrentBoard(boardState);
		//System.out.println("Calculated Score: " + aiScore);
		return aiScore;
	}
	
}
