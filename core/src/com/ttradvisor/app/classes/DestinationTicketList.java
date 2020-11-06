package com.ttradvisor.app.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DestinationTicketList {

	private LinkedList<DestinationTicket> dtList;

	public DestinationTicketList(String path) {
		dtList = new LinkedList<DestinationTicket>();
		try {
			FileHandle handle = Gdx.files.internal(path);
			File initList = handle.file();
			String allTickets = handle.readString();
			String[] dt = allTickets.split(System.lineSeparator());
			for (String s : dt) {
				String[] data = s.split(",");
				for (int i = 0; i < data.length / 3; ++i) {
					dtList.add(new DestinationTicket(data[0], data[1 + (i * 3)], Integer.parseInt(data[2 + (i * 3)])));
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
			dtList = null;
		}
	}

	public DestinationTicket getTicket(int index) {
		return dtList.get(index);
	}

	public LinkedList<DestinationTicket> getList() {
		return dtList;
	}

}
