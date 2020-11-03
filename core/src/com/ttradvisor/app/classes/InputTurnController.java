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
	private boolean initialTurnDTSDrawn;
	private boolean initialTurnTCSDrawn;
	
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
			// for a future iteration
			// process with context of normal round turn
			if (thisTurn instanceof TrainCardAction) {
				return drawTC((TrainCardAction)thisTurn);
			}
			else if (thisTurn instanceof DestinationAction) {
				return drawDT((DestinationAction)thisTurn);
			}
			else if (thisTurn instanceof RouteAction) {
				return claimRoute((RouteAction)thisTurn);
			}
			else {
				Gdx.app.error("Turn", "No action selected for turn.");
				return false;
			}
		}
	}
	
	private boolean initialTurnDrawTC(TrainCardAction thisTurn) {
		if (thisTurn.getDrawnCards().size() != 4) {
			Gdx.app.error("Turn", "Must draw 4 train cards on initial turn.");
			return false;
		}
		thisTurn.actingPlayer.getTCS().addAll(thisTurn.getDrawnCards());
		initialTurnTCSDrawn = true;
		if (initialTurnDTSDrawn && initialTurnTCSDrawn) {
			isInitialTurnActive = false;
			return true;
		}
		return false;
	}
	
	private boolean initialTurnDrawDT(DestinationAction thisTurn) {
		if (thisTurn.getDrawnTickets().size() < 2 || thisTurn.getDrawnTickets().size() > 3) {
			Gdx.app.error("Turn", "Must draw between 2 and 3 destination tickets on initial turn.");
			return false;
		}
		thisTurn.actingPlayer.getDTS().addAll(thisTurn.getDrawnTickets());
		initialTurnDTSDrawn = true;
		if (initialTurnDTSDrawn && initialTurnTCSDrawn) {
			isInitialTurnActive = false;
			return true;
		}
		return false;
	}


	private boolean drawTC(TrainCardAction thisTurn) {
		if (thisTurn.getDrawnCards().size() > 2) {
			Gdx.app.error("Turn", "May not draw more than 2 train cards on a turn.");
			return false;
		}
		//This is incomplete, it needs to check if there are any train cards available.
		else if (thisTurn.getDrawnCards().size() == 1 && thisTurn.getDrawnCards().get(thisTurn.getDrawnCards().size() - 1).getColor() != new TrainCard(Colors.route.ANY).getColor()) {
			Gdx.app.error("Turn", "If one card is drawn it must be a card of Any color.");
			//Gdx.app.error("Turn", "If one card is drawn there are no other cards left.");
			return false;
		}
		else {
			thisTurn.actingPlayer.getTCS().addAll(thisTurn.getDrawnCards());
			return true;
		}
	}

	private boolean drawDT(DestinationAction thisTurn) {
		if (thisTurn.getDrawnTickets().size() >  3 && thisTurn.getDrawnTickets().size() < 0) {
			Gdx.app.error("Turn", "May not draw more than 3 tickets on a turn, and must keep atleast one drawn card.");
			return false;
		}
		else {
			thisTurn.actingPlayer.getDTS().addAll(thisTurn.getDrawnTickets());
			return true;
		}
	}

	private boolean claimRoute(RouteAction thisTurn) {	
		if (gameState.getClaimedRoutes().contains(thisTurn.claimedRoute)){
			Gdx.app.error("Turn", "Can not claim rout that has already been claimed.");
			return false;
		}
		else if(thisTurn.actingPlayer.getNumberOfUsable(thisTurn.claimedRoute.getColor()) <  thisTurn.claimedRoute.getCost()) {
			Gdx.app.error("Turn", "Must have enough train tickets to claim route.");
			return false;
		}
		else {
			int currentCost = thisTurn.claimedRoute.getCost();
			for (int i = 0; i  <  currentCost; i++) {
				if (thisTurn.actingPlayer.getNumberOfColor(thisTurn.claimedRoute.getColor()) >= currentCost){
					thisTurn.actingPlayer.removeTC(thisTurn.actingPlayer.getCardOfColor(thisTurn.claimedRoute.getColor()));
					currentCost--;
				}
				else {
					thisTurn.actingPlayer.removeTC(thisTurn.actingPlayer.getCardOfColor(Colors.route.ANY));
					currentCost--;
				}	
			}

//			thisTurn.actingPlayer.addClaimedRoutes(thisTurn.claimedRoute);
			gameState.addClaimedRoute(thisTurn.claimedRoute);

			return true;
		}
	}
}