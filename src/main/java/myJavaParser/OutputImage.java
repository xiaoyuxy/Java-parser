package myJavaParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
/**
* Helper class to download image from an url
* Given CompilationUnit cu, get Attribute list
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class OutputImage {
	public OutputImage(String imageUrl,String destinationFile) throws IOException {
		System.out.print(imageUrl + " " + destinationFile);
		URL url = new URL(imageUrl);
		InputStream in = url.openStream();
		OutputStream out = new FileOutputStream(destinationFile);
		byte[] b = new byte[2048];
		int length;
		while ((length = in.read(b)) != -1) {
			out.write(b, 0, length);
		}
		in.close();
		out.close();
	}
}
