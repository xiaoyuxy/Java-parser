package myJavaParser;

import java.io.File;
import java.util.ArrayList;
/**
* Helper class to get all file names under a folder
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/

public  class ListFilesForFolder{
	/**
	 * Get files names of files under a folder
	 * @param folder
	 * @return a list of String contains all file names in under the specified folder
	 */
	public static ArrayList<String> listFilesForFolder(final File folder){
		
		ArrayList<String> filelist =new ArrayList<String>();
		String path = "";
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				
				path=fileEntry.getPath().toString();
				filelist.add(path);
	
			}
		}
		//System.out.println(filelist);
		return filelist;
	}
	

}