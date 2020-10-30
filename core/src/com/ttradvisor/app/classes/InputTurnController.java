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
	private int initialTurnTCDrawn;
	private int initialTurnDTDrawn;
	
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
		initialTurnTCDrawn = 0;
		initialTurnDTDrawn = 0;
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
	 * @return true if the turn is finished and we can move on to the next player's turn
	 */
	public boolean takeAction(Action thisTurn) {
		if (isInitialTurnActive) {
			if (thisTurn instanceof TrainCardAction) {
				return initialTurnDrawTC((TrainCardAction)thisTurn);
			}
			else if (thisTurn instanceof DestinationAction) {
				return initialTurnDrawDT((DestinationAction)thisTurn);
			}
			else {
				Gdx.app.error("Turn", "May only draw destination tickets or train cards on initial turn.");
				return false;
			}
		}
		else {
			return false;
			// for a future iteration
			// process with context of normal round turn
		}
	}
	
	private boolean initialTurnDrawTC(TrainCardAction thisTurn) {
		if (thisTurn.getDrawnCards().size() + initialTurnTCDrawn > 4) {
			Gdx.app.error("Turn", "May not draw more than 4 train cards on initial turn.");
			return false;
		}
		thisTurn.actingPlayer.getTCS().addAll(thisTurn.getDrawnCards());
		initialTurnTCDrawn += thisTurn.getDrawnCards().size();
		if (initialTurnTCDrawn == 4 && (initialTurnDTDrawn == 2 || initialTurnDTDrawn == 3)) {
			// done drawing cards for the initial turn
			isInitialTurnActive = false;
			return true;
		}
		return false;
	}
	
	private boolean initialTurnDrawDT(DestinationAction thisTurn) {
		if (thisTurn.getDrawnTickets().size() + initialTurnDTDrawn > 3) {
			Gdx.app.error("Turn", "May not draw more than 3 tickets on initial turn.");
			return false;
		}
		thisTurn.actingPlayer.getDTS().addAll(thisTurn.getDrawnTickets());
		initialTurnDTDrawn += thisTurn.getDrawnTickets().size();
		if (initialTurnTCDrawn == 4 && (initialTurnDTDrawn == 2 || initialTurnDTDrawn == 3)) {
			// done drawing cards for the initial turn
			isInitialTurnActive = false;
			return true;
		}
		return false;
	}

}
