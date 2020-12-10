package com.ttradvisor.app.classes;

import java.util.ArrayList;

public class TrainCardAction extends Action {
	private ArrayList<TrainCard> drawnCards;
	
	public TrainCardAction(Player actingPlayer, ArrayList<TrainCard> drawnCards) {
		super(actingPlayer);
		this.setDrawnCards(drawnCards);
	}
	
	public ArrayList<TrainCard> getDrawnCards() {
		return drawnCards;
	}
	public void setDrawnCards(ArrayList<TrainCard> drawnCards) {
		this.drawnCards = drawnCards;
	}
}