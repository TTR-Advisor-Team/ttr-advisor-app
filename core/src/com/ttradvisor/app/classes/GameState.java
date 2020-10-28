package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class GameState {

	private ArrayList<Player> curPlayers;
	private Board curBoard;
	//private Recommender rec;
	private ArrayList<Turn> turns;
	
	public GameState(ArrayList<Player> players, Board board, ArrayList<Turn> turns) {
		curPlayers = players;
		curBoard = board;
		this.turns = turns;
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
}
