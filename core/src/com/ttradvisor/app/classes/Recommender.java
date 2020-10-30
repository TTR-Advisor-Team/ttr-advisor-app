package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import com.ttradvisor.app.classes.Board.Route;

public class Recommender {
	Board board;
	Player player;

	public Recommender(Board board, Player player) {
		this.board = board;
		this.player = player;
	}

	public void calculate(ArrayList<DestinationTicket> tickets) {
		for (DestinationTicket dt : tickets) {
			LinkedList<Route> route= shortestPath(dt.getStart(), dt.getEnd());
			int cost = 0;
			for(Route r: route) {
				System.out.println(r.toString());
				cost += r.getCost();
			}
			System.out.println(cost);
		}
	}

	public LinkedList<Route> shortestPath(String begin, String end) {

		LinkedList<Route> routes = new LinkedList<Route>();
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
			closed.add(c);
			System.out.println(c.current);
			// check if goal has been reached
			//System.out.println(c.current + " == " + end + ": " + c.current.equals(end));
			if (c.current.equals(end)) {
				// follow previous until null and add them to routes list
				// these are the routes to focus on claiming or saving up for
				while (c.previous != null) {
					//System.out.println(board.getRoute(c.current, c.previous.current).toString());
					routes.add(board.getRoute(c.current, c.previous.current));
					c = c.previous;
				}
				int cost = 0;
				System.out.println("FINAL ROUTE");
				for(Route r: routes) {
					System.out.println(r.toString());
					cost += r.getCost();
				}
				System.out.println("Total route cost: " + cost);

				return routes;
			}

			// if the goal has not been reached
			// expand search to all adjacent cities		
			
			for (Route r : board.getAllRoutes(c.current)) {
				boolean open = false;
				boolean close = false;
				System.out.println(r.toString());
				City next = new City(r.end, c, c.totalCost + r.cost);
				// if openSet contains r.end compare totalCost
				for (City old : openSet) {
					// if less update totalCost and previous
					if (old.equals(next)) {
						open = true;
						next.totalCost = calcCost(c, next.current);
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
						close = true;
						next.totalCost = calcCost(c, next.current);
						if (old.totalCost > next.totalCost) {
							old.previous = next.previous;
							old.totalCost = next.totalCost;
						}
						// else ignore
					}
				}

				// if not on openSet or closed, place in open
				if(!open && !close)
					openSet.add(next);
			}
		}
		return null;
	}

	// calculate total cost of route from city to city
	private int calcCost(City currentCity, String nextCity) {
		LinkedList<Route> routes = board.getAllRoutes(currentCity.current);
		for (Route r : routes) {
			if (!r.end.equals(nextCity))
				continue;
			if (r.owner == Colors.player.NONE)
				return currentCity.totalCost + r.cost;
			else if (r.owner == player.getColor())
				return currentCity.totalCost + 0;
			else
				return currentCity.totalCost + 1000;
		}
		return -1;
	}

	static private class City {
		String current;
		City previous;
		int totalCost;

		private City(String current, City previous, int totalCost) {
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