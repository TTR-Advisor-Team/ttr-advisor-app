package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private ArrayList<DestinationTicket> destTicketHand;
	private ArrayList<TrainCard> trainCardHand;
	private Colors.player color;
	private int score;
	private int numTrains;
	
	/**
	 * Init Player for game start.
	 * 
	 * @param color
	 */
	public Player(Colors.player color) {
		this(new ArrayList<DestinationTicket>(), new ArrayList<TrainCard>(), color, 0, 45);
	}
	
	public Player(ArrayList<DestinationTicket> dts, ArrayList<TrainCard> hand,
			Colors.player color, int score, int numTrains) {
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
	public Colors.player getColor(){
		return color;
	}
	public void setColor(Colors.player color) {
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
	
	/**
	 * Since there can only be one player of a given color in a game, equality is having the same color.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Player) {
			return ((Player)other).color == color; 
		}
		return false;
	}
}
