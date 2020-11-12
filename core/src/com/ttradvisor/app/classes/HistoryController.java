package com.ttradvisor.app.classes;

public class HistoryController {
	
	private GameState gameState;
	private int turnIndex;
	
	public HistoryController(GameState gameState) {
		this.gameState = gameState;
		this.turnIndex = gameState.getCurrentTurnCounter()-1;
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
		if (turnIndex >= gameState.getCurrentTurnCounter()-1) {
			return false;
		}
		turnIndex++;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		gameState.setPlayers(gameState.getTurns().get(turnIndex).getPlayerSnapshots());
		return true;
	}
	
	public boolean goToTurn(int turnIndex) {
		if (turnIndex > gameState.getCurrentTurnCounter()-1 || turnIndex < 0) {
			return false;
		}
		this.turnIndex = turnIndex;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		gameState.setPlayers(gameState.getTurns().get(turnIndex).getPlayerSnapshots());
		return true;
	}
	
	public boolean validateCorrection(Action replacementAction){
		return false;
		
	}
	
	public void makeCorrection(Action replacementAction) {
		
	}

}
