package yymh.connectfour;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;

import yymh.connectfour.ConnectFourAI.AILevel;

public class ConnectFourMouseListener implements MouseListener 
{

	ConnectFourGUIDriver driver;

	ConnectFourMouseListener(ConnectFourGUIDriver driver)
	{
		this.driver = driver;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//check if the AI is in middle of thinking. only process click if it isn't
		
		//TODO ADD TO LOGGING
		System.out.println("User clicked and it should be reistered: " + (driver.getGame().getAILevel() == AILevel.NONE || !driver.getGame().isAITurn()));
		
		if (driver.getGame().getAILevel() == AILevel.NONE || !driver.getGame().isAITurn()) {
			
			//moveMade is used to make sure a valid move is made otherwise the computer would make the next move when the user 
			//clicked on a full slot
			boolean moveMade = driver.makeMove(((int)(e.getX() / 100))); 
			if (moveMade)
			{
				driver.getBoard().revalidate();			
				driver.getBoard().repaint();
				
				if (!driver.getGame().checkForWin(driver.getGame().getCurrentPlayer()))
				{
					
					if (driver.getGame().getAILevel() != AILevel.NONE)
					{
						driver.performAIMove();
						driver.getBoard().repaint();
					}
						
				}
			}
			
		}
		else {
			//TODO ADD TO LOGGING
			System.out.print("User clicked but it is not registered because: ");
			if (driver.getGame().isAITurn()) {
				System.out.println("it is the AI's turn");
				System.out.println("The human player is player #" + driver.getGame().getHumanPlayerNumber());
				System.out.println("The current player is player #" + driver.getGame().getCurrentPlayer());
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}

