package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import edu.westga.cs6910.pig.model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

/**
 * Defines the panel in which the user selects which Player plays first.
 * 
 * Started by CS6910
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public final class NewGamePane extends GridPane {

	private RadioButton humanPlayerRadioButton;
	private RadioButton computerPlayerRadioButton;
	private RadioButton randomPlayerRadioButton;

	private ComboBox<Integer> goalScoreComboBox;

	private StatusPane gameInfoPane;
	private ComputerPane computerPlayerPane;
	private HumanPane humanPlayerPane;

	private Game theGame;
	private Player theHuman;
	private Player theComputer;

	/**
	 * Creates a Pane for selecting which player goes first and starting the game.
	 * 
	 * @param theGame            - reference to the Game model
	 * @param gameInfoPane       - reference to the Game Info Pane
	 * @param computerPlayerPane - reference to the Computer Player Pane
	 * @param humanPlayerPane    - reference to the Human Player Pane
	 */
	public NewGamePane(Game theGame, StatusPane gameInfoPane, ComputerPane computerPlayerPane,
			HumanPane humanPlayerPane) {
		if (theGame == null) {
			throw new IllegalArgumentException("Invalid game");
		}
		this.theGame = theGame;

		if (gameInfoPane == null) {
			throw new IllegalArgumentException("Invalid Status Pane");
		}
		this.gameInfoPane = gameInfoPane;

		if (computerPlayerPane == null) {
			throw new IllegalArgumentException("Invalid Computer Pane");
		}
		this.computerPlayerPane = computerPlayerPane;

		if (humanPlayerPane == null) {
			throw new IllegalArgumentException("Invalid Human Pane");
		}
		this.humanPlayerPane = humanPlayerPane;

		this.theHuman = this.theGame.getHumanPlayer();
		this.theComputer = this.theGame.getComputerPlayer();

		this.buildPane();
	}

	private void buildPane() {
		this.setHgap(20);

		this.createGoalScoreItems();

		this.createFirstPlayerItems();

		this.reset();
	}

	private void createFirstPlayerItems() {
		Label lblFirstPlayer = new Label("Who plays first? ");
		this.add(lblFirstPlayer, 2, 0);

		this.humanPlayerRadioButton = new RadioButton(this.theHuman.getName() + " first");
		this.humanPlayerRadioButton.setOnAction(new HumanFirstListener());

		this.computerPlayerRadioButton = new RadioButton(this.theComputer.getName() + " first");
		this.computerPlayerRadioButton.setOnAction(new ComputerFirstListener());

		this.randomPlayerRadioButton = new RadioButton("Random Player");
		this.randomPlayerRadioButton.setOnAction(new RandomFirstListener());

		ToggleGroup group = new ToggleGroup();
		this.humanPlayerRadioButton.setToggleGroup(group);
		this.computerPlayerRadioButton.setToggleGroup(group);
		this.randomPlayerRadioButton.setToggleGroup(group);

		this.add(this.humanPlayerRadioButton, 3, 0);
		this.add(this.computerPlayerRadioButton, 4, 0);
		this.add(this.randomPlayerRadioButton, 5, 0);
	}

	private void createGoalScoreItems() {
		Label lblGoalScore = new Label("Initial Goal Score: ");
		this.add(lblGoalScore, 0, 0);

		this.goalScoreComboBox = new ComboBox<Integer>();
		this.goalScoreComboBox.getItems().addAll(100, 50, 20);
		this.goalScoreComboBox.setValue(100);
		this.goalScoreComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int goalScore = NewGamePane.this.goalScoreComboBox.getValue();
				NewGamePane.this.theGame.setGoalScore(goalScore);
				NewGamePane.this.gameInfoPane.update();
			}
		});
		this.add(this.goalScoreComboBox, 1, 0);
	}

	/**
	 * Resets the radio buttons for new selection
	 */
	public void reset() {
		NewGamePane.this.theGame.setGoalScore(NewGamePane.this.goalScoreComboBox.getValue());
		this.humanPlayerRadioButton.setSelected(false);
		this.computerPlayerRadioButton.setSelected(false);
		this.randomPlayerRadioButton.setSelected(false);
	}

	/**
	 * Defines the listener for computer player first button.
	 */
	private class RandomFirstListener implements EventHandler<ActionEvent> {
		@Override
		/**
		 * Enables the ComputerPlayerPanel and starts a new game. Event handler for a
		 * click in the computerPlayerButton.
		 */
		public void handle(ActionEvent arg0) {
			int goalScore = NewGamePane.this.goalScoreComboBox.getValue();

			NewGamePane.this.setDisable(true);

			if (Math.random() * 10 <= 4) {
				NewGamePane.this.computerPlayerPane.setDisable(false);
				NewGamePane.this.theGame.startNewGame(NewGamePane.this.theComputer, goalScore);
			} else {
				NewGamePane.this.humanPlayerPane.setDisable(false);
				NewGamePane.this.theGame.startNewGame(NewGamePane.this.theHuman, goalScore);
			}
		}
	}

	/*
	 * Defines the listener for computer player first button.
	 */
	private class ComputerFirstListener implements EventHandler<ActionEvent> {
		@Override
		/**
		 * Enables the ComputerPlayerPanel and starts a new game. Event handler for a
		 * click in the computerPlayerButton.
		 */
		public void handle(ActionEvent arg0) {
			int goalScore = NewGamePane.this.goalScoreComboBox.getValue();

			NewGamePane.this.computerPlayerPane.setDisable(false);
			NewGamePane.this.setDisable(true);
			NewGamePane.this.theGame.startNewGame(NewGamePane.this.theComputer, goalScore);
		}
	}

	/*
	 * Defines the listener for human player first button.
	 */
	private class HumanFirstListener implements EventHandler<ActionEvent> {
		/*
		 * Sets up user interface and starts a new game. Event handler for a click in
		 * the human player button.
		 */
		@Override
		public void handle(ActionEvent event) {
			int goalScore = NewGamePane.this.goalScoreComboBox.getValue();

			NewGamePane.this.setDisable(true);
			NewGamePane.this.humanPlayerPane.setDisable(false);
			NewGamePane.this.theGame.startNewGame(NewGamePane.this.theHuman, goalScore);
		}
	}
}
