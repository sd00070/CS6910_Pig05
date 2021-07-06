package edu.westga.cs6910.pig.view;

import java.util.Optional;

import edu.westga.cs6910.pig.model.Game;
import edu.westga.cs6910.pig.model.Player;
import edu.westga.cs6910.pig.model.strategies.CautiousStrategy;
import edu.westga.cs6910.pig.model.strategies.GreedyStrategy;
import edu.westga.cs6910.pig.model.strategies.PigStrategy;
import edu.westga.cs6910.pig.model.strategies.RandomStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
	private BorderPane pnContent;
	private HumanPane pnHumanPlayer;
	private ComputerPane pnComputerPlayer;
	private StatusPane pnGameInfo;
	private NewGamePane pnChooseFirstPlayer;
	private boolean shouldShowHelpDialog;

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

		this.shouldShowHelpDialog = true;
		this.shouldShowHelpDialog = this.showHelpDialog();
		this.pnContent = new BorderPane();

		this.createMenu();

		this.pnChooseFirstPlayer = new NewGamePane(theGame);
		HBox topBox = this.createHBoxHolder(this.pnChooseFirstPlayer, false);
		this.pnContent.setTop(topBox);

		this.pnHumanPlayer = new HumanPane(theGame);
		HBox leftBox = this.createHBoxHolder(this.pnHumanPlayer, true);
		this.pnContent.setLeft(leftBox);

		this.pnComputerPlayer = new ComputerPane(theGame);
		HBox centerBox = this.createHBoxHolder(this.pnComputerPlayer, true);
		this.pnContent.setCenter(centerBox);

		this.pnGameInfo = new StatusPane(theGame);
		HBox bottomBox = this.createHBoxHolder(this.pnGameInfo, false);
		this.pnContent.setBottom(bottomBox);

		this.setCenter(this.pnContent);
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

		MenuBar mnuMain = new MenuBar();

		Menu mnuFile = this.createGameMenu();

		Menu mnuSettings = this.createStrategyMenu();

		mnuMain.getMenus().addAll(mnuFile, mnuSettings);
		vbxMenuHolder.getChildren().addAll(mnuMain);
		this.setTop(vbxMenuHolder);
	}

	private Menu createStrategyMenu() {
		Menu mnuSettings = new Menu("_Strategy");
		mnuSettings.setMnemonicParsing(true);

		ToggleGroup tglStrategy = new ToggleGroup();

		RadioMenuItem mnuCautious = new RadioMenuItem("_Cautious");
		mnuCautious.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
		mnuCautious.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PigPane.this.theGame.getComputerPlayer().setStrategy(new CautiousStrategy());
			}
		});
		mnuCautious.setMnemonicParsing(true);
		mnuCautious.setToggleGroup(tglStrategy);

		RadioMenuItem mnuGreedy = new RadioMenuItem("Gr_eedy");
		mnuGreedy.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
		mnuGreedy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PigPane.this.theGame.getComputerPlayer().setStrategy(new GreedyStrategy());
			}
		});
		mnuGreedy.setMnemonicParsing(true);
		mnuGreedy.setToggleGroup(tglStrategy);

		RadioMenuItem mnuRandom = new RadioMenuItem("_Random");
		mnuRandom.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
		mnuRandom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PigPane.this.theGame.getComputerPlayer().setStrategy(new RandomStrategy());
			}
		});
		mnuRandom.setMnemonicParsing(true);
		mnuRandom.setToggleGroup(tglStrategy);

		PigStrategy currentStrategy = this.theGame.getComputerPlayer().getStrategy();
		if (currentStrategy.getClass() == CautiousStrategy.class) {
			mnuCautious.setSelected(true);
		} else if (currentStrategy.getClass() == RandomStrategy.class) {
			mnuRandom.setSelected(true);
		} else {
			mnuGreedy.setSelected(true);
		}

		mnuSettings.getItems().addAll(mnuCautious, mnuGreedy, mnuRandom);
		return mnuSettings;
	}

	private Menu createGameMenu() {
		Menu mnuGame = new Menu("_Game");
		mnuGame.setMnemonicParsing(true);

		MenuItem mnuNew = new MenuItem("_New");
		mnuNew.setMnemonicParsing(true);
		mnuNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
		mnuNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PigPane.this.pnChooseFirstPlayer.reset();
				PigPane.this.pnChooseFirstPlayer.setDisable(false);
				PigPane.this.pnHumanPlayer.setDisable(true);
				PigPane.this.pnComputerPlayer.setDisable(true);
				if (PigPane.this.shouldShowHelpDialog) {
					PigPane.this.shouldShowHelpDialog = PigPane.this.showHelpDialog();
				}
				PigPane.this.theGame.resetGame();
				PigPane.this.pnGameInfo.clearInformation();
				PigPane.this.pnHumanPlayer.clearInformation();
				PigPane.this.pnComputerPlayer.clearInformation();
			}
		});

		MenuItem mnuExit = new MenuItem("E_xit");
		mnuExit.setMnemonicParsing(true);
		mnuExit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
		mnuExit.setOnAction(event -> System.exit(0));

		mnuGame.getItems().addAll(mnuNew, mnuExit);
		return mnuGame;
	}

	protected boolean showHelpDialog() {
		if (!this.shouldShowHelpDialog) {
			return false;
		}

		Alert message = new Alert(AlertType.CONFIRMATION);
		message.setTitle("CS6910 - Better Pig");

		String helpMessage = "Pig rules: \n  Play against the computer.\n"
				+ "  Alternate taking turns, rolling the dice.\n"
				+ "  If the player does not roll a 1 on either die, the points are added for the turn.\n"
				+ "  If the player chooses to hold, their turn total "
				+ "  is added to their score and the turn is over.\n"
				+ "  You may set the goal score at the start of each game and\n    switch what "
				+ "strategy the computer uses at any time.";

		message.setHeaderText(helpMessage);
		message.setContentText("Would you like to see this dialog at the start of the next game?");

		ButtonType btnYes = new ButtonType("Yes");
		ButtonType btnNo = new ButtonType("No");
		message.getButtonTypes().setAll(btnYes, btnNo);

		Optional<ButtonType> result = message.showAndWait();

		return result.get() == btnYes;
	}

	/**
	 * Defines the panel in which the user selects which Player plays first.
	 */
	private final class NewGamePane extends GridPane {
		private RadioButton radHumanPlayer;
		private RadioButton radComputerPlayer;
		private RadioButton radRandomPlayer;
		private ComboBox<Integer> cmbGoalScore;

		private Game theGame;
		private Player theHuman;
		private Player theComputer;

		private NewGamePane(Game theGame) {
			if (theGame == null) {
				throw new IllegalArgumentException("Invalid game");
			}
			this.theGame = theGame;

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

			this.radHumanPlayer = new RadioButton(this.theHuman.getName() + " first");
			this.radHumanPlayer.setOnAction(new HumanFirstListener());

			this.radComputerPlayer = new RadioButton(this.theComputer.getName() + " first");
			this.radComputerPlayer.setOnAction(new ComputerFirstListener());

			this.radRandomPlayer = new RadioButton("Random Player");
			this.radRandomPlayer.setOnAction(new RandomFirstListener());

			ToggleGroup group = new ToggleGroup();
			this.radHumanPlayer.setToggleGroup(group);
			this.radComputerPlayer.setToggleGroup(group);
			this.radRandomPlayer.setToggleGroup(group);

			this.add(this.radHumanPlayer, 3, 0);
			this.add(this.radComputerPlayer, 4, 0);
			this.add(this.radRandomPlayer, 5, 0);
		}

		private void createGoalScoreItems() {
			Label lblGoalScore = new Label("Initial Goal Score: ");
			this.add(lblGoalScore, 0, 0);

			this.cmbGoalScore = new ComboBox<Integer>();
			this.cmbGoalScore.getItems().addAll(100, 50, 20);
			this.cmbGoalScore.setValue(100);
			this.cmbGoalScore.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int goalScore = NewGamePane.this.cmbGoalScore.getValue();
					PigPane.this.theGame.setGoalScore(goalScore);
					PigPane.this.pnGameInfo.update();
				}
			});
			this.add(this.cmbGoalScore, 1, 0);
		}

		/**
		 * Resets the radio buttons for new selection
		 */
		public void reset() {
			NewGamePane.this.theGame.setGoalScore(NewGamePane.this.cmbGoalScore.getValue());
			this.radHumanPlayer.setSelected(false);
			this.radComputerPlayer.setSelected(false);
			this.radRandomPlayer.setSelected(false);
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
				int goalScore = NewGamePane.this.cmbGoalScore.getValue();

				PigPane.this.pnChooseFirstPlayer.setDisable(true);

				if (Math.random() * 10 <= 4) {
					PigPane.this.pnComputerPlayer.setDisable(false);
					PigPane.this.theGame.startNewGame(NewGamePane.this.theComputer, goalScore);
				} else {
					PigPane.this.pnHumanPlayer.setDisable(false);
					PigPane.this.theGame.startNewGame(NewGamePane.this.theHuman, goalScore);
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
				int goalScore = NewGamePane.this.cmbGoalScore.getValue();

				PigPane.this.pnComputerPlayer.setDisable(false);
				PigPane.this.pnChooseFirstPlayer.setDisable(true);
				PigPane.this.theGame.startNewGame(NewGamePane.this.theComputer, goalScore);
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
				int goalScore = NewGamePane.this.cmbGoalScore.getValue();

				PigPane.this.pnChooseFirstPlayer.setDisable(true);
				PigPane.this.pnHumanPlayer.setDisable(false);
				PigPane.this.theGame.startNewGame(NewGamePane.this.theHuman, goalScore);
			}
		}
	}
}
