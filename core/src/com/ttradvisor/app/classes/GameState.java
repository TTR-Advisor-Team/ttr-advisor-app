package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

import com.ttradvisor.app.classes.Board.Route;

public class GameState {

	private ArrayList<Player> curPlayers;
	private Board curBoard;
	//private Recommender rec;
	//private DestinationTicketList dtList;
	private ArrayList<Turn> turns;
	private ArrayList<Route> claimedRoutes;
	
	public GameState(ArrayList<Player> players, Board board, ArrayList<Turn> turns) {
		curPlayers = players;
		curBoard = board;
		this.turns = turns;
		//this.dtList = dtList;
		claimedRoutes = new ArrayList<Route>();
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

	public void addClaimedRoute(Route claim) {
		claimedRoutes.add(claim);
	}
	public  ArrayList<Route> getClaimedRoutes(){
		return claimedRoutes;
	}
	public void setClaimedRoutes(ArrayList<Route> claimedRoutes){
		this.claimedRoutes = claimedRoutes;
	}
//	public DestinationTicketList getDtList() {
//		return dtList;
//	}
}
