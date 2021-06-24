package edu.westga.cs6910.pig.view;

import edu.westga.cs6910.pig.model.Game;
import edu.westga.cs6910.pig.model.HumanPlayer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Defines the panel that lets the user indicate whether they want to 
 * 	roll or hold on their turn
 * 
 * @author CS6910
 * @version Summer 2021
 */
public class HumanPane extends GridPane implements InvalidationListener {
	private Button btnRoll;
	private Button btnHold;
	private Label lblTurnTotal;
	private Label lblDiceValues;
	
	private HumanPlayer theHuman;
	private Game theGame;

	/**
	 * Creates a new HumanPane that observes the specified game. 
	 * 
	 * @param theGame	the model object from which this pane gets its data
	 * 
	 * @requires 	theGame != null
	 */
	public HumanPane(Game theGame) {
		if (theGame == null) {
			throw new IllegalArgumentException("Invalid game");
		}
		this.theGame = theGame;
		
		this.theGame.addListener(this);
		
		this.theHuman = this.theGame.getHumanPlayer();
		
		this.buildPane();
	}
	
	private void buildPane() {
		Label lblTitle = new Label("~~ " + this.theHuman.getName() + " ~~");
		this.add(lblTitle, 0, 0);
		
		this.add(new Label("Turn Total: "), 0, 1);
		this.lblTurnTotal = new Label("0");
		this.add(this.lblTurnTotal, 1, 1);
	
		this.add(new Label("Dice Values: "), 0, 2);
		this.lblDiceValues = new Label("");
		this.add(this.lblDiceValues, 1, 2);
		
		Pane verticalGap = new Pane();
	    verticalGap.minHeightProperty().bind(lblTitle.heightProperty());
	    this.add(verticalGap, 0, 3);
		
		this.btnRoll = new Button("Roll");
		this.btnRoll.setOnAction(new TakeTurnListener());
		this.add(this.btnRoll, 0, 4);
		
		this.btnHold = new Button("Hold");
		this.btnHold.setOnAction(new HoldListener());
		this.add(this.btnHold, 1, 4);
	}

	@Override
	public void invalidated(Observable observable) {
		boolean myTurn = this.theGame.getCurrentPlayer() == this.theHuman;

		int turnTotal = this.theHuman.getTurnTotal();
		String result = this.theHuman.getDiceValues();
		this.lblDiceValues.setText(result);
		this.lblTurnTotal.setText("" + turnTotal);
		
		this.setDisable(!myTurn);
		
		if (this.theGame.isGameOver()) {
			this.setDisable(true);
			return;
		}
	}
	
	/**
	 * Updates the output labels in preparation for a new game
	 */
	public void clearInformation() {
		this.lblDiceValues.setText("");
		this.lblTurnTotal.setText(Integer.toString(this.theHuman.getTurnTotal()));
	}	
	
	private class TakeTurnListener implements EventHandler<ActionEvent> {
		/** 
		 * Tells the Game to have its current player (i.e., the human Player)
		 * take its turn.	
		 * 
		 */
		@Override
		public void handle(ActionEvent event) {
			if (!HumanPane.this.theGame.isGameOver()) {
				HumanPane.this.theGame.play();
			}
		}
	}
	
	private class HoldListener implements EventHandler<ActionEvent> {
		/** 
		 * Tells the Game that its current player (i.e., the human Player)
		 * will be holding	
		 * 
		 */
		@Override
		public void handle(ActionEvent event) {
			if (!HumanPane.this.theGame.isGameOver()) {
				HumanPane.this.theGame.hold();
			}
		}
	}
}
