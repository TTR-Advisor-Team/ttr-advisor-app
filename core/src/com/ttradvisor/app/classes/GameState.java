package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

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
	
	public Recommender getRecommender() {
		return rec;
	}
	public void addTurn(Turn newTurn) {
		turns.add(newTurn);
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
