package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class DestinationTicket {
	ArrayList<String> startEnd = new ArrayList<String>();
	int value;
	
	
	public DestinationTicket(String start, String end) {
		
	}
	
	public DestinationTicket(String start, String end, int length) {
		value = length;
	}

	//
}
