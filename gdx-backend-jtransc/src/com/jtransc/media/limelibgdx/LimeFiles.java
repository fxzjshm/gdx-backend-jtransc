package com.jtransc.media.limelibgdx;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.jtransc.JTranscWrapped;
import com.jtransc.annotation.JTranscMethodBody;
import com.jtransc.annotation.haxe.HaxeMethodBody;
import com.jtransc.io.JTranscSyncIO;

import java.io.File;
import java.io.FileNotFoundException;

public class LimeFiles implements Files {
	public LimeFiles() {
		JTranscSyncIO.impl = new JTranscSyncIOLimeImpl(JTranscSyncIO.impl);
	}

	@Override
	public FileHandle getFileHandle(String s, FileType fileType) {
		return new MyFileHandle(s);
	}

	@Override
	public FileHandle classpath(String s) {
		return new MyFileHandle(s);
	}

	@Override
	public FileHandle internal(String s) {
		return new MyFileHandle(s);
	}

	@Override
	public FileHandle external(String s) {
		return new MyFileHandle(s);
	}

	@Override
	public FileHandle absolute(String s) {
		return new MyFileHandle(s);
	}

	@Override
	public FileHandle local(String s) {
		return new MyFileHandle(s);
	}

	@Override
	public String getExternalStoragePath() {
		return "/";
	}

	@Override
	public boolean isExternalStorageAvailable() {
		return false;
	}

	@Override
	public String getLocalStoragePath() {
		return "/";
	}

	@Override
	public boolean isLocalStorageAvailable() {
		return false;
	}

	static private class MyFileHandle extends FileHandle {
		public MyFileHandle(String s) {
			super(s);
			System.out.println("MyFileHandle: " + s);
		}
	}

	static public String fixpath(String path) {
		while (path.startsWith("/")) path = path.substring(1);
		if (!path.startsWith("assets") && !path.startsWith("/assets")) {
			path = "assets/" + path;
		}
		path = path.replace("\\", "/").replace("//", "/");
		return path;
	}

	static private class JTranscSyncIOLimeImpl extends JTranscSyncIO.Impl {
		public JTranscSyncIOLimeImpl(JTranscSyncIO.Impl parent) {
			super(parent);
		}

		@Override
		public JTranscSyncIO.ImplStream open(String path, int mode) {
			String pathFixed = fixpath(path);
			System.out.println("JTranscSyncIOLimeImpl.open: " + pathFixed + " || " + path);
			byte[] bytes = readBytes(pathFixed, mode);
			if (bytes == null) {
				System.out.println("Can't find: " + pathFixed);
				throw new RuntimeException(new FileNotFoundException(path));
			}
			return new JTranscSyncIO.ByteStream(bytes);
		}

		@Override
		public String getCwd() {
			return "";
		}

		@Override
		public int getBooleanAttributes(String path) {
			String pathFixed = fixpath(path);
			System.out.println("JTranscSyncIOLimeImpl.getBooleanAttributes: " + pathFixed + " || " + path);
			int result = 0;
			result |= BA_REGULAR;
			if (exists(pathFixed)) result |= BA_EXISTS;
			return result;
		}

		static private final int BA_EXISTS    = 0x01;
		static private final int BA_REGULAR   = 0x02;
		static private final int BA_DIRECTORY = 0x04;
		static private final int BA_HIDDEN    = 0x08;

		@HaxeMethodBody("return lime.Assets.exists(p0._str);")
		@JTranscMethodBody(target = "js", value = "return libgdx.io.exists(N.istr(p0));")
		native private boolean exists(String path);

		@HaxeMethodBody("return JA_B.fromBytes(lime.Assets.getBytes(p0._str));")
		@JTranscMethodBody(target = "js", value = "return libgdx.io.readBytes(N.istr(p0));")
		native private byte[] readBytes(String path, int mode);
	}
}
