package com.danielvilla.joc;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.danielvilla.joc.Game;

public class DesktopLauncher {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Drop");
		config.setWindowedMode(800,400);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Game(), config);
	}
}