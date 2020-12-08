package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class HistoryController {
	
	private GameState initialGameState;
	private GameState gameState;
	private int turnIndex;
	
	public HistoryController(GameState gameState) {
		this.initialGameState = new GameState(gameState.getUserColor(), (ArrayList<Player>) gameState.getPlayers(), gameState.getBoard(), gameState.getDtList(),(ArrayList<Turn>) gameState.getTurns());
		this.gameState = new GameState(gameState.getUserColor(), (ArrayList<Player>) gameState.getPlayers(), gameState.getBoard(), gameState.getDtList(),(ArrayList<Turn>) gameState.getTurns());
		this.turnIndex = gameState.getCurrentTurnCounter();
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public int getTurnIndex() {
		return turnIndex;
	}
	
	public int getTurnIndexView() {
		if(turnIndex == 0) {
			return turnIndex;
		}
		else {
			return (turnIndex-1)%(gameState.getPlayers().size());
		}
	}
	
	public void setTurnIndex(int turnIndex) {
		this.turnIndex = turnIndex;
	}
	
	public boolean previousTurn() {
		if (turnIndex <= 0) {
			return false;
		}
		turnIndex--;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		gameState.setPlayers(gameState.getTurns().get(turnIndex).getPlayerSnapshots());
		return true;
	}
	
	public boolean nextTurn() {
		if (turnIndex >= gameState.getCurrentTurnCounter()) {
			return false;
		}
		if(turnIndex == gameState.getCurrentTurnCounter()-1) {
			turnIndex++;
			gameState = initialGameState;
			return true;
		}
		turnIndex++;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		gameState.setPlayers(gameState.getTurns().get(turnIndex).getPlayerSnapshots());
		return true;
	}
	
	public boolean goToTurn(int turnIndex) {
		if (turnIndex > gameState.getCurrentTurnCounter() || turnIndex < 0) {
			return false;
		}
		else if(turnIndex >= gameState.getCurrentTurnCounter()-1) {
			this.turnIndex = turnIndex;
			gameState = initialGameState;
			return true;
		}
		this.turnIndex = turnIndex;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		gameState.setPlayers(gameState.getTurns().get(turnIndex).getPlayerSnapshots());
		return true;
	}
	
	//public boolean validateCorrection(Action replacementAction){
		//return false;
		
	//}
	
	//public void makeCorrection(Action replacementAction) {
		
	//}

}
