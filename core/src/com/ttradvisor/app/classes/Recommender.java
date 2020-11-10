package com.ttradvisor.app.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;

import com.ttradvisor.app.classes.Board.Route;

public class Recommender {
	Board board;
	Player player;

	public Recommender(Board board, Player player) {
		this.board = board;
		this.player = player;
	}
	/**
	 * 
	 * @param tickets
	 * @return strings that tell the player what the best option is
	 */
	public String[] calculate(ArrayList<DestinationTicket> tickets) {
		String[] recommendations = new String[3];
		ArrayList<Route> routes = getRoutes(tickets);
		for(Route r: routes) {
			System.out.println(r.toString());
		}
		// calculate the player's train ticket resources
		HashMap<Colors.route, Integer> resources = getResources();
		
		// if can purchase a route in routes, buy it
		// if not draw train cards, prioritize those closest to completion, if tied prioritize longer routes
		
		// if not possible to complete, either draw new destination tickets, or claim as many expensive
		// routes as possible depending on how many turns have expired and how many expected turns are left
		
		// or go for the longest route bonus. Iteration3
		
		return recommendations;
	}
	/**
	 * 
	 * @return a mapping of the number of each train card resource
	 */
	public HashMap<Colors.route, Integer> getResources() {
		HashMap<Colors.route, Integer> resources = new HashMap<Colors.route, Integer>();
		for (Colors.route c: Colors.route.values()) {
			resources.put(c, 0);			
		}
		for(TrainCard tc: player.getTCS()) {
			resources.replace(tc.getColor(), resources.get(tc.getColor())+1);
		}
		return resources;
	}
	/**
	 * 
	 * @param tickets
	 * @return all unique routes that a player needs to complete their destination tickets
	 */
	public ArrayList<Route> getRoutes(ArrayList<DestinationTicket> tickets){
		ArrayList<Route> allRoutes = new ArrayList<Route>();
		for (DestinationTicket dt : tickets) {
			ArrayList<Route> routes = shortestPath(dt.getStart(), dt.getEnd());
			for(Route r: routes) {
				if (!allRoutes.contains(r)) 
					allRoutes.add(r);
			}
		}
		return allRoutes;
	}
	/**
	 * Implementation of Dijkstra's Algorithm
	 * @param begin the starting node of the path
	 * @param end then ending node of the path
	 * @return A list of all routes on the shortest path
	 */
	public ArrayList<Route> shortestPath(String begin, String end) {

		ArrayList<Route> routes = new ArrayList<Route>();
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
			//System.out.println(c.current + " total cost:" + c.totalCost);
			// check if goal has been reached
			if (c.current.equals(end)) {
				// follow previous until null and add them to routes list
				// these are the routes to focus on claiming or saving up for
				while (c.previous != null) {
					//System.out.println(board.getRoute(c.current, c.previous.current).toString());
					routes.add(board.getRoute(c.current, c.previous.current));
					c = c.previous;
				}
				/*
				int cost = 0;
				System.out.println("FINAL ROUTE");
				for(Route r: routes) {
					System.out.println(r.toString());
					cost += r.getCost();
				}
				System.out.println("Total route cost: " + cost);
				*/
				return routes;
			}

			// if the goal has not been reached
			// expand search to all adjacent cities		
			
			for (Route r : board.getAllRoutes(c.current)) {
				boolean open = false;
				boolean close = false;
				//System.out.println(r.toString());
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
				if(!open && !close)
					openSet.add(next);
			}
		}
		return null;
	}

	/**
	 * Cost calculator for Dijkstra's Algorithm
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