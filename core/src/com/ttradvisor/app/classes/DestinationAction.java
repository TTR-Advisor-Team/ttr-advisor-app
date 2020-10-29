package com.ttradvisor.app.classes;
import java.util.ArrayList;

public class DestinationAction extends Action {
	ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
	
	public DestinationAction(ArrayList<DestinationTicket> tickets) {
		drawnTickets = tickets;
	}
	
	public ArrayList<DestinationTicket> getDrawnTickets() {
		return drawnTickets;
	}
	
	public void setDrawnCards(ArrayList<DestinationTicket> tickets) {
		this.drawnTickets = tickets;
	}

}
