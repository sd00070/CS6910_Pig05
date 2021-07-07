package edu.westga.cs6910.pig.view;

import javafx.scene.control.Alert;

/**
 * Is a modified Information Alert, displaying the program's author and date of
 * creation.
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class PigAboutDialog extends Alert {

	/**
	 * Creates a new Alert with the Author's name, and the date the program was
	 * created.
	 */
	public PigAboutDialog() {
		super(AlertType.INFORMATION);

		this.setTitle("CS6910 - Better Pig");

		String author = "Spencer Dent";

		String creationDate = "July 6, 2021";

		this.setHeaderText("Author: " + author + "\nCreated: " + creationDate);
	}
}
