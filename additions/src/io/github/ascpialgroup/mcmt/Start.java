package io.github.ascpialgroup.mcmt;

import java.lang.reflect.Field;

import org.lwjgl.LWJGLException;

import com.mojang.minecraft.Minecraft;

public class Start {

	public static final String MCMT_STARTER_VERSION = "1.0.0";
	
	public static void main(String[] args) {
		System.out.println("Launching Minecraft " + Minecraft.VERSION_STRING + " trough MCMT starter " + MCMT_STARTER_VERSION + ".");

		try {
			System.setProperty("java.library.path", "../natives");
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (Exception e) {
			System.err.println("Failed to set natives path.");
			return;
		}

		try {
			Minecraft.main(args);
		} catch (LWJGLException e) {
			System.err.println("Failed to start Minecraft " + Minecraft.VERSION_STRING + ".");
			e.printStackTrace();
		}
	}

}
