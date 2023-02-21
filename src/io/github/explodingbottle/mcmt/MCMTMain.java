/*
 * MCMT - A tool that allows you to modify Minecraft Classic
 * Copyright (C) 2021  ExplodingBottle
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

package io.github.explodingbottle.mcmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import io.github.explodingbottle.explodingau.ExplodingAULib;
import io.github.explodingbottle.mcmt.config.ConfigurationObject;
import io.github.explodingbottle.mcmt.config.ConfigurationProvider;
import io.github.explodingbottle.mcmt.managers.FilesManager;
import io.github.explodingbottle.mcmt.utils.LibrariesUtils;
import io.github.explodingbottle.mcmt.utils.NetworkUtils;
import io.github.explodingbottle.mcmt.utils.OperatingSystem;
import io.github.explodingbottle.mcmt.utils.OperatingSystemUtils;
import io.github.explodingbottle.mcmt.utils.ZipUtils;

public class MCMTMain {

	public static ConfigurationObject configuration;
	public static FilesManager filesManager;

	public static void main(String[] args) {
		Logger mainLogger = Logger.getLogger(MCMTMain.class.getName());
		mainLogger.log(Level.INFO, "Logger initialized !");
		mainLogger.log(Level.INFO, "MCMT - Version " + MCMTVersion.MCMT_VERSION);

		// ExplodingAULib Integration
		mainLogger.log(Level.INFO, "Querying ExplodingAU...");
		ExplodingAULib.standardProgramRoutine("MCMT");
		mainLogger.log(Level.INFO, "ExplodingAU queried.");

		OperatingSystem os = OperatingSystemUtils.getOperatingSystem();
		mainLogger.log(Level.INFO, "Operating system is " + os.toString());
		if (os == OperatingSystem.UNKNOWN) {
			mainLogger.log(Level.SEVERE, "Unknown operating system.");
			return;
		}
		mainLogger.log(Level.INFO, "Loading configuration...");
		try {
			configuration = ConfigurationProvider.loadConfiguration();
		} catch (IOException e) {
			mainLogger.log(Level.SEVERE, "Failed to load configuration.", e);
			return;
		}
		mainLogger.log(Level.INFO, "Configuration loaded !");

		mainLogger.log(Level.INFO, "Initializing files...");
		filesManager = new FilesManager();
		boolean resultCreation = filesManager.createFolders();
		if (!resultCreation) {
			mainLogger.log(Level.SEVERE, "Failed to create folders.");
			return;
		}

		boolean checkResult = filesManager.requiredFilesPresent();
		if (!checkResult) {
			mainLogger.log(Level.SEVERE, "Required files not installed.");
			return;
		}

		mainLogger.log(Level.INFO, "Files were initialized !");

		if (args.length != 1) {
			mainLogger.log(Level.SEVERE, "Please use an option.");
			return;
		}
		switch (args[0]) {
		case "decompile":
			mainLogger.log(Level.INFO, "Client will be downloaded from " + configuration.clientDownloadUrl);

			try {
				NetworkUtils.downloadFile(filesManager.binariesPath, configuration.clientDownloadUrl);
			} catch (IOException e) {
				mainLogger.log(Level.SEVERE, "Failed to download binaries.", e);
				return;
			}

			mainLogger.log(Level.INFO, "Downloading libraries...");
			String libsForDecompiler = "";

			try {
				switch (os) {
				case WINDOWS:
					if (configuration.fernflowerSupport)
						libsForDecompiler = LibrariesUtils.downloadLibraries(configuration.librariesWindows);
					else
						LibrariesUtils.downloadLibraries(configuration.librariesWindows);
					break;
				case MAC:
					if (configuration.fernflowerSupport)
						libsForDecompiler = LibrariesUtils.downloadLibraries(configuration.librariesMac);
					else
						LibrariesUtils.downloadLibraries(configuration.librariesMac);
					break;
				case LINUX:
					if (configuration.fernflowerSupport)
						libsForDecompiler = LibrariesUtils.downloadLibraries(configuration.librariesLinux);
					else
						LibrariesUtils.downloadLibraries(configuration.librariesLinux);
					break;

				default:
					mainLogger.log(Level.SEVERE, "Unknown operating system.");
					return;
				}

			}

			catch (IOException e1) {
				mainLogger.log(Level.SEVERE, "Failed to download libraries.", e1);
				return;
			}
			mainLogger.log(Level.INFO, "Libraries downloaded !");

			mainLogger.log(Level.INFO, "Downloading natives...");
			List<String> natives;
			try {
				switch (os) {
				case WINDOWS:
					natives = LibrariesUtils.downloadNatives(configuration.nativesWindows);
					break;
				case MAC:
					natives = LibrariesUtils.downloadNatives(configuration.nativesMac);
					break;
				case LINUX:
					natives = LibrariesUtils.downloadNatives(configuration.nativesLinux);
					break;
				default:
					mainLogger.log(Level.SEVERE, "Unknown operating system.");
					return;
				}

			} catch (IOException e1) {
				mainLogger.log(Level.SEVERE, "Failed to download natives.", e1);
				return;
			}
			natives.forEach(curNative -> {
				try {
					ZipUtils.unzip(filesManager.nativesFolder, new File(filesManager.nativesFolder, curNative));
				} catch (IOException e) {
					mainLogger.log(Level.SEVERE, "Failed to extract natives.", e);
					System.exit(0);
					;
				}
			});

			mainLogger.log(Level.INFO, "Natives downloaded !");

			mainLogger.log(Level.INFO, "Will decompile to " + filesManager.binariesPath);

			String execCommand = "java -jar " + filesManager.decompilerPath + " " + libsForDecompiler
					+ configuration.decompilerOptions.replace("${binaries}", filesManager.binariesPath.getPath())
							.replace("${sources}", filesManager.sourcesPath.getPath());

			mainLogger.log(Level.INFO, "Will use command " + execCommand);

			mainLogger.log(Level.INFO, "Output from decompiler will be redirected to console.");

			ProcessBuilder procB = new ProcessBuilder(execCommand.split(" "));
			procB.inheritIO();
			int decompResultCode;
			try {
				Process proc = procB.start();
				decompResultCode = proc.waitFor();
			} catch (IOException e) {
				mainLogger.log(Level.SEVERE, "Failed to launch decompiler.", e);
				return;
			} catch (InterruptedException e) {
				mainLogger.log(Level.SEVERE, "We are interrupted.", e);
				return;
			}
			if (decompResultCode != 0) {
				mainLogger.log(Level.SEVERE,
						"Decompilation finished with an exit code other than zero, " + decompResultCode);
				return;
			}
			mainLogger.log(Level.INFO, "Finished decompilation !");

			mainLogger.log(Level.INFO, "Extracting sources...");
			try {
				ZipUtils.unzip(filesManager.eclipseSrc,
						new File(filesManager.sourcesPath, filesManager.binariesPath.getName()));
			} catch (IOException e) {
				mainLogger.log(Level.SEVERE, "Failed to unzip.", e);
				return;
			}
			mainLogger.log(Level.INFO, "Sources extracted !");
			mainLogger.log(Level.INFO, "Patching source...");
			Path patchDir = Paths.get(filesManager.patchesFolder.toURI());
			try {
				Files.walkFileTree(patchDir, new FileVisitor<Path>() {

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						if (!file.toFile().getName().endsWith(".patch")) {
							mainLogger.log(Level.WARNING, "Patches without the .patch extension are ignored.");
							return FileVisitResult.CONTINUE;
						}
						File target = new File(filesManager.eclipseSrc,
								patchDir.relativize(file).toFile().getPath().replace(".patch", ""));
						mainLogger.log(Level.INFO, "Patching " + target.getPath() + "...");
						Patch<String> patch = DiffUtils
								.parseUnifiedDiff(Files.readAllLines(file, Charset.forName("UTF-8")));
						try {
							linesToFile(target.getPath(), DiffUtils.patch(
									Files.readAllLines(Paths.get(target.toURI()), Charset.forName("UTF-8")), patch));
						} catch (PatchFailedException e) {
							mainLogger.log(Level.WARNING, "Failed to patch.", e);
						} catch (IOException e) {
							mainLogger.log(Level.WARNING, "Failed to open patch or file.", e);
						}
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
						mainLogger.log(Level.WARNING, "Failed to visit file " + file.toFile().getPath());
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				mainLogger.log(Level.SEVERE, "Failed to browse patches.", e);
				return;
			}

			mainLogger.log(Level.INFO, "Source patched !");

			mainLogger.log(Level.INFO, "Injecting additions...");
			try {
				Files.walkFileTree(Paths.get(filesManager.additionsFolder.toURI()), new FileVisitor<Path>() {

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Path target = Paths.get(new File(filesManager.eclipseFolder,
								Paths.get(filesManager.additionsFolder.toURI()).relativize(file).toFile().getPath())
										.toURI());
						target.toFile().getParentFile().mkdirs();
						Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				mainLogger.log(Level.SEVERE, "Failed to inject additions.", e);
				return;
			}
			mainLogger.log(Level.INFO, "Additions injected !");

			break;
		case "cleanup":
			mainLogger.log(Level.INFO, "Cleaning up...");
			FilesManager.recursiveDelete(filesManager.sourcesPath);
			FilesManager.recursiveDelete(filesManager.eclipseFolder);
			FilesManager.recursiveDelete(filesManager.nativesFolder);
			FilesManager.recursiveDelete(filesManager.librariesFolder);
			FilesManager.recursiveDelete(filesManager.binariesPath.getParentFile());
			mainLogger.log(Level.INFO, "Cleaned up !");
			break;
		default:
			mainLogger.log(Level.SEVERE, "Please use a valid option. Options: decompile / cleanup");
			return;
		}

		mainLogger.log(Level.INFO, "Job finished ! Happy modding !");

	}

	private static void linesToFile(String filename, List<String> linesToWrite) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		for (String line : linesToWrite) {
			out.write(line);
			out.newLine();
		}
		out.close();
	}
}
