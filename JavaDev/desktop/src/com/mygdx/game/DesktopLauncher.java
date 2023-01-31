package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Launches libgdx game upon running on any desktop device.
 *
 */
public class DesktopLauncher {
    /**
     *
     * @param arg Any parameters from the system taken to load the
     */
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("piazza_panic");
		new Lwjgl3Application(new PiazzaPanicGame(), config);
	}
}
