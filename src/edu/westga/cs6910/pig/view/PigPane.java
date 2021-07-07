package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Defines a GUI for the Pig game. This class was started by CS6910
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class PigPane extends BorderPane {

	private Game theGame;

	private FullPigPane contentPane;

	private PigHelpDialog pigHelpDialog;

	/**
	 * Creates a pane object to provide the view for the specified Game model
	 * object.
	 * 
	 * @param theGame the domain model object representing the Pig game
	 * 
	 * @requires theGame != null
	 * @ensures the pane is displayed properly
	 */
	public PigPane(Game theGame) {
		if (theGame == null) {
			throw new IllegalArgumentException("Invalid game");
		}
		this.theGame = theGame;

		this.pigHelpDialog = new PigHelpDialog();

		this.contentPane = new FullPigPane(this.theGame);

		this.createMenu();

		this.setCenter(this.contentPane);
	}

	private void createMenu() {
		VBox vbxMenuHolder = new VBox();

		PigMenuBar pigMenu = new PigMenuBar(this.theGame, this.contentPane.getGameInfoPane(),
				this.contentPane.getChooseFirstPlayerPane(), this.contentPane.getHumanPlayerPane(),
				this.contentPane.getComputerPlayerPane(), this.pigHelpDialog);

		vbxMenuHolder.getChildren().addAll(pigMenu);
		this.setTop(vbxMenuHolder);
	}
}
