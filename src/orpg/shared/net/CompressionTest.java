package orpg.shared.net;


//import net.jpountz.lz4.LZ4Compressor;
//import net.jpountz.lz4.LZ4Factory;

public class CompressionTest {

	public static void main(String[] args) {
		/*
		LZ4Compressor compressor = LZ4Factory.fastestJavaInstance().fastCompressor();
		byte[] bytes = new byte[50*50*9*2];
		Random r = new Random();
		
		for (int i = 0; i < bytes.length;i++) {
			
			if (r.nextInt(10) < 3) {
				bytes[i] = (byte)r.nextInt(127);
			}
		}
		
		
		byte[] out;
		long tick = System.currentTimeMillis();
		int b = 0;
		for (int i = 0; i < 1000; i++) {
			out = new byte[compressor.maxCompressedLength(bytes.length)];
			b = compressor.compress(bytes, 0, bytes.length, out, 0);
			
		}
		long diff = System.currentTimeMillis() - tick;
		System.out.println("Total millis: " + diff);
		System.out.println("Ms/iteration: " + diff / 1000);
		System.out.println(b);
		*/
		
	}
	
}
