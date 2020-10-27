package com.ttradvisor.app.classes;

/**
 *	Contains logic and validation for inputting new turns. 
 *	Determines whether the new turn created is valid and updates the View
 */
public class InputTurnController {
	
	private boolean isInitialTurnActive;
	
	/**
	 * Manage and validate the input of the initial turn
	 * (drawing 4 Train Cards and 2-3 Destination Tickets)
	 */
	public void startInitialTurn() {
		isInitialTurnActive = false;
	}
	
	/**
	 * Process an Action object as the next turn.
	 * @param thisTurn the Action taken for the current player's turn
	 */
	public void takeAction(Action thisTurn) {
		if (isInitialTurnActive) {
			// process with context of initial turn
			
		}
		else {
			// process with context of normal round turn
		}
	}

}
