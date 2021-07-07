package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Defines a GUI for the Pig game. This class was started by CS6910
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class PigPane extends BorderPane {
	private Game theGame;

	private BorderPane contentPane;
	private HumanPane humanPlayerPane;
	private ComputerPane computerPlayerPane;
	private StatusPane gameInfoPane;
	private NewGamePane chooseFirstPlayerPane;

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

		this.contentPane = new BorderPane();
		
		this.humanPlayerPane = new HumanPane(theGame);
		HBox leftBox = this.createHBoxHolder(this.humanPlayerPane, true);
		this.contentPane.setLeft(leftBox);

		this.computerPlayerPane = new ComputerPane(theGame);
		HBox centerBox = this.createHBoxHolder(this.computerPlayerPane, true);
		this.contentPane.setCenter(centerBox);

		this.gameInfoPane = new StatusPane(theGame);
		HBox bottomBox = this.createHBoxHolder(this.gameInfoPane, false);
		this.contentPane.setBottom(bottomBox);

		this.chooseFirstPlayerPane = new NewGamePane(theGame, this.gameInfoPane, this.computerPlayerPane,
				this.humanPlayerPane);
		HBox topBox = this.createHBoxHolder(this.chooseFirstPlayerPane, false);
		this.contentPane.setTop(topBox);
		
		this.createMenu();

		this.setCenter(this.contentPane);
	}

	private HBox createHBoxHolder(Pane newPane, boolean disable) {
		newPane.setDisable(disable);
		HBox leftBox = new HBox();
		leftBox.setMinWidth(350);
		leftBox.getStyleClass().add("pane-border");
		leftBox.getChildren().add(newPane);
		return leftBox;
	}

	private void createMenu() {
		VBox vbxMenuHolder = new VBox();

		PigMenuBar pigMenu = new PigMenuBar(this.theGame, this.gameInfoPane, this.chooseFirstPlayerPane,
				this.humanPlayerPane, this.computerPlayerPane, this.pigHelpDialog);

		vbxMenuHolder.getChildren().addAll(pigMenu);
		this.setTop(vbxMenuHolder);
	}
}
