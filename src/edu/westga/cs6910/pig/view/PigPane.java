package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import edu.westga.cs6910.pig.model.strategies.CautiousStrategy;
import edu.westga.cs6910.pig.model.strategies.GreedyStrategy;
import edu.westga.cs6910.pig.model.strategies.PigStrategy;
import edu.westga.cs6910.pig.model.strategies.RandomStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
	private BorderPane pnContent;
	private HumanPane pnHumanPlayer;
	private ComputerPane pnComputerPlayer;
	private StatusPane pnGameInfo;
	private NewGamePane pnChooseFirstPlayer;

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

		this.pnContent = new BorderPane();

		this.createMenu();

		this.pnHumanPlayer = new HumanPane(theGame);
		HBox leftBox = this.createHBoxHolder(this.pnHumanPlayer, true);
		this.pnContent.setLeft(leftBox);

		this.pnComputerPlayer = new ComputerPane(theGame);
		HBox centerBox = this.createHBoxHolder(this.pnComputerPlayer, true);
		this.pnContent.setCenter(centerBox);

		this.pnGameInfo = new StatusPane(theGame);
		HBox bottomBox = this.createHBoxHolder(this.pnGameInfo, false);
		this.pnContent.setBottom(bottomBox);
		
		this.pnChooseFirstPlayer = new NewGamePane(theGame, this.pnGameInfo, this.pnComputerPlayer, this.pnHumanPlayer);
		HBox topBox = this.createHBoxHolder(this.pnChooseFirstPlayer, false);
		this.pnContent.setTop(topBox);

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

		Menu helpMenu = this.createHelpMenu();

		mnuMain.getMenus().addAll(mnuFile, mnuSettings, helpMenu);
		vbxMenuHolder.getChildren().addAll(mnuMain);
		this.setTop(vbxMenuHolder);
	}

	private RadioMenuItem addStrategyItem(String radioMenuItemText, KeyCode acceleratorKey, PigStrategy selectedStrategy) {
		RadioMenuItem strategyItem = new RadioMenuItem(radioMenuItemText);
		strategyItem.setAccelerator(new KeyCodeCombination(acceleratorKey, KeyCombination.SHORTCUT_DOWN));
		strategyItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PigPane.this.theGame.getComputerPlayer().setStrategy(selectedStrategy);
			}
		});
		strategyItem.setMnemonicParsing(true);
		
		return strategyItem;
	}
	
	private Menu createStrategyMenu() {
		Menu mnuSettings = new Menu("_Strategy");
		mnuSettings.setMnemonicParsing(true);

		ToggleGroup tglStrategy = new ToggleGroup();
		
		RadioMenuItem mnuCautious = this.addStrategyItem("_Cautious", KeyCode.C, new CautiousStrategy());
		mnuCautious.setToggleGroup(tglStrategy);

		RadioMenuItem mnuGreedy = this.addStrategyItem("Gr_eedy", KeyCode.E, new GreedyStrategy());
		mnuGreedy.setToggleGroup(tglStrategy);

		RadioMenuItem mnuRandom = this.addStrategyItem("_Random", KeyCode.R, new RandomStrategy());
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

				if (PigPane.this.pigHelpDialog.getShouldShowHelpDialog()) {
					PigPane.this.pigHelpDialog.setShouldShowHelpDialog(PigPane.this.pigHelpDialog.showHelpDialog());
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

	private Menu createHelpMenu() {
		Menu helpMenu = new Menu("_Help");
		helpMenu.setMnemonicParsing(true);

		MenuItem helpContentsMenuItem = new MenuItem("Help _Contents");
		helpContentsMenuItem.setMnemonicParsing(true);
		helpContentsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
		helpContentsMenuItem.setOnAction(event -> {
			PigPane.this.pigHelpDialog.setShouldShowHelpDialog(true);
			PigPane.this.pigHelpDialog.setShouldShowHelpDialog(PigPane.this.pigHelpDialog.showHelpDialog());
		});

		MenuItem aboutMenuItem = new MenuItem("_About");
		aboutMenuItem.setMnemonicParsing(true);
		aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
		aboutMenuItem.setOnAction(event -> {
			new PigAboutDialog().showAndWait();
		});

		helpMenu.getItems().addAll(helpContentsMenuItem, aboutMenuItem);
		return helpMenu;
	}
}
