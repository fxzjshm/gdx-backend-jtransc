package com.jtransc.media.limelibgdx;

import com.badlogic.gdx.Application;
import com.jtransc.JTranscSystem;
import com.jtransc.annotation.haxe.HaxeMethodBody;

public class LimeDevice {
	@HaxeMethodBody(target = "android", value = "return true;")
	@HaxeMethodBody("return false;")
	static public boolean isAndroid() {
		return false;
	}

	@HaxeMethodBody(target = "android", value = "return true;")
	@HaxeMethodBody("return false;")
	static public boolean isIos() {
		return false;
	}

	static public boolean isJs() {
		return JTranscSystem.isJs();
	}

	static public boolean isFlash() {
		return JTranscSystem.isSwf();
	}

	static public Application.ApplicationType getType() {
		if (LimeDevice.isJs()) {
			return Application.ApplicationType.WebGL;
		} else if (LimeDevice.isIos()) {
			return Application.ApplicationType.iOS;
		} else if (LimeDevice.isAndroid()) {
			return Application.ApplicationType.Android;
		} else {
			return Application.ApplicationType.Desktop;
		}
	}
}
