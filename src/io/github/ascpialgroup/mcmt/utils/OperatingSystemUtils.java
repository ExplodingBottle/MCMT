package io.github.ascpialgroup.mcmt.utils;

public class OperatingSystemUtils {
	public static OperatingSystem getOperatingSystem() {
		String toCheck = System.getProperty("os.name");
		if (toCheck.toLowerCase().contains("win"))
			return OperatingSystem.WINDOWS;
		if (toCheck.toLowerCase().contains("mac"))
			return OperatingSystem.MAC;
		if (toCheck.toLowerCase().contains("linux") || toCheck.toLowerCase().contains("unix"))
			return OperatingSystem.LINUX;
		return OperatingSystem.UNKNOWN;
	}
}
