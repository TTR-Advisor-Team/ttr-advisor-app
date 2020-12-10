package com.ttradvisor.app.classes;
import java.util.ArrayList;

public class DestinationAction extends Action {
	private ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
	
	public DestinationAction(Player actingPlayer, ArrayList<DestinationTicket> tickets) {
		super(actingPlayer);
		drawnTickets = tickets;
	}
	
	public ArrayList<DestinationTicket> getDrawnTickets() {
		return drawnTickets;
	}

}
