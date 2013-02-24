package orpg.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TilesetSplitter {

	public static void main(String... args) {
		if (args.length != 4) {
			System.out
					.println("Invalid usage. Arguments required: [path-to-image] [output-dir] [tileset-width] [tileset-height].");
			System.exit(1);
		}

		String imagePath = args[0];
		String outputPath = args[1];
		int tilesetWidth = Integer.parseInt(args[2]);
		int tilesetHeight = Integer.parseInt(args[3]);

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			System.out.println("Error occured loading image." + e.getMessage());
			System.exit(1);
		}

		int tileset = 0;
		for (int x = 0; x < (image.getWidth() / tilesetWidth); x++) {
			for (int y = 0; y < (image.getHeight() / tilesetHeight); y++) {
				try {
					ImageIO.write(image.getSubimage(x * tilesetWidth, y
							* tilesetHeight, tilesetWidth, tilesetHeight),
							"png", new File(outputPath + "/tiles_" + tileset
									+ ".png"));
				} catch (IOException e) {
					System.out.println("Error occured saving image."
							+ e.getMessage());
					System.exit(1);
				}
				tileset++;
			}
		}

	}
}
