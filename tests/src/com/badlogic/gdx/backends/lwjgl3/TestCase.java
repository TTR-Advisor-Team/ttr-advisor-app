package com.badlogic.gdx.backends.lwjgl3;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.TestApplication;
import com.ttradvisor.app.TTRAdvisorApp;

import org.junit.AfterClass;
import org.junit.Test;

public class TestCase {

protected final TTRAdvisorApp application;

  private final TestApplication testApplication;

  public TestCase() {
    this.application = new TTRAdvisorApp();

    this.testApplication = new TestApplication(application);

    application.create();
  }

  @AfterClass
  public static void afterAll() {
    Gdx.app.exit();
  }

  @Test
  public void testSomething() {
    // Here you'll have access to anything that uses OpenGL stuff, like SpriteBatch.
	  Gdx.app.log("Test", "Routes from Miami: " + application.gameState.getBoard().getAllRoutes("Miami"));
  }
}