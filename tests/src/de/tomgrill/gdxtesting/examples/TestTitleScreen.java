package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class TestTitleScreen {
	
	@Test
	public void testMockTTRApp() {
		
		TTRAdvisorApp testApp = new TTRAdvisorApp();
		testApp.create();
		
		System.out.println(testApp.gameState.getBoard().getBoard());
	}

}
