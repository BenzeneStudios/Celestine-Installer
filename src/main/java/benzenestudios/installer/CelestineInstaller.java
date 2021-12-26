package benzenestudios.installer;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

public class CelestineInstaller {
	private static JTextField minecraftLocation;
	private static JTextField celestineLocation;

	public static void main(String[] args) {
		FlatDarkLaf.setup();

		try {
			JFrame frame = new JFrame();
			frame.setTitle("Celestine Installer");
			frame.setIconImages(Arrays.asList(
					getIncludedImage("celestine_32.png"),
					getIncludedImage("celestine_16.png")
			));

			frame.add(createPanel());
			frame.pack();
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) {
			displayCrashWindow(e, "Exception running Celestine Installer!");
		}
	}

	private static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(500, 300));

		// add fields
		Panel fields = new Panel(new FlowLayout(FlowLayout.CENTER));
		(minecraftLocation = new JTextField()).setText(findDefaultInstallDir("minecraft").toString());
		(celestineLocation = new JTextField()).setText(findDefaultInstallDir("celestine").toString());

		fields.add(minecraftLocation);
		fields.add(celestineLocation);
		fields.setPreferredSize(new Dimension(500, 250));
		panel.add(fields, BorderLayout.CENTER);

		// add button
		Button install = new Button("Install");
		panel.add(install, BorderLayout.SOUTH);

		return panel;
	}

	private static BufferedImage getIncludedImage(String location) throws IOException {
		return ImageIO.read(CelestineInstaller.class.getClassLoader().getResource(location));
	}

	/**
	 * Adapted from my private "Swing Game" repository, with permission from myself.
	 * @author Valoeghese
	 */
	public static void displayCrashWindow(Exception exception, String title) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());

		JTextArea errorText = new JTextArea();
		errorText.setBorder(new BevelBorder(BevelBorder.LOWERED));
		errorText.append("Please report the following stack trace to https://github.com/BenzeneStudios/Celestine-Installer/issues:\n");

		// The print writer has been inlined from the original code, which had it as a separate class
		exception.printStackTrace(new PrintWriter(new NoneOutputStream()) {
			@Override
			public void write(String str) {
				errorText.append(str + "\n");
			}
		});
		panel.add(errorText, BorderLayout.CENTER);

		frame.add(panel);
		frame.pack();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 400);
		frame.setVisible(true);
	}

	/*
	 * Adapted from code at https://github.com/FabricMC/fabric-installer
	 * Original license has been preserved for this method.
	 *
	 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *     http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
	public static Path findDefaultInstallDir(String application) {
		String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		Path dir;

		if (os.contains("win") && System.getenv("APPDATA") != null) {
			dir = Paths.get(System.getenv("APPDATA")).resolve("." + application);
		} else {
			String home = System.getProperty("user.home", ".");
			Path homeDir = Paths.get(home);

			if (os.contains("mac")) {
				dir = homeDir.resolve("Library").resolve("Application Support").resolve(application);
			} else {
				dir = homeDir.resolve("." + application);
			}
		}

		return dir.toAbsolutePath().normalize();
	}
}
