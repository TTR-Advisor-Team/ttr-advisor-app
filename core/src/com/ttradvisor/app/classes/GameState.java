package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.ttradvisor.app.classes.Board.Route;

public class GameState {

	private ArrayList<Player> curPlayers;
	private Board curBoard;
	private Recommender rec;
	private DestinationTicketList dtList;
	private ArrayList<Turn> turns;
	public Player currentPlayer;
	private String errorMessage;

	public Colors.player userColor;
//	private ArrayList<Route> claimedRoutes;
	
	public GameState(Colors.player userColor, ArrayList<Player> players, Board board, DestinationTicketList dtList,
			ArrayList<Turn> turns) {
		this.userColor = userColor;
		curPlayers = players;
		curBoard = board;
		this.turns = turns;
		this.dtList = dtList;
		currentPlayer = null;
		errorMessage  = "";
//		claimedRoutes = new ArrayList<Route>();

	}
	
	public Colors.player getUserColor(){
		return userColor;
	}
	
	public Player getUserPlayer() {
		for (Player p : curPlayers) {
			if (p.getColor() == userColor) {
				return p;
			}
		}
		// shouldn't happen, but avoid a null pointer anyway
		Gdx.app.error("GameState", "Could not look up the user player.");
		return curPlayers.get(0);
	}
	
	public Recommender getRecommender() {
		return rec;
	}
	public void addTurn(Turn newTurn) {
		turns.add(newTurn);
	}
	
	public void removePrevTurn() {
		int index = curPlayers.indexOf(currentPlayer);
		turns.remove(getCurrentTurnCounter()-1);
		ArrayList<Player> deepCopyPlayers = new ArrayList<Player>();
		for (Player p : turns.get(getCurrentTurnCounter()-1).getPlayerSnapshots()) {
			deepCopyPlayers.add(p.getDeepCopy());
		}
		setPlayers(deepCopyPlayers);
		setBoard(turns.get(getCurrentTurnCounter()-1).getSnapshot().snapshotBoard());
		currentPlayer = curPlayers.get(index);
	}
	
	public List<Player> getPlayers() {
		return curPlayers;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		curPlayers = players;
	}
	
	public Board getBoard() {
		return curBoard;
	}
	
	public void setBoard(Board board) {
		curBoard = board;
	}
	public List<Turn> getTurns(){
		return turns;
	}
	
	public void setError(String error) {
		this.errorMessage = error;
	}
	public String getError() {
		return errorMessage;
	}

	public DestinationTicketList getDtList() {
		return dtList;
	}
	
	/**
	 * @return the current turn (not the current round)
	 */
	public int getCurrentTurnCounter() {
		return turns.size();
	}
	
	/**
	 * @return the current round (not the current turn)
	 */
	public int getCurrentRoundCounter() {
		return turns.size() / curPlayers.size() + 1;
	}
}
