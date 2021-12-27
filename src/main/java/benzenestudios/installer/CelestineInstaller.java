package benzenestudios.installer;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;

public class CelestineInstaller {
	private static final String VERSION = "0.1.0"; // TODO REMEMBER TO CHANGE THIS EACH VERSION ALONG WITH MAVEN
	private static JTextField minecraftLocation;
	private static JComboBox<String> minecraftVersion;
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

	private static JPanel createPanel() throws IOException {
		String comicSans = findTypeFace(".*Comic.*Sans.*(?i)");

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// add space
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

		JLabel image = new JLabel(new ImageIcon(getIncludedImage("celestine_icon.png")));
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel text = new JLabel("Celestine Installer " + VERSION);
		text.setFont(new Font(comicSans, Font.PLAIN, 20));
		text.setAlignmentX(Component.CENTER_ALIGNMENT);

		header.add(image);
		header.add(text);
		panel.add(header, BorderLayout.NORTH);

		// add fields
		JPanel fields = new JPanel(new GridBagLayout());
		fields.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		JLabel minecraftVersionText = new JLabel("Minecraft Version");
		minecraftVersionText.setFont(new Font(comicSans, Font.PLAIN, 16));

		minecraftVersion = new JComboBox<>(new Vector<>(Arrays.asList("1.17.1")));

		JLabel minecraftLocationText = new JLabel("Minecraft Location");
		//DEBUG: minecraftLocationText.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
		minecraftLocationText.setFont(new Font(comicSans, Font.PLAIN, 16));

		(minecraftLocation = new JTextField()).setText(findDefaultInstallDir("minecraft").toString());

		JLabel celestineLocationText = new JLabel("Profile Location");
		celestineLocationText.setFont(new Font(comicSans, Font.PLAIN, 16));

		(celestineLocation = new JTextField()).setText(findDefaultInstallDir("celestine").toString());

		fields.add(minecraftVersionText, createTextConstraints(0));
		fields.add(minecraftVersion, createInputConstraints(0));
		fields.add(minecraftLocationText, createTextConstraints(1));
		fields.add(minecraftLocation, createInputConstraints(1));
		fields.add(celestineLocationText, createTextConstraints(2));
		fields.add(celestineLocation, createInputConstraints(2));
		fields.setPreferredSize(new Dimension(500, 150));
		panel.add(fields, BorderLayout.CENTER);

		// add button
		JButton install = new JButton("Install");
		install.setPreferredSize(new Dimension(500 - 60, 50));

		JPanel formattedInstall = new JPanel();
		formattedInstall.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));

		formattedInstall.add(install);
		panel.add(formattedInstall, BorderLayout.SOUTH);

		return panel;
	}

	private static Insets TEXT_INSETS = new Insets(5, 0, 5, 5);
	private static Insets INPUT_INSETS = new Insets(5, 5, 5, 0);

	private static GridBagConstraints createTextConstraints(int index) {
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = 0;
		result.gridy = index;
		result.anchor = GridBagConstraints.WEST;
		result.fill = GridBagConstraints.BOTH;
		result.gridwidth = 1;
		result.gridheight = 1;
		result.insets = TEXT_INSETS;
		return result;
	}

	private static GridBagConstraints createInputConstraints(int index) {
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = 1;
		result.gridy = index;
		result.anchor = GridBagConstraints.EAST;
		result.fill = GridBagConstraints.HORIZONTAL;
		result.gridwidth = 1;
		result.gridheight = 1;
		result.insets = INPUT_INSETS;
		return result;
	}


	private static String findTypeFace(String regex) {
		for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH)) {
			if (font.matches(regex)) return font;
		}

		System.out.println("Couldn't find font matching " + regex);
		return regex; // will just revert to default if can't find it
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
