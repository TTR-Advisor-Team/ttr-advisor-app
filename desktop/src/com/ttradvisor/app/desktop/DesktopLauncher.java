package com.ttradvisor.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ttradvisor.app.TTRAdvisorApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.pauseWhenMinimized = true;
		
		// config options set for testing
		
		config.width = 1080;
		config.height = 720;
//		config.fullscreen = true;
		config.resizable = false;
		config.title = "Ticket to Ride Advisor";
		
		new LwjglApplication(new TTRAdvisorApp(), config);
	}
}
