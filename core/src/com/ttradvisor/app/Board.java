package com.ttradvisor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Board {
	private HashMap<String, LinkedList<Route>> board;

	public Board() {
		this.board = new HashMap<String, LinkedList<Route>>();
		FileHandle handle = Gdx.files.internal("cities.txt");
		System.out.println(handle.path());
		File initBoard = handle.file();
		Scanner reader;
		try {
			reader = new Scanner(initBoard);
			while (reader.hasNextLine()) {
				String[] data = reader.nextLine().split(",");
				String city = data[0];
				LinkedList<Route> routes = new LinkedList<Route>();
				for (int i = 0; i < data.length / 4; ++i) {
					routes.add(new Route(data[0], data[1 + (i * 4)], Colors.route.valueOf(data[2 + (i * 4)]),
							Integer.parseInt(data[3 + (i * 4)])));
				}
				board.put(city, routes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public HashMap<String, LinkedList<Route>> getBoard() {
		return board;
	}

	static class Route {
		String begin;
		String end;
		Colors.route color;
		int cost;

		public Route(String begin, String end, Colors.route color, int cost) {
			this.begin = begin;
			this.end = end;
			this.color = color;
			this.cost = cost;
		}
	}
}