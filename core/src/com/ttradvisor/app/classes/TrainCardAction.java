package com.ttradvisor.app.classes;

import java.util.ArrayList;

public class TrainCardAction extends Action {
	private ArrayList<TrainCard> drawnCards;
	
	public TrainCardAction(ArrayList<TrainCard> drawnCards) {
		this.setDrawnCards(drawnCards);
	}
	
	public ArrayList<TrainCard> getDrawnCards() {
		return drawnCards;
	}
	public void setDrawnCards(ArrayList<TrainCard> drawnCards) {
		this.drawnCards = drawnCards;
	}
}