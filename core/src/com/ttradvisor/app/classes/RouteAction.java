package com.ttradvisor.app.classes;

import java.util.ArrayList;

import com.ttradvisor.app.classes.Board.Route;

public class RouteAction extends Action{
	ArrayList<TrainCard> spentCards;
	Route claimedRoute;
	
	public RouteAction(Player actingPlayer, ArrayList<TrainCard> spentCards, Route claimedRoute) {
		super(actingPlayer);
		this.spentCards = spentCards;
		this.claimedRoute = claimedRoute;
	}
}
