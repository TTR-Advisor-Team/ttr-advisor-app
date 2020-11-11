package de.tomgrill.gdxtesting.examples;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.TrainCard;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@RunWith(GdxTestRunner.class)
public class TestPlayer {
	Player p = new Player(new ArrayList<DestinationTicket>(), new ArrayList<TrainCard>(), Colors.player.BLACK, 15, 39);
	Player p1 = new Player(Colors.player.BLACK);
	
	@Test
	public void playerNotNullTest1() {
		assertNotNull(p);
	}
	
	@Test
	public void playerNotNullTest2() {
		assertNotNull(p1);
	}
	
	@Test
	public void playergetDtsTest1() {
		p.getDTS().add(new DestinationTicket("Miami", "Los Angeles"));
		assertEquals(false, p.getDTS().isEmpty());
	}
	
	@Test
	public void playergetDtsTest2() {
		assertEquals(true, p1.getDTS().isEmpty());
	}
	
	@Test
	public void playergetTcsTest1() {
		p.getTCS().add(new TrainCard(Colors.route.BLACK));
		p.getTCS().add(new TrainCard(Colors.route.ANY));
		assertNotNull(p.getTCS());
	}
	
	@Test
	public void playergetTcsTest2() {
		assertEquals(true, p1.getTCS().isEmpty());
	}
	
	@Test
	public void playergetColorTest() {
		assertEquals(Colors.player.BLACK, p1.getColor());
	}
	
	
	@Test
	public void playerScoreTest() {
		p1.setScore(1);
		assertEquals(1, p1.getScore());
	}
	
	@Test
	public void playerNumTrainsTest() {
		p1.setNumTrains(40);
		assertEquals(40, p1.getNumTrains());
	}
	
	@Test
	public void playerNumColorTest1() {
		p.getTCS().add(new TrainCard(Colors.route.BLACK));
		p.getTCS().add(new TrainCard(Colors.route.ANY));
		assertEquals(1, p.getNumberOfColor(Colors.route.BLACK));
	}
	
	@Test
	public void playerNumColorTest2() {
		p.getTCS().add(new TrainCard(Colors.route.BLACK));
		p.getTCS().add(new TrainCard(Colors.route.ANY));
		assertEquals(0, p.getNumberOfColor(Colors.route.BLUE));
	}
	
	@Test
	public void playerNumColorUsableTest1() {
		p.getTCS().add(new TrainCard(Colors.route.BLACK));
		p.getTCS().add(new TrainCard(Colors.route.ANY));
		assertEquals(2, p.getNumberOfUsable(Colors.route.BLACK));
	}
	
	@Test
	public void playerNumColorUsableTest2() {
		p.getTCS().add(new TrainCard(Colors.route.BLACK));
		p.getTCS().add(new TrainCard(Colors.route.ANY));
		assertEquals(p.getNumberOfUsable(Colors.route.GREEN), p.getNumberOfUsable(Colors.route.RED));
	}
	
	@Test
	public void playerDeepCopyTest() {
		Player p2 = p1.getDeepCopy();
		assertEquals(p1, p2);
	}
	
	@Test
	public void playerEqualsTest() {
		assertEquals(true, p1.equals(p));
	}
}
