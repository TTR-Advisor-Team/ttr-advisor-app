package com.ttradvisor.app.classes;

public abstract class Action {
	
	public Player actingPlayer;
	
	public Action(Player actingPlayer) {
		this.actingPlayer = actingPlayer;
	}
	
	public void setPlayer(Player player) {
		actingPlayer = player;
	}

}
