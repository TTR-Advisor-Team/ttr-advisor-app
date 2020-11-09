package com.ttradvisor.app.classes;

public class TrainCard {
	private Colors.route color;
	
	public TrainCard(Colors.route color) {
		this.color = color;
	}

	public Colors.route getColor() {
		return color;
	}
	
	public String toString() {
		return color.toString();
	}
	
}
