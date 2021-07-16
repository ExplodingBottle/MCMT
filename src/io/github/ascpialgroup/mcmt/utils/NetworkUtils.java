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
