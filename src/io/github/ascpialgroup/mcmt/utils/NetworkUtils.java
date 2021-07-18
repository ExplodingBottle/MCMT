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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils {
	public static void downloadFile(File where, String from) throws IOException {
		
		URL url = new URL(from);
		URLConnection connection = url.openConnection();
		InputStream input = connection.getInputStream();
		FileOutputStream output = new FileOutputStream(where);

		byte[] buffer = new byte[2048];
		int readed;

		while ((readed = input.read(buffer, 0, buffer.length)) != -1) {
			output.write(buffer, 0, readed);
		}

		input.close();
		output.close();
	}
}
