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

package io.github.ascpialgroup.mcmt.managers;

import java.io.File;

import io.github.ascpialgroup.mcmt.MCMTMain;

public class FilesManager {

	public File sourcesPath;
	public File binariesPath;
	public File decompilerPath;
	public File librariesFolder;
	public File eclipseFolder;
	public File eclipseSrc;
	public File nativesFolder;
	public File patchesFolder;
	public File additionsFolder;
	
	public FilesManager() {
		sourcesPath = new File(MCMTMain.configuration.sourcesPath);
		binariesPath = new File(MCMTMain.configuration.binariesPath);
		decompilerPath = new File(MCMTMain.configuration.decompilerPath);
		librariesFolder = new File(MCMTMain.configuration.librariesPath);
		eclipseFolder = new File(MCMTMain.configuration.eclipseFolder);
		eclipseSrc = new File(eclipseFolder, "src");
		nativesFolder = new File(MCMTMain.configuration.nativesPath);
		patchesFolder = new File(MCMTMain.configuration.patchPath);
		additionsFolder = new File(MCMTMain.configuration.additionsPath);
	}

	public boolean createFolders() {
		boolean result = true;
		result = result & createDirectoryIfNotExisting(sourcesPath);
		result = result & createDirectoryIfNotExisting(librariesFolder);
		result = result & createDirectoryIfNotExisting(binariesPath.getParentFile());
		result = result & createDirectoryIfNotExisting(eclipseSrc);
		result = result & createDirectoryIfNotExisting(nativesFolder);
		return result;
	}

	public boolean requiredFilesPresent() {
		boolean result = true;
		result = result & decompilerPath.exists();
		result = result & patchesFolder.exists();
		result = result & additionsFolder.exists();
		return result;
	}

	private boolean createDirectoryIfNotExisting(File path) {
		if (!path.exists()) {
			return path.mkdirs();
		}
		return true;
	}

	public static void recursiveDelete(File file) {
		if (!file.exists())
			return;

		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				recursiveDelete(f);
			}
		}

		file.delete();
	}
}
