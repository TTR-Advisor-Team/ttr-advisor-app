package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import com.ttradvisor.app.classes.Board.Route;

public class Recommender {
	Board board;
	Player player;

	public Recommender(Board board) {
		this.board = board;
		this.player = player;
	}

	public LinkedList<Route> shortestPath(ArrayList<DestinationTicket> tickets) {

		LinkedList<Route> routes = new LinkedList<Route>();
		
		for (DestinationTicket dt : tickets) {
			
			PriorityQueue<City> openSet = new PriorityQueue<City>(new Comparator<City>() {
				public int compare(City c1, City c2) {
					if (c1.totalCost > c2.totalCost)
						return 1;
					if (c1.totalCost < c2.totalCost)
						return -1;
					return 0;
				}
			});

			String begin = dt.startEnd.get(0);
			String end = dt.startEnd.get(1);

			LinkedList<City> closed = new LinkedList<City>();
			openSet.add(new City(begin, null, 0));

			while (!openSet.isEmpty()) {
				City c = openSet.poll();

				// check if goal has been reached
				if (c.current.equals(end)) {
					// follow previous until null and add them to routes list
					// these are the routes to focus on claiming or saving up for
					while (c.previous != null) {
						routes.add(board.getRoute(c.current, c.previous));
						break;
					}

				}

				// if the goal has not been reached
				// expand search to all adjacent cities
				for (Route r : board.getAllRoutes(c.current)) {
					City next = new City(r.end, r.begin, calcCost(c, r.end));
					// if openSet contains r.end compare totalCost
					for (City old : openSet) {
						// if less update totalCost and previous
						if (old.equals(next)) {
							if (old.totalCost > next.totalCost) {
								old.previous = next.previous;
								old.totalCost = next.totalCost;
							}
							// else ignore
						}

					}
					// if closed contains r.end compare totalCost
					for (City old : closed) {
						// if less update totalCost and previous
						if (old.equals(next)) {
							if (old.totalCost > next.totalCost) {
								old.previous = next.previous;
								old.totalCost = next.totalCost;
							}
							// else ignore
						}
					}
					
					// if not on openSet or closed, place in open
					openSet.add(next);
				}

				// visited node get added to close
				closed.add(c);
			}

		}

		return routes;
	}

	// calculate total cost of route from city to city
	private int calcCost(City start, String end) {
		LinkedList<Route> routes = board.getAllRoutes(start.current);
		for (Route r : routes) {
			if (r.end.equals(end))
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

		@Override
		public boolean equals(Object obj) {
			City that = (City) obj;
			return this.current.equals(that.current);
		}

	}
}