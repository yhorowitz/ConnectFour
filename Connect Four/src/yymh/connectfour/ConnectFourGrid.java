package yymh.connectfour;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.*;

import javax.swing.*;

public class ConnectFourGrid extends JPanel
{
	
	ConnectFour game;
	
	boolean animate = false;
	int animateRow = 0;
	int animateCol = 0;
	int dropYPos = -30;
	Timer dropTimer = new Timer(1, new animateListener());
	
	ConnectFourGrid()
	{
		this.setPreferredSize(new Dimension(ConnectFour.NUM_OF_COLUMNS * 100, ConnectFour.NUM_OF_ROWS * 100));
	}
	
	class animateListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			dropYPos += 10;
			
			if (dropYPos == (animateRow * 100) + 10)
			{
				dropTimer.stop();
				dropYPos = -30;
				animate = false;
			}
			repaint();
		}
		
	}
	
	public void setGame(ConnectFour game) { this.game = game; }
	public ConnectFour getGame() { return game; }
	
	public void animate(int row, int col)
	{
		animate = true;
		animateRow = row;
		animateCol = col;
		dropTimer.start();
	}
	
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g.create();
            
		Area background = new Area(new Rectangle(0, 0, ConnectFour.NUM_OF_COLUMNS * 100, ConnectFour.NUM_OF_ROWS * 100));

		for (int row = 0; row < ConnectFour.NUM_OF_ROWS; row++)
		{
			for (int col = 0; col < ConnectFour.NUM_OF_COLUMNS; col++)
			{
				background.subtract(new Area(new Ellipse2D.Double(col * 100 + 5, row * 100 + 10, 80, 80)));
				
				if (animate)
				{
					if (game.getCurrentPlayer() == 1)
						g.setColor(Color.YELLOW);
					else
						g.setColor(Color.RED);
					
					g.fillOval(animateCol * 100 + 5, dropYPos, 80, 80);

				}
				
				if (game.getCurrentGameState()[row][col] == 0 || (animate && row == animateRow && col == animateCol))
					g.setColor(Color.WHITE);
				else if (game.getCurrentGameState()[row][col] == 1)
					g.setColor(Color.RED);
				else 
					g.setColor(Color.YELLOW);
				
				g.fillOval( col * 100 + 5, row * 100 + 10, 80, 80);

			}
		}

		g2d.setColor(Color.BLUE);
      g2d.fill(background);
      g2d.dispose();
	}
	
}
