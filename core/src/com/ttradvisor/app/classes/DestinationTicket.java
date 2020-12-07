package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.List;

public class DestinationTicket {
	private ArrayList<String> startEnd = new ArrayList<String>();
	private int value;
	private boolean completed;
	
	
	public DestinationTicket(String start, String end) {
		startEnd.add(start);
		startEnd.add(end);
		//Maybe some shortest path calculation if passed in without length 
	}
	
	public DestinationTicket(String start, String end, int length) {
		setValue(length);
		startEnd.add(start);
		startEnd.add(end);
	}
	public void setCompleted(boolean complete) {
		completed = complete;
	}
	
	public void setValue(int length) {
		value = length;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getStart() {
		return startEnd.get(0);
	}
	
	public String getEnd() {
		return startEnd.get(1);
	}
	
	@Override
	public String toString() {
		return startEnd.get(0) + " to " + startEnd.get(1) + ", " + Integer.toString(value);
	}

}
