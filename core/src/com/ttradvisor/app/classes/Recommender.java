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
//		for (Route r : routes) {
//			System.out.println(r.toString() + " color: " + r.getColor());
//		}
//		System.out.println("\n\n\n");
		// Remove routes already owned by player. Removed transposed routes. Remove
		// duplicate routes.
		ArrayList<Route> dup = new ArrayList<Route>();
		for (int x = 0; x < routes.size(); ++x) {
			if (routes.get(x) == null)
				continue;
			if (routes.get(x).getOwner().equals(player.getColor())) {
				dup.add(routes.get(x));
				routes.set(x, null);
				continue;
			}
			for (int y = 0; y < routes.size(); ++y) {
				if (routes.get(y) == null)
					continue;
				if (x == y)
					continue;
				if (routes.get(x).getBegin().equals(routes.get(y).getBegin())
						&& routes.get(x).getEnd().equals(routes.get(y).getEnd())
						&& routes.get(x).getColor().equals(routes.get(y).getColor())) {
					routes.set(y, null);
				} else if (routes.get(x).getBegin().equals(routes.get(y).getEnd())
						&& routes.get(x).getEnd().equals(routes.get(y).getBegin())
						&& routes.get(x).getColor().equals(routes.get(y).getColor())) {
					routes.set(y, null);
				} else if (routes.get(y).getOwner().equals(player.getColor())) {
					dup.add(routes.get(y));
					routes.set(y, null);
					
				}
			}
		}
		for(Route r: dup) {
			for(int i = 0; i < routes.size();) {
				if(r.begin.equals(routes.get(i).begin) && r.end.equals(routes.get(i).end)) {
					routes.set(i, null);
					break;
				}
				else {
					i++;
					
				}
			}
		}
		if (!routes.isEmpty()) {
			int i = 0;
			while (i < routes.size()) {
				if (routes.get(i) == null)
					routes.remove(i);
				else
					i++;
			}
		}

//		for (Route r : routes) {
//			System.out.println(r.toString() + " color: " + r.getColor());
//		}
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
			ArrayList<Route> routes = shortestPath(dt, player.getColor());
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
	public ArrayList<Route> shortestPath(DestinationTicket ticket, Colors.player player) {

		System.out.println("Start: " + ticket.getStart() + " End: " + ticket.getEnd());
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
		openSet.add(new City(ticket.getStart(), null, 0));

		while (!openSet.isEmpty()) {
			City c = openSet.poll();
			closed.add(c);
			if (c.totalCost >= 1000) {
				return routes;
			}
			// check if goal has been reached
			if (c.current.equals(ticket.getEnd())) {
				if (c.totalCost == 0) {
					ticket.setCompleted(true);
				}
				// follow previous until null and add them to routes list
				// these are the routes to focus on claiming or saving up for
				while (c.previous != null) {
					routes.addAll(board.getAllRoutesBetween(c.current, c.previous.current, player));
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

				City next = new City(r.end, c, calcCost(c, r.end, player));
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
	private int calcCost(City currentCity, String nextCity, Colors.player player) {
		LinkedList<Route> routes = board.getAllRoutes(currentCity.current);
		for (Route r : routes) {
			if (!r.end.equals(nextCity))
				continue;
			if (r.owner == Colors.player.NONE)
				return currentCity.totalCost + r.cost;
			else if (r.owner == player)
				return currentCity.totalCost + 0;
			else
				return currentCity.totalCost + 1000;
		}
		return 100000;
	}

	public int longestRoute(Colors.player player) {
		ArrayList<Route> routes = board.getAllRoutesOfPlayer(player);
		ArrayList<String> cities = new ArrayList<String>();
		ArrayList<Integer> lengths = new ArrayList<Integer>();

		for (Route r : routes) {
			if (!cities.contains(r.begin))
				cities.add(r.begin);

		}
		// get longest routes values for all cities found within the routes
		for (String city : cities) {
			int l = LRHelper(0, city, routes);
			LRHelper(0, city, routes);
			lengths.add(l);
		}

		int longestRoute = 0;
		// find max
		for (Integer l : lengths) {
			if (l >= longestRoute)
				longestRoute = l;
		}
		// return maxF
		return longestRoute;
	}

	public int LRHelper(int totalLength, String city, ArrayList<Route> routes) {

		boolean next = false;
		if (routes.isEmpty()) {

		} else {
			for (Route r : routes) {
				if (r.begin.equals(city))
					next = true;
			}
		}
		if (!next) {
			System.out.println();
			return totalLength;
		} else {
			ArrayList<Integer> len = new ArrayList<Integer>();
			for (Route r : routes) {
				// find a route that connect to current city
				if (r.begin.equals(city)) {
					System.out.println("Next city: " + r.end);
					// make a new list and remove the route, and its inverse from the list
					ArrayList<Route> newRoutes = new ArrayList<Route>();
					newRoutes.addAll(routes);

//					for (int j = 0; j < 2; ++j) {
//						for (int i = 0; i < newRoutes.size(); ++i) {
//
//							if (newRoutes.get(i).begin.equals(r.begin) && newRoutes.get(i).end.equals(r.end)
//									|| newRoutes.get(i).end.equals(r.begin) && newRoutes.get(i).begin.equals(r.end)) {
//								newRoutes.remove(i);
//								break;
//							}
//
//						}
//					}
					
					ArrayList<Route> removeRoutes = new ArrayList<Route>();				
					for (Route nr: routes) {					
						if (nr.begin.equals(r.begin) && nr.end.equals(r.end) || nr.end.equals(r.begin) && nr.begin.equals(r.end)) {
							removeRoutes.add(nr);
						}
					}
					for (Route rr: removeRoutes) {
						newRoutes.remove(rr);
					}

					int l = LRHelper(totalLength + r.cost, r.end, newRoutes);
					len.add(l);
				}
			}
			int l = 0;
			for (Integer i : len) {
				if (i >= l)
					l = i;
			}
			return l;
		}
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