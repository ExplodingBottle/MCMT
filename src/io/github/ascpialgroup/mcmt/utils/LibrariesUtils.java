/*
 * Copyright (C) 2021  ASCPIAL Group
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
