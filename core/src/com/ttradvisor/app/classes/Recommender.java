package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import com.ttradvisor.app.classes.Board.Route;

public class Recommender {
	Board board;
	Player player;
	public Recommender (Board board) {
		this.board = board;
		this.player = player;
	}
	public LinkedList<LinkedList<Route>> shortestPath(ArrayList<DestinationTicket> tickets) {
		
		LinkedList<LinkedList<Route>> routes = new LinkedList<LinkedList<Route>>();

		for (DestinationTicket dt : tickets) {
			
			String begin = dt.startEnd.get(0);
			String end = dt.startEnd.get(1);
			
			PriorityQueue<City> openSet = new PriorityQueue<City>(new Comparator<City>() {
				public int compare(City c1, City c2) {
					if (c1.totalCost > c2.totalCost)
						return 1;
					if (c1.totalCost < c2.totalCost)
						return -1;
					return 0;
				}
			});
			
			LinkedList<City> closed = new LinkedList<City>();
			openSet.add(new City(begin, null, 0));
			
			while (!openSet.isEmpty()) {
				City c = openSet.poll();
				
				//check if goal has been reached
				if(c.current.compareTo(end) == 0) {
					//follow previous until null and add them to routes list
					//these are the route to focus claiming or saving up for
					//sometimes duplicate routes can be added because they will
					//be optimal for mulitple DT's
				}
				
				//if the goal has not been reached
				//expand search to all adjacent cities
				for (Route r: board.getRoutes(c.current)) {
					//if openset contain r.end compare totalCost
					//if less update totalCost and previous
					//else ignore
					//if closed contains r.end compare totalCost
					//else ignore
				    //if not on openSet or closed
					//calulate totalCost and place in open
					openSet.add(new City(r.end, c.current, calcCost(c, r.end)));
				}
				
				//visited node get added to close
				closed.add(c);
			}

		}

		return routes;
	}
	//calculate total cost of route from city to city
	private int calcCost(City start, String end) {
		LinkedList<Route> routes = board.getRoutes(start.current);
		for(Route r: routes) {
			if(r.end.compareTo(end) != 0) 
				continue;
			if (r.owner == Colors.player.NONE)
				return start.totalCost + r.cost;
			else if (r.owner == player.getColor())
				return start.totalCost + 0;
			else
				return start.totalCost + 1000;
		}
		return -1;
	}

	static private class City {
		String current;
		String previous;
		int totalCost;

		private City(String previous, String current, int totalCost) {
			this.current = current;
			this.previous = previous;
			this.totalCost = totalCost;
		}
	}
}