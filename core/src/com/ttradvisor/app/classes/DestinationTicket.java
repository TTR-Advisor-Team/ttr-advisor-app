package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class DestinationTicket {
	ArrayList<City> startEnd = new ArrayList<City>();
	int value;
	
	
	public DestinationTicket(City start, City end) {
		
	}
	
	public DestinationTicket(City start, City end, int length) {
		value = length;
	}

	//
}
