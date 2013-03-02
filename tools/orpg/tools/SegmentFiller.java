package orpg.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import orpg.shared.data.Segment;
import orpg.shared.net.InputByteBuffer;
import orpg.shared.net.OutputByteBuffer;

public class SegmentFiller {

	public static void main(String... args) {
		if (args.length != 2) {
			System.out
					.println("Invalid usage. Arguments required: [path-to-segment] [tile-to-fill].");
			System.exit(1);
		}

		String segmentPath = args[0];
		short tileToFill = Short.parseShort(args[1]);

		InputByteBuffer in = null;
		try {
			in = new InputByteBuffer(new File(segmentPath));
		} catch (IOException e) {
			System.out
					.println("Could not load segment file. " + e.getMessage());
			System.exit(1);
		}

		Segment segment = in.getSegment();

		for (int x = 0; x < segment.getWidth(); x++) {
			for (int y = 0; y < segment.getHeight(); y++) {
				segment.getTiles()[0][x][y] = tileToFill;
			}
		}

		OutputByteBuffer out = new OutputByteBuffer();
		out.putSegment(segment);

		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(new File(segmentPath));
			fileOut.write(out.getBytes());
		} catch (IOException e) {
			System.out
					.println("Could not save segment file. " + e.getMessage());
			System.exit(1);
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
