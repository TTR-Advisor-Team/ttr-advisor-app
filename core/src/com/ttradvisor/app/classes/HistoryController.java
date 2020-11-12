package com.ttradvisor.app.classes;

public class HistoryController {
	
	private GameState gameState;
	private int turnIndex;
	
	public HistoryController(GameState gameState) {
		this.gameState = gameState;
		this.turnIndex = gameState.getCurrentTurnCounter()-1;
	}
	
	private boolean previousTurn() {
		if (turnIndex <= 0) {
			return false;
		}
		turnIndex--;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		return true;
	}
	
	private boolean nextTurn() {
		if (turnIndex >= gameState.getCurrentTurnCounter()-1) {
			return false;
		}
		turnIndex++;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		return true;
	}
	
	private boolean goToTurn(int turnIndex) {
		if (turnIndex > gameState.getCurrentTurnCounter()-1 || turnIndex < 0) {
			return false;
		}
		this.turnIndex = turnIndex;
		gameState.setBoard(gameState.getTurns().get(turnIndex).getSnapshot());
		return true;
	}
	
	private boolean validateCorrection(Action replacementAction){
		return false;
		
	}
	
	private void makeCorrection(Action replacementAction) {
		
	}

}
