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

	private FullPigPane contentPane;

	private StatusPane gameInfoPane;
	private NewGamePane chooseFirstPlayerPane;
	private HumanPane humanPlayerPane;
	private ComputerPane computerPlayerPane;

	private PigHelpDialog pigHelpDialog;

	/**
	 * Creates a MenuBar attached to the Pig Game and each of the Application's
	 * Panes.
	 * 
	 * @param theGame       - reference to Pig Game model
	 * @param contentPane   - reference to the Full Pig Content Pane
	 * @param pigHelpDialog - reference to PigHelpDialog
	 */
	public PigMenuBar(Game theGame, FullPigPane contentPane, PigHelpDialog pigHelpDialog) {

		if (theGame == null) {
			throw new IllegalArgumentException("Invalid Game");
		}
		this.theGame = theGame;
		
		if (contentPane == null) {
			throw new IllegalArgumentException("Invalid Full Pig Pane");
		}
		this.contentPane = contentPane;

		this.gameInfoPane = this.contentPane.getGameInfoPane();
		this.chooseFirstPlayerPane = this.contentPane.getChooseFirstPlayerPane();
		this.computerPlayerPane = this.contentPane.getComputerPlayerPane();
		this.humanPlayerPane = this.contentPane.getHumanPlayerPane();

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
		Menu gameMenu = new Menu("_Game");
		gameMenu.setMnemonicParsing(true);

		MenuItem newGameMenuItem = new MenuItem("_New");
		newGameMenuItem.setMnemonicParsing(true);
		newGameMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
		newGameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
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

		MenuItem exitMenuItem = new MenuItem("E_xit");
		exitMenuItem.setMnemonicParsing(true);
		exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
		exitMenuItem.setOnAction(event -> System.exit(0));

		gameMenu.getItems().addAll(newGameMenuItem, exitMenuItem);
		return gameMenu;
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
		Menu strategyMenu = new Menu("_Strategy");
		strategyMenu.setMnemonicParsing(true);

		ToggleGroup strategyToggleGroup = new ToggleGroup();

		RadioMenuItem cautiousStrategyMenuItem = this.addStrategyItem("_Cautious", KeyCode.C, new CautiousStrategy());
		cautiousStrategyMenuItem.setToggleGroup(strategyToggleGroup);

		RadioMenuItem greedyStrategyMenuItem = this.addStrategyItem("Gr_eedy", KeyCode.E, new GreedyStrategy());
		greedyStrategyMenuItem.setToggleGroup(strategyToggleGroup);

		RadioMenuItem randomStrategyMenuItem = this.addStrategyItem("_Random", KeyCode.R, new RandomStrategy());
		randomStrategyMenuItem.setToggleGroup(strategyToggleGroup);

		PigStrategy currentStrategy = this.theGame.getComputerPlayer().getStrategy();
		if (currentStrategy.getClass() == CautiousStrategy.class) {
			cautiousStrategyMenuItem.setSelected(true);
		} else if (currentStrategy.getClass() == RandomStrategy.class) {
			randomStrategyMenuItem.setSelected(true);
		} else {
			greedyStrategyMenuItem.setSelected(true);
		}

		strategyMenu.getItems().addAll(cautiousStrategyMenuItem, greedyStrategyMenuItem, randomStrategyMenuItem);
		return strategyMenu;
	}

	private MenuItem createHelpMenuItem(String menuItemText, KeyCode acceleratorKey,
			EventHandler<ActionEvent> eventHandler) {

		MenuItem helpMenuItem = new MenuItem(menuItemText);

		helpMenuItem.setMnemonicParsing(true);

		helpMenuItem.setAccelerator(new KeyCodeCombination(acceleratorKey, KeyCombination.SHORTCUT_DOWN));

		helpMenuItem.setOnAction(eventHandler);

		return helpMenuItem;
	}

	private Menu createHelpMenu() {
		Menu helpMenu = new Menu("_Help");
		helpMenu.setMnemonicParsing(true);

		MenuItem helpContentsMenuItem = this.createHelpMenuItem("Help _Contents", KeyCode.C, (event -> {
			PigMenuBar.this.pigHelpDialog.setShouldShowHelpDialog(true);
			PigMenuBar.this.pigHelpDialog.setShouldShowHelpDialog(PigMenuBar.this.pigHelpDialog.showHelpDialog());
		}));

		MenuItem aboutMenuItem = this.createHelpMenuItem("_About", KeyCode.A, (event -> {
			new PigAboutDialog().showAndWait();
		}));

		helpMenu.getItems().addAll(helpContentsMenuItem, aboutMenuItem);
		return helpMenu;
	}
}
