package com.ttradvisor.app.classes;

import java.util.ArrayList;

public class Turn {
	private Board snapshot;
	private Action action;
	private ArrayList<Player> playerSnapshots;
	
	public Turn(Board snapshot, Action action, ArrayList<Player> playerSnapshots) {
		this.setSnapshot(snapshot);
		this.setAction(action);
		this.setPlayerSnapshots(playerSnapshots);
	}

	public Board getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Board snapshot) {
		this.snapshot = snapshot;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public ArrayList<Player> getPlayerSnapshots() {
		return playerSnapshots;
	}

	public void setPlayerSnapshots(ArrayList<Player> playerSnapshots) {
		this.playerSnapshots = playerSnapshots;
	}
	

}
