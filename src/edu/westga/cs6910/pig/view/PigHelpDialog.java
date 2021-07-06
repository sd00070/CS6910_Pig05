package edu.westga.cs6910.pig.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Contains all of the information to display the help dialog and interact with
 * the user.
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class PigHelpDialog {

	private boolean shouldShowHelpDialog;

	/**
	 * Creates a new Help Dialog for the Pig Game Application.
	 */
	public PigHelpDialog() {
		this.shouldShowHelpDialog = true;
		this.shouldShowHelpDialog = this.showHelpDialog();
	}

	/**
	 * Obtains the boolean flag indicating whether or not to display the help
	 * dialog.
	 * 
	 * @return whether or not to show the help dialog
	 */
	public boolean getShouldShowHelpDialog() {
		return this.shouldShowHelpDialog;
	}

	/**
	 * Sets whether or not to show the help dialog
	 * 
	 * @param shouldShowFlag - the new flag to set the value to
	 */
	public void setShouldShowHelpDialog(boolean shouldShowFlag) {
		this.shouldShowHelpDialog = shouldShowFlag;
	}

	/**
	 * Displays an Alert with the Rules and other Info about the Application.
	 * 
	 * @return if the user clicked "yes": true, "no": false
	 */
	public boolean showHelpDialog() {
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
}
