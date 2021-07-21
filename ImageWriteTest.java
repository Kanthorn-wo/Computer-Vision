package vision59;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

//import com.sun.image.codec.jpeg.ImageFormatException;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageWriteTest {

/**
 * 
 */

	public ImageWriteTest() {
		super();
		BufferedImage image = null;
		
		try {

			// Read from a file
			File file = new File("salt1.jpg");
			image = ImageIO.read(file);
		} catch (IOException e) {

			System.err.println("Cannot open the file.");
		}

		System.out.println("Width: " + image.getWidth());
		System.out.println("Height: " + image.getHeight());

		//สร้าง BufferedImage ขึ้นมาใหม่สำหรับ Save ภาพให้เป็นไฟล์ใหม่ต้องระบุ size และ type ด้วย
		BufferedImage image2 = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
		
		//สร้าง array สองมิติขึ้นมาเพื่อสำหรับเก็บค่าในการคำนวณ
		int rgb[][] = new int[image.getWidth()][image.getHeight()];
		
		//ดึงค่า RGB ออกมาและทำการแปลงให้อยู่ในรูปของ integer ที่มีค่าช่วง Gray Scale ตั้งแต่ 0-255
		//และค่าใน array ที่ทำการแปลงเป็น gray scale นี้จะสามารถนำไปคำนวณค่าต่าง ๆ ได้ เช่น filter noise
		for(int i=0;i<image.getWidth();i++)
			for(int j=0;j<image.getHeight();j++){
				rgb[i][j] = averageRGB(image.getRGB(i,j));
				System.out.println(rgb[i][j]);
			}
		
		//แปลงค่าจาก integer กลับไปเป็น Hex เพื่อนำกลับไปสร้างภาพใหม่ขึ้นมา
		for(int i=0;i<image.getWidth();i++)
			for(int j=0;j<image.getHeight();j++){
				image2.setRGB(i,j,averageRGB2(rgb[i][j]));
			}
		
		//เรียก method writeImage เพื่อสร้างไฟล์ภาพขึ้นมาใหม่
		try {
			writeImage(image2);
		} catch (ImagingOpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	//แปลงค่า Hex เป็น integer ตังแต่ 0-255
	public int averageRGB(int rgb)
	{
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >>  8) & 0xff;
		int b = (rgb >>  0) & 0xff;
	
		int nIntensity = (int) (r+g+b)/3;
		r = g = b = nIntensity;
		return nIntensity;
	}	
	
	//แปลงค่าจาก integer กลับเป็น Hex เนื่องจากก่อนสร้างภาพขึ้นมาเป็นไฟล์ ค่าที่นำไป setRGB
	//ลงใน BufferedImage จะต้องกลับไปเป็น Hex เหมือนเดิม
	public int averageRGB2(int rgb)
	{

		int r = (rgb  & 0xff ) << 16;
		int g = (rgb  & 0xff) << 8;
		int b = (rgb  & 0xff) ;

		int nIntensity = (int) (r+g+b);
		r = g = b = nIntensity;
		return (rgb & 0xff000000)| (r<<16) | (g<<8) | (b<<0);

	}
	
	//	method นี้สำหรับสร้างไฟล์รูปภาพขึ้นมาใหม่ซึ่งจะสร้างเป็นไฟล์ png เนื่องจาก
	// ไฟล์ png จะได้คุณภาพของภาพดีกว่า ซึ่งถ้า save เป็น jpg จะทำให้ภาพแตก
	public void writeImage(BufferedImage img){
		File f = new File("myimage.png");
	     try {
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	public static void main(String[] args) {
		new ImageWriteTest();
	}
}