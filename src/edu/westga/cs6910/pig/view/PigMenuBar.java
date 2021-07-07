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

/**
 * Contains all code used to display and interact with the Menus at the top of
 * the application.
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class PigMenuBar extends MenuBar {

	private Game theGame;

	private StatusPane gameInfoPane;
	private NewGamePane chooseFirstPlayerPane;
	private HumanPane humanPlayerPane;
	private ComputerPane computerPlayerPane;

	private PigHelpDialog pigHelpDialog;

	/**
	 * Creates a MenuBar attached to the Pig Game and each of the Application's
	 * Panes.
	 * 
	 * @param theGame               - reference to Pig Game model
	 * @param gameInfoPane          - reference to Game Info Pane
	 * @param chooseFirstPlayerPane - reference to New Game Pane
	 * @param humanPlayerPane       - reference to Human Pane
	 * @param computerPlayerPane    - reference to Computer Pane
	 * @param pigHelpDialog         - reference to PigHelpDialog
	 */
	public PigMenuBar(Game theGame, StatusPane gameInfoPane, NewGamePane chooseFirstPlayerPane,
			HumanPane humanPlayerPane, ComputerPane computerPlayerPane, PigHelpDialog pigHelpDialog) {

		if (theGame == null) {
			throw new IllegalArgumentException("Invalid Game");
		}
		this.theGame = theGame;

		if (gameInfoPane == null) {
			throw new IllegalArgumentException("Invalid Game Info Pane");
		}
		this.gameInfoPane = gameInfoPane;

		if (chooseFirstPlayerPane == null) {
			throw new IllegalArgumentException("Invalid New Game Pane");
		}
		this.chooseFirstPlayerPane = chooseFirstPlayerPane;

		if (humanPlayerPane == null) {
			throw new IllegalArgumentException("Invalid Human Pane");
		}
		this.humanPlayerPane = humanPlayerPane;

		if (computerPlayerPane == null) {
			throw new IllegalArgumentException("Invalid Computer Pane");
		}
		this.computerPlayerPane = computerPlayerPane;

		if (pigHelpDialog == null) {
			throw new IllegalArgumentException("Invalid Help Dialog");
		}
		this.pigHelpDialog = pigHelpDialog;

		Menu gameMenu = this.createGameMenu();
		Menu strategyMenu = this.createStrategyMenu();
		Menu helpMenu = this.createHelpMenu();

		this.getMenus().addAll(gameMenu, strategyMenu, helpMenu);
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
				PigMenuBar.this.chooseFirstPlayerPane.reset();
				PigMenuBar.this.chooseFirstPlayerPane.setDisable(false);
				PigMenuBar.this.humanPlayerPane.setDisable(true);
				PigMenuBar.this.computerPlayerPane.setDisable(true);

				if (PigMenuBar.this.pigHelpDialog.getShouldShowHelpDialog()) {
					PigMenuBar.this.pigHelpDialog
							.setShouldShowHelpDialog(PigMenuBar.this.pigHelpDialog.showHelpDialog());
				}

				PigMenuBar.this.theGame.resetGame();
				PigMenuBar.this.gameInfoPane.clearInformation();
				PigMenuBar.this.humanPlayerPane.clearInformation();
				PigMenuBar.this.computerPlayerPane.clearInformation();
			}
		});

		MenuItem mnuExit = new MenuItem("E_xit");
		mnuExit.setMnemonicParsing(true);
		mnuExit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
		mnuExit.setOnAction(event -> System.exit(0));

		mnuGame.getItems().addAll(mnuNew, mnuExit);
		return mnuGame;
	}

	private RadioMenuItem addStrategyItem(String radioMenuItemText, KeyCode acceleratorKey,
			PigStrategy selectedStrategy) {
		RadioMenuItem strategyItem = new RadioMenuItem(radioMenuItemText);
		strategyItem.setAccelerator(new KeyCodeCombination(acceleratorKey, KeyCombination.SHORTCUT_DOWN));
		strategyItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PigMenuBar.this.theGame.getComputerPlayer().setStrategy(selectedStrategy);
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

	private Menu createHelpMenu() {
		Menu helpMenu = new Menu("_Help");
		helpMenu.setMnemonicParsing(true);

		MenuItem helpContentsMenuItem = new MenuItem("Help _Contents");
		helpContentsMenuItem.setMnemonicParsing(true);
		helpContentsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
		helpContentsMenuItem.setOnAction(event -> {
			PigMenuBar.this.pigHelpDialog.setShouldShowHelpDialog(true);
			PigMenuBar.this.pigHelpDialog.setShouldShowHelpDialog(PigMenuBar.this.pigHelpDialog.showHelpDialog());
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
