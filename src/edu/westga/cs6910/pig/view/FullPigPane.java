package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Builds the NewGamePane, StatusPane, HumanPane, and ComputerPane and groups
 * them to be added to the PigPane.
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class FullPigPane extends BorderPane {

	private Game theGame;

	private HumanPane humanPlayerPane;
	private ComputerPane computerPlayerPane;
	private StatusPane gameInfoPane;
	private NewGamePane chooseFirstPlayerPane;

	/**
	 * Creates the content Pane containing all of the other Panes.
	 * 
	 * @param theGame - reference to the Pig Game model
	 */
	public FullPigPane(Game theGame) {
		super();

		if (theGame == null) {
			throw new IllegalArgumentException("Invalid Game");
		}
		this.theGame = theGame;

		this.humanPlayerPane = new HumanPane(this.theGame);
		HBox leftBox = this.createHBoxHolder(this.humanPlayerPane, true);
		this.setLeft(leftBox);

		this.computerPlayerPane = new ComputerPane(this.theGame);
		HBox centerBox = this.createHBoxHolder(this.computerPlayerPane, true);
		this.setCenter(centerBox);

		this.gameInfoPane = new StatusPane(this.theGame);
		HBox bottomBox = this.createHBoxHolder(this.gameInfoPane, false);
		this.setBottom(bottomBox);

		this.chooseFirstPlayerPane = new NewGamePane(this.theGame, this.gameInfoPane, this.computerPlayerPane,
				this.humanPlayerPane);
		HBox topBox = this.createHBoxHolder(this.chooseFirstPlayerPane, false);
		this.setTop(topBox);
	}

	private HBox createHBoxHolder(Pane newPane, boolean disable) {
		newPane.setDisable(disable);
		HBox leftBox = new HBox();
		leftBox.setMinWidth(350);
		leftBox.getStyleClass().add("pane-border");
		leftBox.getChildren().add(newPane);
		return leftBox;
	}

	/**
	 * Retrieves a reference to the HumanPane
	 * 
	 * @return - a reference to the Human Player Pane
	 */
	public HumanPane getHumanPlayerPane() {
		return this.humanPlayerPane;
	}

	/**
	 * Retrieves a reference to the ComputerPane
	 * 
	 * @return - a reference to the Computer Player Pane
	 */
	public ComputerPane getComputerPlayerPane() {
		return this.computerPlayerPane;
	}

	/**
	 * Retrieves a reference to the StatusPane
	 * 
	 * @return - a reference to the Game Info Pane
	 */
	public StatusPane getGameInfoPane() {
		return this.gameInfoPane;
	}

	/**
	 * Retrieves a reference to the NewGamePane
	 * 
	 * @return - a reference to the Choose First Player Pane
	 */
	public NewGamePane getChooseFirstPlayerPane() {
		return this.chooseFirstPlayerPane;
	}
}
