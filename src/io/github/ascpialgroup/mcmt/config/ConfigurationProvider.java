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

package io.github.ascpialgroup.mcmt.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationProvider {
	private static final String CONFIGURATION_PATH = "mcmt.cfg";

	public static ConfigurationObject loadConfiguration() throws IOException {
		ConfigurationObject toReturn = new ConfigurationObject();
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(CONFIGURATION_PATH);
		props.load(fis);
		fis.close();
		toReturn.clientDownloadUrl = props.getProperty("client-download-url");
		toReturn.binariesPath = props.getProperty("binaries-path");
		toReturn.sourcesPath = props.getProperty("sources-directory");
		toReturn.decompilerPath = props.getProperty("decompiler-path");
		toReturn.decompilerOptions = props.getProperty("decompiler-arguments");
		toReturn.librariesWindows = props.getProperty("w-libraries-url");
		toReturn.librariesMac = props.getProperty("m-libraries-url");
		toReturn.librariesLinux = props.getProperty("l-libraries-url");
		toReturn.librariesPath = props.getProperty("libraries-folder");
		toReturn.fernflowerSupport = props.getProperty("fernflower-support").equals("true") ? true : false;
		toReturn.eclipseFolder = props.getProperty("eclipse-folder");
		toReturn.nativesWindows = props.getProperty("w-natives-url");
		toReturn.nativesMac = props.getProperty("m-natives-url");
		toReturn.nativesLinux = props.getProperty("l-natives-url");
		toReturn.nativesPath = props.getProperty("natives-folder");
		toReturn.patchPath = props.getProperty("patches-folder");
		toReturn.additionsPath = props.getProperty("additions-path");
		return toReturn;
	}
}
