package yymh.connectfour;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;

public class ConnectFourMouseListener implements MouseListener 
{

	ConnectFourGUIDriver driver;
	AIMoveTimer timer = new AIMoveTimer(3000, new AIMoveAction());

	ConnectFourMouseListener(ConnectFourGUIDriver driver)
	{
		this.driver = driver;
	}
	
	class AIMoveTimer extends Timer
	{

		public AIMoveTimer(int interval, ActionListener listener) 
		{
			super(interval, listener);
			this.setInitialDelay(2000);
		}
		
	}
	
	class AIMoveAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			while (true)
			{
				int move = driver.getGame().makeAIMove();
				
				if (driver.getGame().isValidMove(move))
				{
					driver.makeMove(move);
					timer.stop();
					break;
				}
				
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//moveMade is used to make sure a valid move is made otherwise the computer would make the next move when the user 
		//clicked on a full slot
		boolean moveMade = driver.makeMove(((int)(e.getX() / 100))); 
		if (moveMade)
		{
			driver.getBoard().revalidate();			
			driver.getBoard().repaint();
			
			if (!driver.getGame().checkForWin(driver.getGame().getCurrentPlayer()))
			{
				
				if (driver.getGame().getAILevel() != ConnectFourAI.AILevel.NONE)
				{
					timer.start();
					driver.getBoard().repaint();
					
				}
					
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

