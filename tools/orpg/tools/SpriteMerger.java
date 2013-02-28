package orpg.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteMerger {

	public static void main(String... args) {
		if (args.length != 5) {
			System.out
					.println("Invalid usage. Arguments required: [sprite-dir] [output-path] [total-sprites] [sprite-size] [desired-size].");
			System.exit(1);
		}

		String spriteDir = args[0];
		String outputPath = args[1];
		int totalSpriteFiles = Integer.parseInt(args[2]);
		int spriteWidth = Integer.parseInt(args[3]);
		int desiredWidth = Integer.parseInt(args[4]);
		int spritesWide = desiredWidth / spriteWidth;
		int spritesPerImage = spritesWide * spritesWide;

		if (desiredWidth % spriteWidth != 0) {
			System.out
					.println("Invalid desired size. The desired size must be a multiple of the sprite size.");
			System.exit(1);
		}

		int set = 0;
		BufferedImage image = new BufferedImage(desiredWidth,
				desiredWidth, BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage loadedImage = null;

		for (int i = 0; i < totalSpriteFiles; i++) {

			// Load the sprite and copy it into the correct destination
			try {
				loadedImage = ImageIO.read(new File((i + 1) + ".png"));
			} catch (IOException e1) {
				System.out.println("Could not read image " + (i + 1)
						+ ".png");
				System.exit(1);
			}

			image.getGraphics().drawImage(loadedImage,
					(i % spritesWide) * spriteWidth,
					((i / spritesWide) % spritesWide) * spriteWidth,
					((i % spritesWide) + 1) * spriteWidth,
					(((i / spritesWide) % spritesWide) + 1) * spriteWidth, 0, 0,
					spriteWidth, spriteWidth, null);

			if (i != 0 && i % spritesPerImage == 0) {
				try {
					// Write and then create a new image
					image.getGraphics().dispose();
					ImageIO.write(image, "png", new FileOutputStream(
							outputPath + "/sprite_" + set + ".png"));
					set++;
					image = new BufferedImage(desiredWidth, desiredWidth,
							BufferedImage.TYPE_4BYTE_ABGR);
				} catch (IOException e) {
					System.out.println("Could not save sprite_" + set
							+ ".png");
					System.exit(1);
				}
			}

		}

	}
}
