package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;
import java.util.HashMap;

import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors.player;

public class Recommender {
	Board board;
	Player player;
	int turn;
	int numPlayers;

	public Recommender(Board board, Player player, int turn, int numPlayers) {
		this.board = board;
		this.player = player;
		this.turn = turn;
		this.numPlayers = numPlayers;
	}

	/**
	 * 
	 * @param tickets
	 * @return strings that tell the player what the best options are
	 */
	public ArrayList<String> calculate(ArrayList<DestinationTicket> tickets) {
		ArrayList<String> recommendations = new ArrayList<String>();
		// find all routes on shortest path between cities
		ArrayList<Route> routes = getRoutes(tickets);
		routes.sort(new Comparator<Route>() {
			public int compare(Route r1, Route r2) {
				return r2.getCost() - r1.getCost();
			}
		});
		// calculate the player's train ticket resources
		HashMap<Colors.route, Integer> resources = getResources();

		// if player has completed all destination tickets
		if (tickets.isEmpty()) {
			// the game is still relatively new
			if (turn <= numPlayers * 10) {
				recommendations.add("Draw destination ticket.");
			} else {
				// the game is too far along to realistically complete another ticket
				for (Colors.route c : resources.keySet()) {
					if (resources.get(c) > 5) {
						recommendations.add("Claim the most expensive " + c + " or GRAY route.");
					} else {
						String s = "Draw train cards. Priority: " + Colors.route.ANY;
						if (!recommendations.contains(s))
							recommendations.add(s);
					}
				}

			}
		}
		// tickets might be impossible to complete
		else {
			// try for new destinations ticket as the game is still new
			if ((turn <= numPlayers * 10) && routes.isEmpty()) {
				recommendations.add("Draw destination ticket.");
			} else {
				// the game is too far along to realistically complete another ticket
				if (routes.isEmpty()) {
					for (Colors.route c : resources.keySet()) {
						if (resources.get(c) > 5) {
							recommendations.add("Claim the most expensive " + c + " or GRAY route.");
						} else {
							String s = "Draw train cards. Priority: " + Colors.route.ANY;
							if (!recommendations.contains(s))
								recommendations.add(s);
						}
					}
				}

			}
		}

		// if can purchase a route in routes, buy it
		for (Route r : routes) {
			if (((resources.get(r.getColor()) + resources.get(Colors.route.ANY)) >= r.getCost())
					&& (player.getNumTrains() >= r.cost) && (r.getOwner().equals(Colors.player.NONE))) {
				recommendations
						.add("Claim " + r.getColor() + " route from " + r.getBegin() + " to " + r.getEnd() + ".");
			}
		}

		// if not draw train cards, prioritize longer routes
		for (Route r : routes) {
			if (r.getOwner().equals(Colors.player.NONE)) {
				String s = "Draw train cards. Priority: " + r.color;
				if (!recommendations.contains(s))
					recommendations.add(s);
			}

		}
		// or go for the longest route bonus. Iteration3
		while (recommendations.size() < 3) {
			recommendations.add("No recommendation");
		}
		return recommendations;
	}

	/**
	 * 
	 * @return a mapping of the number of each train card resource
	 */
	public HashMap<Colors.route, Integer> getResources() {
		HashMap<Colors.route, Integer> resources = new HashMap<Colors.route, Integer>();
		for (Colors.route c : Colors.route.values()) {
			resources.put(c, 0);
		}
		for (TrainCard tc : player.getTCS()) {
			resources.replace(tc.getColor(), resources.get(tc.getColor()) + 1);
		}
		return resources;
	}

	/**
	 * 
	 * @param tickets
	 * @return all unique routes that a player needs to complete their destination
	 *         tickets, can return empty if impossible to complete route(s)
	 */
	public ArrayList<Route> getRoutes(ArrayList<DestinationTicket> tickets) {
		ArrayList<Route> allRoutes = new ArrayList<Route>();
		for (DestinationTicket dt : tickets) {
			ArrayList<Route> routes = shortestPath(dt.getStart(), dt.getEnd());
//			System.out.println("tickets " + tickets);
//			System.out.println("dt " + dt);
//			System.out.println("size" + routes.size());
			for (Route r : routes) {
				if (!allRoutes.contains(r))
					allRoutes.add(r);
			}
		}
		return allRoutes;
	}

	/**
	 * Implementation of Dijkstra's Algorithm. Currently there is no termination
	 * protocol for routes impossible to complete.
	 * 
	 * @param begin the starting node of the path
	 * @param end   then ending node of the path
	 * @return A list of all routes on the shortest path
	 */
	public ArrayList<Route> shortestPath(String begin, String end) {

		ArrayList<Route> routes = new ArrayList<Route>();
		PriorityQueue<City> openSet = new PriorityQueue<City>(new Comparator<City>() {
			public int compare(City c1, City c2) {
				// if (c1.totalCost > c2.totalCost)
				// return 1;
				// if (c1.totalCost < c2.totalCost)
				// return -1;
				return c1.totalCost - c2.totalCost;
			}
		});

		LinkedList<City> closed = new LinkedList<City>();
		openSet.add(new City(begin, null, 0));

		while (!openSet.isEmpty()) {
			City c = openSet.poll();
			closed.add(c);
			if (c.totalCost >= 1000) {
				return routes;
			}
			// check if goal has been reached
			if (c.current.equals(end)) {
				// follow previous until null and add them to routes list
				// these are the routes to focus on claiming or saving up for
				while (c.previous != null) {
					routes.addAll(board.getAllRoutesBetween(c.current, c.previous.current, player.getColor()));
					c = c.previous;
				}

//				int cost = 0;
//				System.out.println("FINAL ROUTE");
//				Route prev = null;
//				for (Route r : routes) {
//					if (prev == null) {
//						System.out.println(r.toString());
//						cost += r.getCost();
//					} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
//						cost += 0;
//					} else {
//						cost += r.getCost();
//						System.out.println(r.toString());
//					}
//					prev = r;
//				}
//				System.out.println("Total route cost: " + cost);

				return routes;
			}

			// if the goal has not been reached
			// expand search to all adjacent cities

			for (Route r : board.getAllRoutes(c.current)) {
				boolean open = false;
				boolean close = false;

				City next = new City(r.end, c, calcCost(c, r.end));
				// if openSet contains r.end compare totalCost
				for (City old : openSet) {
					// if less update totalCost and previous
					if (old.equals(next)) {
						open = true;
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
						if (old.totalCost > next.totalCost) {
							old.previous = next.previous;
							old.totalCost = next.totalCost;
						}
						// else ignore
					}
				}

				// if not on openSet or closed, place in open
				if (!open && !close)
					openSet.add(next);
			}
		}
		return null;
	}

	/**
	 * Cost calculator for Dijkstra's Algorithm
	 * 
	 * @param currentCity
	 * @param nextCity
	 * @return the total cost of traversing to the next city
	 */
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
		return 100000;
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