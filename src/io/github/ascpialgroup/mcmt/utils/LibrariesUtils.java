package io.github.ascpialgroup.mcmt.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.ascpialgroup.mcmt.MCMTMain;

public class LibrariesUtils {
	public static String downloadLibraries(String libsInfo) throws IOException {
		String[] libraries = libsInfo.split(";");
		String decompilerLibs = "";
		for (String libCouple : libraries) {
			String[] splitedLib = libCouple.split("\\|");
			String libUrl = splitedLib[0];
			String libPath = splitedLib[1];
			decompilerLibs += "-e=" + new File(MCMTMain.configuration.librariesPath, libPath).getPath() + " ";
			NetworkUtils.downloadFile(new File(MCMTMain.configuration.librariesPath, libPath), libUrl);
		}

		return decompilerLibs;
	}

	public static List<String> downloadNatives(String nativesInfo) throws IOException {
		String[] libraries = nativesInfo.split(";");
		ArrayList<String> toReturn = new ArrayList<>();
		for (String libCouple : libraries) {
			String[] splitedLib = libCouple.split("\\|");
			String libUrl = splitedLib[0];
			String libPath = splitedLib[1];
			toReturn.add(libPath);
			NetworkUtils.downloadFile(new File(MCMTMain.configuration.nativesPath, libPath), libUrl);
		}
		return toReturn;

	}
}
