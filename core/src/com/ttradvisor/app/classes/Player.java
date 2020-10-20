package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

import com.ttradvisor.app.Colors;

public class Player {

	private ArrayList<DestinationTicket> destTicketHand;
	private ArrayList<TrainCard> trainCardHand;
	private Colors color;
	private int score;
	private int numTrains;
	
	public Player(ArrayList<DestinationTicket> dts, ArrayList<TrainCard> hand,
			Colors color, int score, int numTrains) {
		destTicketHand = dts;
		trainCardHand = hand;
		this.color = color;
		this.score = score;
		this.numTrains = numTrains;
	}
	
	public void addDT(DestinationTicket addedTicket) {
		destTicketHand.add(addedTicket);
	}
	
	public void removeDT(DestinationTicket removedTicket) {
		if(destTicketHand.contains(removedTicket))
			destTicketHand.remove(removedTicket);
	}
	
	public void addTC(TrainCard addedCard) {
		trainCardHand.add(addedCard);
	}
	
	public void removeTC(TrainCard removedCard) {
		if(trainCardHand.contains(removedCard))
			trainCardHand.remove(removedCard);
	}
	
	public List<DestinationTicket> getDTS(){
		return destTicketHand;
	}
	public void setDTS(ArrayList<DestinationTicket> dts) {
		destTicketHand = dts;
	}
	public List<TrainCard> getTCS(){
		return trainCardHand;
	}
	public void setTCS(ArrayList<TrainCard> hand) {
		trainCardHand = hand;
	}
	public Colors getColor(){
		return color;
	}
	public void setColor(Colors color) {
		this.color = color;
	}
	public int getScore(){
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getNumTrains(){
		return numTrains;
	}
	public void setNumTrains(int numTrains) {
		this.numTrains = numTrains;
	}
}
