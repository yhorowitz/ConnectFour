package yymh.connectfour;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class ConnectFourMenuBar extends JMenuBar
{

	//menu bar components
	private JMenu systemMenu = new JMenu("System");
	private JMenu aiMenu = new JMenu("AI");
	private JMenuItem newGameMenuButton = new JMenuItem("New Game");
	private JMenuItem rulesMenuButton = new JMenuItem("Rules");
	private JMenuItem statsMenuButton = new JMenuItem("Statistics");
	private JMenuItem achievementsMenuButton = new JMenuItem("Achievements");
	private JMenuItem exitMenuButton = new JMenuItem("Exit");
	private JCheckBoxMenuItem toggleSoundMenuCheckBox = new JCheckBoxMenuItem("Sound (on)"); 
	private JRadioButtonMenuItem aiNone = new JRadioButtonMenuItem("None (2 Player)");
	private JRadioButtonMenuItem aiEasy = new JRadioButtonMenuItem("Beginner");
	private JRadioButtonMenuItem aiMedium = new JRadioButtonMenuItem("Intermediate");
	private JRadioButtonMenuItem aiHard = new JRadioButtonMenuItem("Expert");
	
	public JMenuItem getNewGameItem() { return newGameMenuButton; }
	public JMenuItem getRulesItem() { return rulesMenuButton; }
	public JMenuItem getStatsItem() { return statsMenuButton; }
	public JMenuItem getAchievmentItem() { return achievementsMenuButton; }
	public JMenuItem getExitItem() { return exitMenuButton; }
	public JCheckBoxMenuItem getToggleSoundItem() { return toggleSoundMenuCheckBox; }
	public JRadioButtonMenuItem getAINoneOption() { return aiNone; }
	public JRadioButtonMenuItem getAIEasyOption() { return aiEasy; }
	public JRadioButtonMenuItem getAIMediumOption() { return aiMedium; }
	public JRadioButtonMenuItem getAIHardOption() { return aiHard; }
	

	ConnectFourMenuBar()
	{
		//set up action commands
		newGameMenuButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_NEW_GAME);
		toggleSoundMenuCheckBox.setActionCommand(ConnectFourGUIDriver.ACT_CMD_TOGGLE_SOUND);
		exitMenuButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_EXIT);
		rulesMenuButton.setActionCommand(ConnectFourGUIDriver.ACT_CMD_SHOW_RULES);
		aiNone.setActionCommand(ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_OFF);
		aiEasy.setActionCommand(ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_EASY);
		aiMedium.setActionCommand(ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_MEDIUM);
		aiHard.setActionCommand(ConnectFourGUIDriver.ACT_CMD_CHANGE_AI_TO_HARD);
		
		//set accelerators
		newGameMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		exitMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		toggleSoundMenuCheckBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		aiNone.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
		aiEasy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		aiMedium.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		aiHard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
		
		//set up menu bar
		this.add(systemMenu);
		this.add(aiMenu);
		systemMenu.add(newGameMenuButton);
		systemMenu.add(toggleSoundMenuCheckBox);
		toggleSoundMenuCheckBox.setSelected(true);
		toggleSoundMenuCheckBox.setText("Sound (on)");
		systemMenu.addSeparator();
		systemMenu.add(rulesMenuButton);
		//systemMenu.add(statsMenuButton);
		//systemMenu.add(achievementsMenuButton);
		systemMenu.addSeparator();
		systemMenu.add(exitMenuButton);
		ButtonGroup aiSelection = new ButtonGroup();
		aiSelection.add(aiNone);
		aiMenu.add(aiNone);
		aiSelection.add(aiEasy);
		aiMenu.add(aiEasy);
		aiSelection.add(aiMedium);
		aiMenu.add(aiMedium);
		aiSelection.add(aiHard);
		aiMenu.add(aiHard);
		aiNone.setSelected(true);
	}
	
}
