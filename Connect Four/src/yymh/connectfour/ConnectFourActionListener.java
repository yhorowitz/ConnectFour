package yymh.connectfour;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Date;

import javax.swing.*;

import yymh.connectfour.ConnectFourAI.AILevel;

public class ConnectFourActionListener implements ActionListener 
{

	ConnectFourGUIDriver driver;
	
	ConnectFourActionListener(ConnectFourGUIDriver driver)
	{
		this.driver = driver;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals(ConnectFourGUIDriver.ACT_CMD_EXIT))
		{
			System.exit(0);
		}
		
		else if (e.getActionCommand().equals(ConnectFourGUIDriver.ACT_CMD_NEW_GAME))
		{
			driver.newGame();
		}
		
		else if (e.getActionCommand().equals(ConnectFourGUIDriver.ACT_CMD_TOGGLE_SOUND))
		{
			driver.toggleSound();
			
			JCheckBoxMenuItem cb = null;
			
			if (e.getSource() instanceof JCheckBoxMenuItem)
				cb = (JCheckBoxMenuItem)e.getSource();
			else
				System.out.println("Error: Toggle Sound Command sent from an unknown location.");
			
			//switch checkbox text
			if (cb.isSelected())
				cb.setText("Sound (on)");
			else
				cb.setText("Sound (off)");
			
		}
		else if (e.getActionCommand().equals(ConnectFourGUIDriver.ACT_CMD_SHOW_RULES))
		{
			try
			{
				String rules = driver.getRules();
				JOptionPane.showMessageDialog(null, rules, "Rules", JOptionPane.PLAIN_MESSAGE);
			}
			catch(FileNotFoundException ex)
			{
				JOptionPane.showMessageDialog(null, new Date() + "\nError: The file can not be found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getActionCommand() == ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_OFF)
		{
			if (driver.getGame().getAILevel() != AILevel.NONE) {
				driver.getGame().setAILevel(ConnectFourAI.AILevel.NONE);
				driver.getBoard().setHeaderLabelText(ConnectFourGUIBoard.HEADER_EASY_AI);
				ConnectFourMenuBar menu = (ConnectFourMenuBar)driver.getBoard().getMenu();
				menu.getAINoneOption().setSelected(true);
				driver.newGame();
			}
		}
		else if (e.getActionCommand() == ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_EASY)
		{
			if (driver.getGame().getAILevel() != AILevel.BEGINNER) {
				driver.getGame().setAILevel(ConnectFourAI.AILevel.BEGINNER);
				driver.getBoard().setHeaderLabelText(ConnectFourGUIBoard.HEADER_EASY_AI);
				ConnectFourMenuBar menu = (ConnectFourMenuBar)driver.getBoard().getMenu();
				menu.getAIEasyOption().setSelected(true);
				driver.newGame();
			}
		}
		else if (e.getActionCommand() == ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_MEDIUM)
		{
			if (driver.getGame().getAILevel() != AILevel.INTERMEDIATE) {
				driver.getGame().setAILevel(ConnectFourAI.AILevel.INTERMEDIATE);
				driver.getBoard().setHeaderLabelText(ConnectFourGUIBoard.HEADER_MEDIUM_AI);
				ConnectFourMenuBar menu = (ConnectFourMenuBar)driver.getBoard().getMenu();
				menu.getAIMediumOption().setSelected(true);
				driver.newGame();
			}
		}
		else if (e.getActionCommand() == ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_HARD)
		{
			if (driver.getGame().getAILevel() != AILevel.EXPERT) {
				driver.getGame().setAILevel(ConnectFourAI.AILevel.EXPERT);
				driver.getBoard().setHeaderLabelText(ConnectFourGUIBoard.HEADER_HARD_AI);
				ConnectFourMenuBar menu = (ConnectFourMenuBar)driver.getBoard().getMenu();
				menu.getAIHardOption().setSelected(true);
				driver.newGame();
			}
		}

	}


}
