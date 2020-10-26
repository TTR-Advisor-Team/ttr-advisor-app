package com.ttradvisor.app.classes;
import java.util.ArrayList;
import java.util.List;

public class DestinationAction extends Action {
	ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
	
	public DestinationAction(ArrayList<DestinationTicket> tickets) {
		drawnTickets = tickets;
	}
	

}
