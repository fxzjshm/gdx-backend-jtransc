package com.jtransc.media.limelibgdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Clipboard;
import com.jtransc.JTranscSystem;
import com.jtransc.annotation.JTranscAddFile;
import com.jtransc.annotation.JTranscMethodBody;
import com.jtransc.annotation.JTranscNativeClass;
import com.jtransc.annotation.haxe.*;
import com.jtransc.io.JTranscIoTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@HaxeAddFilesTemplate({
	"HaxeLimeGdxApplication.hx"
})
@HaxeAddFilesBeforeBuildTemplate({
	"program.xml"
})
@HaxeCustomMain("" +
	"package {{ entryPointPackage }};\n" +
	"class {{ entryPointSimpleName }} extends HaxeLimeGdxApplication {\n" +
	"    public function new() {\n" +
	"        super();\n" +
	"        {{ inits }}\n" +
	"        {{ mainClass }}.{{ mainMethod }}(HaxeNatives.strArray(HaxeNatives.args()));\n" +
	"    }\n" +
	"}\n"
)
@HaxeAddSubtargetList({
	@HaxeAddSubtarget(name = "android"),
	@HaxeAddSubtarget(name = "blackberry"),
	@HaxeAddSubtarget(name = "desktop"),
	@HaxeAddSubtarget(name = "emscripten"),
	@HaxeAddSubtarget(name = "flash", alias = {"swf", "as3"}),
	@HaxeAddSubtarget(name = "html5", alias = {"js"}),
	@HaxeAddSubtarget(name = "ios"),
	@HaxeAddSubtarget(name = "linux"),
	@HaxeAddSubtarget(name = "mac"),
	@HaxeAddSubtarget(name = "tizen"),
	@HaxeAddSubtarget(name = "tvos"),
	@HaxeAddSubtarget(name = "webos"),
	@HaxeAddSubtarget(name = "windows"),
	@HaxeAddSubtarget(name = "neko")
})
@HaxeCustomBuildCommandLine({
	"@limebuild.cmd"
})
@HaxeCustomBuildAndRunCommandLine({
	"@limetest.cmd"
})
@HaxeAddLibraries({
	"lime:2.9.1"
})
@HaxeAddAssets({
	"com/badlogic/gdx/utils/arial-15.fnt",
	"com/badlogic/gdx/utils/arial-15.png"
})
//@JTranscMethodBody(target = "js", value = "")
@JTranscAddFile(target = "js", priority = -3000, process = true, prepend = "js/libgdx.js")
public class LimeApplication extends GdxApplicationAdapter implements Application {
	static private final boolean TRACE = false;

	public LimeApplication(ApplicationListener applicationListener, String title, int width, int height) {
		super(applicationListener);
		setApplicationToLime(this);
		referenceClasses();
	}

	@Override
	protected LimeNet createNet() {
		return new LimeNet();
	}

	@Override
	protected LimeFiles createFiles() {
		return new LimeFiles();
	}

	@Override
	protected LimeInput createInput() {
		return new LimeInput();
	}

	@Override
	protected LimeGraphics createGraphics() {
		return new LimeGraphics(TRACE);
	}

	// @TODO: mark package to include!
	static private void referenceClasses() {
		//new com.badlogic.gdx.graphics.g2d.BitmapFont();
		new com.badlogic.gdx.graphics.Color(0xFFFFFFFF);
		new com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable();
		new com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle();
		new com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle();
		new GlyphLayout.GlyphRun();
	}

	@HaxeMethodBody("HaxeLimeGdxApplication.app = p0;")
	@JTranscMethodBody(target = "js", value = "app = p0;")
	private void setApplicationToLime(LimeApplication app) {
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	protected Preferences createPreferences(String name) {
		// @TODO: node.js support
		return new GdxPreferencesAdapter(name, false) {
			private File getFile(String name) {
				return new File(name + ".prefs");
			}

			@Override
			@HaxeMethodBody(target = "js", value = "trace('localStorage.getItem:' + p0); return N.str(js.Browser.window.localStorage.getItem(p0._str));")
			protected String loadString(String name) throws IOException {
				//return new String(JTranscIoTools.readFile(getFile(name)), "utf-8");
				return super.loadString(name);
			}

			@Override
			@HaxeMethodBody(target = "js", value = "trace('localStorage.setItem:' + p0 + ':' + p1); js.Browser.window.localStorage.setItem(p0._str, p1._str);")
			protected void storeString(String name, String prefs) throws IOException {
				//writeFile(getFile(name), prefs.getBytes("utf-8"));
				super.storeString(name, prefs);
			}

			@Override
			public void flush() {
				System.out.println("GdxPreferencesAdapter.flush();");
				super.flush();
			}
		};
	}

	@Override
	protected Clipboard createClipboard() {
		if (JTranscSystem.usingJTransc()) {
			return new Clipboard() {
				private String content = "";

				@Override
				@HaxeMethodBody("return HaxeNatives.str(lime.system.Clipboard.text);")
				native public String getContents();

				@Override
				@HaxeMethodBody("lime.system.Clipboard.text = p0._str;")
				native public void setContents(String content);
			};
		} else {
			try {
				return (com.badlogic.gdx.utils.Clipboard) Class.forName("com.jtransc.media.limelibgdx.LimeApplicationAwtUtils$AwtClipboardAdaptor").newInstance();
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
	}

	@Override
	protected Audio createAudio() {
		return new LimeAudio();
	}

	@SuppressWarnings("unused")
	public void create() {
		super.create();
		resized(HaxeLimeGdxApplication.instance.getWidth(), HaxeLimeGdxApplication.instance.getHeight());
	}

	@SuppressWarnings("unused")
	public void render() {
		LimeInput.lime_frame();
		super.render();
		if (Gdx.gl instanceof GL20Ext) {
			((GL20Ext)Gdx.gl).present();
		}

	}

	@SuppressWarnings("unused")
	public void resized(int width, int height) {
		super.resized(width, height);
	}

	@JTranscNativeClass("HaxeLimeGdxApplication")
	static public class HaxeLimeGdxApplication {
		static public HaxeLimeGdxApplication instance;

		native public int getWidth();

		native public int getHeight();
	}

	@Override
	public void onResumed() {
		super.onResumed();
	}

	@Override
	public void onPaused() {
		super.onPaused();
	}

	@Override
	public void onDisposed() {
		super.onDisposed();
	}

	@Override
	protected ApplicationType createApplicationType() {
		return LimeDevice.getType();
	}
}
