package com.ttradvisor.app.classes;
import java.util.ArrayList;
import java.util.List;

public class DestinationAction extends Action {
	ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
	
	public DestinationAction(Player actingPlayer, ArrayList<DestinationTicket> tickets) {
		super(actingPlayer);
		drawnTickets = tickets;
	}
	

}
