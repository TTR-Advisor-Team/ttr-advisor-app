// Andrey's note - trial at making the JUnit testing work

package de.tomgrill.gdxtesting;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ttradvisor.app.TTRAdvisorApp;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Sourced from https://github.com/TomGrill/gdx-testing
 *
 */
public class GdxTestRunnerTrial extends BlockJUnit4ClassRunner implements ApplicationListener {
	
	public static TTRAdvisorApp testApp;

	protected Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<FrameworkMethod, RunNotifier>();
	
	public GdxTestRunnerTrial(Class<?> klass) throws InitializationError {
		super(klass);
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.pauseWhenMinimized = true;
		
		// config options set for testing
		
		config.width = 1080;
		config.height = 720;
//		config.fullscreen = true;
		config.resizable = false;
		config.title = "Ticket to Ride Advisor";
		
		testApp = new TTRAdvisorAppTest(this);
		new LwjglApplication(testApp, config);
		

		// guiStage.getRoot().findActor(name);
	}

	@Override
	public void create() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
		synchronized (invokeInRender) {
			for (Map.Entry<FrameworkMethod, RunNotifier> each : invokeInRender.entrySet()) {
				super.runChild(each.getKey(), each.getValue());
			}
			invokeInRender.clear();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		synchronized (invokeInRender) {
			// add for invoking in render phase, where gl context is available
			invokeInRender.put(method, notifier);
		}
		// wait until that test was invoked
		waitUntilInvokedInRenderMethod();
	}

	/**
	    *
	    */
	protected void waitUntilInvokedInRenderMethod() {
		try {
			while (true) {
				Thread.sleep(10);
				synchronized (invokeInRender) {
					if (invokeInRender.isEmpty())
						break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
