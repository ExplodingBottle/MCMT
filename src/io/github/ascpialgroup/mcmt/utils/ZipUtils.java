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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
	public static void unzip(File folder, File zipFile) throws IOException {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry currentEntry = zis.getNextEntry();
		while (currentEntry != null) {
			if (currentEntry.isDirectory()) {
				new File(folder, currentEntry.getName()).mkdirs();
			} else {
				new File(folder, currentEntry.getName()).getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(new File(folder, currentEntry.getName()));
				byte[] buffer = new byte[2048];
				int readed;
				while ((readed = zis.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, readed);
				}
				fos.close();
			}
			currentEntry = zis.getNextEntry();
		}
		zis.close();
	}
}
