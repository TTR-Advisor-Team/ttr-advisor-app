package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;

@RunWith(GdxTestRunner.class)
public class TestBoard {
	
	@Test
	public void boardNotNull() {
		Board b = new Board("cities.txt");
		assertNotNull(b);
	}
	@Test
	public void getBoardNotNull() {
		Board b = new Board("cities.txt");
		assertNotNull(b.getBoard());
	}
	@Test
	public void handleNotNull() {
		FileHandle handle = Gdx.files.internal("cities.txt");
		assertNotNull(handle);
	}

	@Test
	public void fileNotNull() {
		FileHandle handle = Gdx.files.internal("cities.txt");
		File file = handle.file();
		assertNotNull(file);
	}
	
	@Test
	public void notHere() {
		Board b = new Board("NotHere.txt");
		assertNull(b.getBoard());
	}
	
	@Test
	public void goodRoute() {
		Route r = new Route("a","b",Colors.route.ANY,2);
		assertNotNull(r);
	}
}
