package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.ComputerPlayer;
import edu.westga.cs6910.pig.model.Game;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Defines the pane that lets the user either roll or hold during
 * 	their turn
 * This class was started by CS6910
 * 
 * @author Spencer Dent
 * @version 2021-07-06
 */
public class ComputerPane extends GridPane implements InvalidationListener {
	private Game theGame;
	private Label lblTurnTotal;
	private Label lblDiceValues;
	private ComputerPlayer theComputer;
	private Button btnTakeTurn;

	/**
	 * Creates a new ComputerPane that observes the specified game. 
	 * 
	 * @param theGame	the model object from which this pane gets its data
	 * 
	 * @requires 	theGame != null
	 */
	public ComputerPane(Game theGame) {
		if (theGame == null) {
			throw new IllegalArgumentException("Invalid game");
		}
		this.theGame = theGame;

		this.theGame.addListener(this);
		
		this.theComputer = this.theGame.getComputerPlayer();
		
		this.buildPane();
	}
	
	private void buildPane() {
		Label lblTitle = new Label("~~ " + this.theComputer.getName() + " ~~");
		this.add(lblTitle, 0, 0);

		this.btnTakeTurn = new Button("Take Turn");
		this.btnTakeTurn.setOnAction(new TakeTurnListener());
		this.add(this.btnTakeTurn, 0, 1);
		
		Pane verticalGap = new Pane();
	    verticalGap.minHeightProperty().bind(lblTitle.heightProperty());
	    this.add(verticalGap, 0, 2);

		this.add(new Label("Turn Total: "), 0, 3);
		this.lblTurnTotal = new Label("0");
		this.add(this.lblTurnTotal, 1, 3);

		Label lblDiceValuesHeader = new Label("Dice Values");
		this.add(lblDiceValuesHeader, 0, 4);
		GridPane.setValignment(lblDiceValuesHeader, VPos.TOP);
		this.lblDiceValues = new Label("");
		this.add(this.lblDiceValues, 1, 4);
	}

	@Override
	public void invalidated(Observable theObservable) {
		boolean myTurn = this.theGame.getCurrentPlayer() == this.theComputer;
		
		if (!myTurn) {
			this.updateTotalLabels();
		} 

		if (myTurn) {
			this.setDisable(false);
		} else {
			this.setDisable(true);
		}
		
		this.setDisable(!myTurn);

		if (this.theGame.isGameOver()) {
			this.setDisable(true);
			return;
		}
	}

	private void updateTotalLabels() {
		int turnTotal = this.theComputer.getTurnTotal();
		if (this.theComputer.getRolledOne()) {
			turnTotal = 0;
		}
		this.lblTurnTotal.setText("" + turnTotal);
		
		String result = "";
		for (String current : this.theComputer.getRollsInTurn()) {
			result += current + "\n";
		}
		this.lblDiceValues.setText(result);
	}

	/**
	 * Updates the output labels in preparation for a new game
	 */
	public void clearInformation() {
		this.lblTurnTotal.setText("0");
		this.lblDiceValues.setText("");
	}	
	
	/** 
	 * Defines the listener for takeTurnButton.
	 */
	private class TakeTurnListener implements EventHandler<ActionEvent> {

		/** 
		 * Tells the Game to have its current player (i.e., the computer player)
		 * take its turn.	
		 * 
		 * @see javafx.event.EventHandler handle(T-extends-javafx.event.Event)
		 */
		@Override
		public void handle(ActionEvent arg0) {
			if (!ComputerPane.this.theGame.isGameOver()) {
				ComputerPane.this.theGame.play();
			}
		}
	}
}
