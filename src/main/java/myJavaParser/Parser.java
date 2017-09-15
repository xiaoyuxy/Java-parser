 package myJavaParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import myJavaParser.InterfacePrinter.ClassOrInterface;
import myJavaParser.RelatedClass.Relation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Parser {
	public static void main(String[] args) throws Exception{
		if(args.length == 2){
			String path = args[0];
			String yUML = "http://yuml.me/diagram/scruffy/class/";
			String uRL =yUML.concat( umlParser(path));
			@SuppressWarnings("unused")
			OutputImage image = new OutputImage(uRL, args[1]);
		}
		else
			System.out.println("Your command is wrong");
	}

	public static String umlParser(String path) throws Exception{
		final File folder = new File(path); //"/Users/kaichen/Code/cmpe202/javacode/uml-parser-test-1"
		
		ArrayList<String> files = ListFilesForFolder.listFilesForFolder(folder);
	
		RelatedClass rc = new RelatedClass();
		for(String file : files){
			if(file.endsWith(".java")){
				FileInputStream in = new FileInputStream(file);
				CompilationUnit cu = JavaParser.parse(in);
				rc.prepareData(cu);				
			}
		}
		// remove the duplication in relationList
		for(int i=0;i<rc.relationList.size();i++){
			for(int j=i+1;j<rc.relationList.size();j++){
				if(rc.relationList.get(i).equals(rc.relationList.get(j))){
					rc.relationList.remove(j);
				}			
			}
		}
		ArrayList<ArrayList<ClassOrInterface>> globalInterfaceList = rc.globalInterfaceList(files);
		ArrayList<String> resultList= new ArrayList<String>();
		for(Relation rel : rc.relationList){		
			String result = rc.map.get(rel.class1);
			if(rel.related == 0){//1 to many
				result += "1-0..*";
				result += rc.map.get(rel.class2);
			}
			if(rel.related == 1){//1 to 1
				result += "-1";
				result += rc.map.get(rel.class2);
			}
			if(rel.related == 2){// class or interface without relation 
				result ="";
			}				
			if(rel.related == 3){//inherit
				result +="-^";
				result += rc.map.get(rel.class2);
			}
			if(rel.related == 4){
				result += "-.-^";					
				result += rc.map.get(rel.class2);
			}
			if(rel.related == 5){// only when class2 is interfaceList 
				if(rc.map.get(rel.class2) != null){
					for(ArrayList<ClassOrInterface> interfaceList:globalInterfaceList){
						for(ClassOrInterface inter:interfaceList){
							if(inter.Name.equals(rel.class2)){
								result += "uses -.->";
								result += rc.map.get(rel.class2);
								break;
							}
						}
					}
				}
				else{
					result ="";
				}
			}
			if(rel.related == 6){
				result +="uses -.->";
				result += rc.map.get(rel.class2);
			}
			if(!result.isEmpty() && !result.equals(rc.map.get(rel.class1)) ){
				//System.out.print(result);
				String r = result;
				resultList.add(r);
			}
		}
		return resultList.toString();
		//System.out.print(resultList.toString());
	}
}



