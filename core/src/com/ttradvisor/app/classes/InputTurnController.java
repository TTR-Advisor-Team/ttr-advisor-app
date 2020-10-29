package com.ttradvisor.app.classes;

import com.badlogic.gdx.Gdx;
import com.ttradvisor.app.TTRAdvisorApp;

/**
 *	Contains logic and validation for inputting new turns. 
 *	Determines whether the new turn created is valid and updates the View.
 *	The essential method is takeAction(). Depending on Controller and Game states,
 *	applies or refuses to apply Action objects.
 */
public class InputTurnController {
	
	private GameState gameState; // the GameState paired with this controller
	
	// context for interpreting Actions
	private boolean isInitialTurnActive;
	
	/**
	 * Init a controller linked to the given game state object.
	 * 
	 * @param gameState
	 */
	public InputTurnController(GameState gameState) {
		this.gameState = gameState;
	}
	
	/**
	 * Manage and validate the input of the initial turn
	 * (drawing 4 Train Cards and 2-3 Destination Tickets)
	 * 
	 * Call this method to inform the controller that the next incoming
	 * Actions are to be interpreted in the context of the initial turn.
	 */
	public void startInitialTurn() {
		isInitialTurnActive = true;
	}
	
	/**
	 * @return true only if the initial turn is ongoing
	 */
	public boolean isInitialTurn() {
		return isInitialTurnActive;
	}
	
	/**
	 * Process an Action object as the next turn.
	 * @param thisTurn the Action taken for the current player's turn
	 */
	public void takeAction(Action thisTurn) {
		if (isInitialTurnActive) {
			if (thisTurn instanceof TrainCardAction) {
				initialTurnDrawTC((TrainCardAction)thisTurn);
			}
			else if (thisTurn instanceof DestinationAction) {
				initialTurnDrawDT((DestinationAction)thisTurn);
			}
			else {
				Gdx.app.error("Turn", "May only draw destination tickets or train cards on initial turn.");
			}
		}
		else {
			// for a future iteration
			// process with context of normal round turn
		}
	}
	
	private void initialTurnDrawTC(TrainCardAction thisTurn) {
		// checks:
		// # of cards must be valid (2-4)?
		// player must exist in the game state
		// 
		thisTurn.actingPlayer.getTCS().addAll(thisTurn.getDrawnCards());
	}
	
	private void initialTurnDrawDT(DestinationAction thisTurn) {
		thisTurn.actingPlayer.getDTS().addAll(thisTurn.getDrawnTickets());
	}

}
