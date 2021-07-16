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
