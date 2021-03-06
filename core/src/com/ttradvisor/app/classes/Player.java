package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

import com.ttradvisor.app.classes.Board.Route;

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
	
	public ArrayList<DestinationTicket> getDTS(){
		return destTicketHand;
	}

	public ArrayList<TrainCard> getTCS(){
		return trainCardHand;
	}

	public Colors.player getColor(){
		return color;
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

//	public TrainCard getCardOfColor(Colors.route cardColor){
//		for (TrainCard card : trainCardHand) {
//			if(card.getColor() ==  cardColor) {
//				return card;
//			}
//		}
//		//if no card of the correct color is found
//		return null;
//	}
	public int getNumberOfColor(Colors.route cardColor){
		int i = 0;
		for (TrainCard card : trainCardHand) {
			if(card.getColor() ==  cardColor) {
				i++;
			}
		}
		return i;
	}
	public int getNumberOfUsable(Colors.route cardColor){
		int i = 0;
		for (TrainCard card : trainCardHand) {
			if(card.getColor() ==  cardColor || card.getColor() == Colors.route.ANY) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * @return true if the player has the cards in this list
	 */
	public boolean canPlayerSpendCards(List<TrainCard> spentCards) {
		ArrayList<TrainCard> hand = (ArrayList<TrainCard>) trainCardHand.clone();
		for (TrainCard spentCard : spentCards) {
			if (!hand.remove(spentCard)) {
				// the player didn't have that card!
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove all the cards in the list from the player's hand
	 * Assumes canPlayerSpendCards has been validated
	 */
	public  void spendCards(List<TrainCard> spentCards) {
		for (TrainCard spentCard : spentCards) {
			trainCardHand.remove(spentCard);
		}
	}
	
	/**
	 * @return a deep copy of this Player object (incl. internal lists)
	 */
	@SuppressWarnings("unchecked")
	public Player getDeepCopy() {
		ArrayList<DestinationTicket> deepCopyDTHand = (ArrayList<DestinationTicket>) destTicketHand.clone();
		ArrayList<TrainCard> deepCopyTCHand = (ArrayList<TrainCard>) trainCardHand.clone();
		
		return new Player(deepCopyDTHand, deepCopyTCHand, color, score, numTrains);
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
