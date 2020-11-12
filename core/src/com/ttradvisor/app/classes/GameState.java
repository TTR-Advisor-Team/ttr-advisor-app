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
//	private ArrayList<Route> claimedRoutes;
	
	public GameState(ArrayList<Player> players, Board board, DestinationTicketList dtList,
			ArrayList<Turn> turns) {
		curPlayers = players;
		curBoard = board;
		this.turns = turns;
		this.dtList = dtList;
		currentPlayer = null;
//		claimedRoutes = new ArrayList<Route>();

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
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

//	public void addClaimedRoute(Route claim) {
//		claimedRoutes.add(claim);
//	}
//	public  ArrayList<Route> getClaimedRoutes(){
//		return claimedRoutes;
//	}
//	public void setClaimedRoutes(ArrayList<Route> claimedRoutes){
//		this.claimedRoutes = claimedRoutes;
//	}
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
