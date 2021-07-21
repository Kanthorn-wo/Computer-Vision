import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class Grayscale_to_Binary {

	public Grayscale_to_Binary() {
		super();
		BufferedImage image = null;

		try {

			File file = new File("original_image.jpg");
			image = ImageIO.read(file);
		} catch (IOException e) {

			System.err.println("Cannot open the file.");
		}

		BufferedImage image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		int rgb[][] = new int[image.getWidth()][image.getHeight()];

		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				rgb[i][j] = averageRGB(image.getRGB(i, j));
			}

		float histogram[] = new float[256];
		float wb = 0, ub = 0, wf = 0, uf = 0;
		float threshold[] = new float[256];
		float use_threshold[] = new float[256];

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				histogram[rgb[i][j]]++;

				System.out.println("Graph histogram " + "[" + (i) + "]" + "[" + (j) + "] :" + histogram[rgb[i][j]]);
				System.out.println("Gray scale " + "[" + (i) + "]" + "[" + (j) + "] :" + rgb[i][j]);

			}

		}
		System.out.println("Width: " + image.getWidth());
		System.out.println("Height: " + image.getHeight());

		for (int i = 0; i < histogram.length; i++) {

			for (int j = 0; j < i; j++) {
				wb = wb + histogram[j];
				ub = ub + (j * histogram[j]);

			}

			if (wb == 0 || ub == 0) {
				ub = 0;
			} else
				ub = ub / wb;

			wb = wb / (image.getHeight() * image.getWidth());

			for (int k = i; k < histogram.length; k++) {
				wf = wf + histogram[k];
				uf = uf + (k * histogram[k]);
			}

			if (wf == 0 || uf == 0)
				uf = 0;
			else
				uf = uf / wf;

			wf = wf / (image.getHeight() * image.getWidth());

			threshold[i] = (wb * wf) * ((ub - uf) * (ub - uf));
			use_threshold[i] = threshold[i];

			wb = 0;
			ub = 0;
			wf = 0;
			ub = 0;
		}

		Arrays.sort(use_threshold);
		int n = 0;
		while (threshold[n] != use_threshold[use_threshold.length - 1]) {
			n++;
		}
		System.out.println("threshold :" + n);

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				if (rgb[i][j] < n)
					rgb[i][j] = 0;
				else
					rgb[i][j] = 255;
			}
		}

		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				image2.setRGB(i, j, averageRGB2(rgb[i][j]));
			}

		try {
			writeImage(image2);
		} catch (ImagingOpException e) {
			e.printStackTrace();
		}

	}

	public int averageRGB(int rgb) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = (rgb >> 0) & 0xff;

		int nIntensity = (int) (r + g + b) / 3;
		r = g = b = nIntensity;
		return nIntensity;
	}

	public int averageRGB2(int rgb) {

		int r = (rgb & 0xff) << 16;
		int g = (rgb & 0xff) << 8;
		int b = (rgb & 0xff);

		int nIntensity = (int) (r + g + b);
		r = g = b = nIntensity;
		return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);

	}

	public void writeImage(BufferedImage img) {
		File f = new File("render.jpg");
		try {
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Grayscale_to_Binary();
	}
}