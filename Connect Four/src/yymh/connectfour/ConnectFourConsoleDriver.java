package yymh.connectfour;

import java.util.Scanner;

/*
 * broken after ai changed
 */
public class ConnectFourConsoleDriver 
{
	static ConnectFour game;
	static int aiLevelChoice;
	static ConnectFourAI.AILevel aiLevel;
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		game = new ConnectFour();

		System.out.println("Please select an AI Difficulty:\n"
				+ "\t1 - None (2 player)\n"
				+ "\t2 - Easy AI\n"
				+ "\t3 - Medium AI\n"
				+ "\t4 - Hard AI\n");
		aiLevelChoice = input.nextInt();
		
		switch (aiLevelChoice) {
		case 1:
			aiLevel = ConnectFourAI.AILevel.NONE;
			break;
		case 2:
			aiLevel = ConnectFourAI.AILevel.BEGINNER;
			break;
		case 3:
			aiLevel = ConnectFourAI.AILevel.INTERMEDIATE;
			break;
		case 4:
			aiLevel = ConnectFourAI.AILevel.EXPERT;
			break;
		}
		
		
		game.setAILevel(aiLevel);
		
		printCurrentBoard(game.getCurrentGameState());
		boolean play = true;
		int moveSelection;

		while(play)
		{
			System.out.println(game.getCurrentPlayerName() + " please enter which column you would like to drop a piece into: ");
			moveSelection = (input.nextInt() - 1);
			if (game.isValidMove(moveSelection))
			{
				game.makeMove(moveSelection);
				printCurrentBoard(game.getCurrentGameState());
				if(game.checkForWin(game.getCurrentPlayer()))
				{
					System.out.println("Congratulations " + game.getCurrentPlayerName() + " you have won!");
					System.out.println("Would you like to play again (Y / N)? ");
					String choice = input.next();
					if (choice.equalsIgnoreCase("Y"))
					{
						game = new ConnectFour();
						game.setAILevel(aiLevel);
						printCurrentBoard(game.getCurrentGameState());
						continue;
					}
					else
					{
						System.out.println("\nThank you for playing!");
						System.exit(0);
					}
				
				}
				else if(game.checkForTie())
				{
					System.out.println("Tie Game");
					System.out.println("Would you like to play again (Y / N)? ");
					String choice = input.next();
					if (choice.equalsIgnoreCase("Y"))
					{
						game = new ConnectFour();
						game.setAILevel(aiLevel);
						printCurrentBoard(game.getCurrentGameState());
						continue;
					}
					else
					{
						System.out.println("\nThank you for playing!");
						System.exit(0);
					}
				
				}
				game.switchPlayer();
				
				if (game.getAILevel() != ConnectFourAI.AILevel.NONE)
				{
					int aiMove = 0;
					System.out.println("The computer is thinking...");
					aiMove = game.makeAIMove();
					System.out.println("The computer drops a piece into column " + (aiMove + 1));
					System.out.println("Best move has been determined to be column " + (aiMove + 1));
					game.makeMove(aiMove);
					printCurrentBoard(game.getCurrentGameState());
					if(game.checkForWin(game.getCurrentPlayer()))
					{
						System.out.println("The AI won.");
						System.out.println("Would you like to play again (Y / N)? ");
						String choice = input.next();
						if (choice.equalsIgnoreCase("Y"))
						{
							game = new ConnectFour();
							game.setAILevel(aiLevel);
							printCurrentBoard(game.getCurrentGameState());
							continue;
						}
						else
						{
							System.out.println("\nThank you for playing!");
							System.exit(0);
						}
					
					}
					else if(game.checkForTie())
					{
						System.out.println("Tie Game");
						System.out.println("Would you like to play again (Y / N)? ");
						String choice = input.next();
						if (choice.equalsIgnoreCase("Y"))
						{
							game = new ConnectFour();
							game.setAILevel(aiLevel);
							printCurrentBoard(game.getCurrentGameState());
							continue;
						}
						else
						{
							System.out.println("\nThank you for playing!");
							System.exit(0);
						}
					
					}
					game.switchPlayer();				
					
				}
				
			}
			else
				System.out.println("That column is full\n");
		}

		input.close();
		
	}
	
	public static void newGame(ConnectFourAI.AILevel level)
	{
		game = new ConnectFour();
		game.setAILevel(level);
		printCurrentBoard(game.getCurrentGameState());
	}
	
	public static void printCurrentBoard(int[][] board)
	{
		String symbol = "-";
		
		System.out.println();
		
		for (int row = 0; row < board.length; row++)
		{	
		
			for (int column = 0; column < board[row].length; column++)
			{
				
				switch (board[row][column])
				{
					case 0: symbol = "-"; break;
					case 1: symbol = "Y"; break;
					case 2: symbol = "R"; break;
				}
				System.out.print(symbol + " ");
				
			}
			
			System.out.println();
			
		}
		
		System.out.println("\n");
		
	}

}
