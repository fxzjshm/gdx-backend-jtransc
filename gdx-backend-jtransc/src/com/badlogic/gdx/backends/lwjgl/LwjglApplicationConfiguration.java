package com.badlogic.gdx.backends.lwjgl;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.jtransc.media.limelibgdx.LimeGraphics;

public class LwjglApplicationConfiguration {
	static public boolean disableAudio;
	public boolean useGL30 = false;
	public int gles30ContextMajorVersion = 3;
	public int gles30ContextMinorVersion = 2;

	public int r = 8, g = 8, b = 8, a = 8;
	public int depth = 16, stencil = 0;
	public int samples = 0;
	public int width = 640, height = 480;
	public int x = -1, y = -1;
	public boolean fullscreen = false;
	public int overrideDensity = -1;
	public boolean vSyncEnabled = true;
	public String title;
	public boolean forceExit = true;
	public boolean resizable = true;
	public int audioDeviceSimultaneousSources = 16;
	public int audioDeviceBufferSize = 512;
	public int audioDeviceBufferCount = 9;
	public Color initialBackgroundColor = Color.BLACK;
	public int foregroundFPS = 60;
	public int backgroundFPS = 60;
	public boolean allowSoftwareMode = false;
	public String preferencesDirectory = ".prefs/";
	public Files.FileType preferencesFileType = Files.FileType.External;
	public boolean useHDPI = false;

	Array<String> iconPaths = new Array();
	Array<Files.FileType> iconFileTypes = new Array();

	public void addIcon(String path, Files.FileType fileType) {
		iconPaths.add(path);
		iconFileTypes.add(fileType);
	}

	public void setFromDisplayMode(Graphics.DisplayMode mode) {
		this.width = mode.width;
		this.height = mode.height;
		if (mode.bitsPerPixel == 16) {
			this.r = 5;
			this.g = 6;
			this.b = 5;
			this.a = 0;
		}
		if (mode.bitsPerPixel == 24) {
			this.r = 8;
			this.g = 8;
			this.b = 8;
			this.a = 0;
		}
		if (mode.bitsPerPixel == 32) {
			this.r = 8;
			this.g = 8;
			this.b = 8;
			this.a = 8;
		}
		this.fullscreen = true;
	}

	public static Graphics.DisplayMode getDesktopDisplayMode() {
		return new LimeGraphics.DisplayMode2(640, 480, 60, 32);
	}

	public static Graphics.DisplayMode[] getDisplayModes() {
		return new Graphics.DisplayMode[]{getDesktopDisplayMode()};
	}
}
