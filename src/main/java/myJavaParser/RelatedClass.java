package myJavaParser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import myJavaParser.AttributesPrinter.Attribute;
import myJavaParser.ConstructorPrinter.Constructor;
import myJavaParser.InterfacePrinter.ClassOrInterface;
import myJavaParser.MethodPrinter.Method;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

/**
* Get the relation of classes
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class RelatedClass {

	ArrayList<Relation> relationList;
	HashMap<String, String> map;

	public RelatedClass(){
		map = new HashMap<String, String>();
		relationList = new ArrayList<Relation>();
	}
	
	public  class Relation{
		String class1;
		String class2;
		int related;
		@Override
		public boolean equals(Object obj) {
		    if (obj == null) {
		        return false;
		    }
		    Relation newrel = (Relation)obj;
		    if(newrel.class2 == null){
		    	if(this.class2 == null && newrel.class1.equals(this.class1))
		    		return true;
		    }
		    else{
		    if(newrel.class1.equals(this.class1) && newrel.class2.equals(this.class2) && newrel.related == this.related)
		    	return true;
		    }
			return false;
		}
	}

	/**
	 * Get a global list of interface by visiting all files once
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ArrayList<ClassOrInterface>> globalInterfaceList(ArrayList<String> files) throws Exception {
		ArrayList< ArrayList<ClassOrInterface>> globalInterfaceList = new ArrayList< ArrayList<ClassOrInterface>>() ;
		for(String file:files){
			if(file.endsWith(".java")){
				FileInputStream in = new FileInputStream(file);
				CompilationUnit cu = JavaParser.parse(in);
				InterfacePrinter Inter=new InterfacePrinter(cu);
		        ArrayList<ClassOrInterface> InterfaceList= Inter.getInterface();
		        globalInterfaceList.add(InterfaceList);
			}
		}
		globalInterfaceList.removeAll(Collections.singleton(null));
		return globalInterfaceList;
		
	}

	public Relation getRelation(String original,Attribute attribute) throws Exception {
		
		Relation r1 = new Relation();
		if(!(attribute == null)){
			String a = attribute.type.toString().replaceAll("Collection","").replace("<", "").replace(">", "");
			r1.class1 = original;
			r1.class2 = a;
			if(attribute.type.toString().contains("Collection")){
				r1.related = 0;
			}
			else
				r1.related = 1 ;
		}
		return r1;
	}
	
	public Relation getRelation(String original) throws Exception{// interface/class without relation
		Relation r2 = new Relation();
		r2.class1 = original;
		r2.class2 = null;
		r2.related =2;
		return r2;
	}
	public Relation getRelation(String original,ClassOrInterfaceType parent) throws Exception {
		//inherit relation
		
		Relation r3 = new Relation();
		r3.class1 = original;
		r3.class2 =parent.getName().toString();
		r3.related = 3; //3 means inherit
		return r3;
		
		
	}
	public Relation getRelation1(String original,ClassOrInterfaceType inter ) throws Exception{
		// implements relation
		Relation r4 = new Relation();
		r4.class1 = original;
		r4.class2 = inter.getName().toString();
		r4.related = 4 ;
		return r4;
	} 
	public Relation getRelation(String original, Type parameterType) {
		//uses relation
		Relation r5 = new Relation();
		r5.class1 = original;
		r5.class2 = parameterType.toString();
		r5.related = 5;
		return r5;
	}
	public Relation getRelation(String original, String string) {
		Relation r6 = new Relation();
		r6.class1 = original;
		r6.class2 = string;
		r6.related = 6;
		return r6;
	}
	/**
	 * Prepare the data to form uml
	 * @param cu
	 * @throws Exception
	 */
	public void prepareData(CompilationUnit cu) throws Exception{
		String result = "[";
		// InterfaceList
        InterfacePrinter Inter=new InterfacePrinter(cu);
        ArrayList<ClassOrInterface> interfaceOrClassList= Inter.getInterfaceOrClass();
        //AttributeList
        AttributesPrinter attri=new AttributesPrinter(cu);
        ArrayList<Attribute> attrilist=attri.getAttribute();
        //MethodList
        MethodPrinter Meth = new MethodPrinter(cu);
        ArrayList<Method> methodList = Meth.getmethod();
        //ConstructorList
        ConstructorPrinter Con = new ConstructorPrinter(cu);
        ArrayList<Constructor> constructorList = Con.getConstructor();
        //parser class or interface
        for(int j=0;j<interfaceOrClassList.size();j++){        	
        	if(interfaceOrClassList.get(j).isInterface== false){// class name parser
            	result += interfaceOrClassList.get(j).Name;
            	result += "|";
            	//parser getMethod,setMethod 
            	for(Method method1: methodList){
            		if(method1.name.contains("get")){
            			String set = method1.name.replace("get", "set");
            			for(Method method2: methodList){
            				if(method2.name.equals(set)){
            					for(Attribute attribute1: attrilist){
            						String first = method1.name.substring(3, 4).toLowerCase();
            						String last = method1.name.substring(4);
            						String attrib = first.concat(last);
            						if(attribute1.variable.toString().substring(1,attribute1.variable.toString().length()-1).equals(attrib)){
            							methodList.remove(method1);
            							methodList.remove(method2);
            							attribute1.modifier = 1;
            							break;
            						}	
            					}break;
            				}		
            			}break;
            		}
            	// "Public Attribute" instead of Private with public setter/getter method
            	}
            	
            	//parse attribute:
            	for(int i=0;i< attrilist.size();i++){
            		String variable = attrilist.get(i).variable.toString().substring(1, attrilist.get(i).variable.toString().length()-1);
                
        			if(attrilist.get(i).type.toString().contains("int") ||
        					attrilist.get(i).type.toString().contains("String") ){//internal attributes
        				             	
	                	if( attrilist.get(i).modifier == 2 ){//private attribute
	                			result += "-";
	                			result += variable;
	                			result += ":";
	                			if(attrilist.get(i).type.toString().contains("int[]")){
	                				result += "int(*)" ;// parser int[] to int(*)
	                				}
	                			else{
	                				result += attrilist.get(i).type;
	                				}
	                			result += ";";
	                	//	System.out.print("["+ InterfaceList.get(j).Name + "|" + "-" + variable + ":" +attrilist.get(i).type+"]");
	                	}
	                	if(attrilist.get(i).modifier == 1)// public attribute
	                	{
	                		
	                		result += "+";
	                		result += variable;
	               			result += ":";
	               			result += attrilist.get(i).type;
	               			result += ";";
	               		//System.out.print("["+InterfaceList.get(j).Name+"|"+" +"+ variable + ":" +attrilist.get(i).type+"]");
	               		}
	                	
        			}
        			else{//parser Collection or other class attribute
        				//if(attrilist.get(i).modifier == 2 ||attrilist.get(i).modifier == 1 ){
        					Relation r1 = this.getRelation(interfaceOrClassList.get(j).Name, attrilist.get(i));
        					if(!relationExist(r1)){
        						this.relationList.add(r1);
        					}
        				//}
        			}
        		}
            	
            	if(attrilist.isEmpty()){
            		if(!constructorList.isEmpty() || !methodList.isEmpty())
            			result += "|";
            		
            	}
            	else{
            		//String interAttri = "[".concat(interfaceOrClassList.get(j).Name).concat("|");
            		//if(result.equals(interAttri))
                    result += "|";	
                    String interAttri = "[".concat(interfaceOrClassList.get(j).Name).concat("||");
                    if(result.equals(interAttri) && constructorList.isEmpty() || methodList.isEmpty())
                    //if(constructorList.isEmpty() || methodList.isEmpty())
            			result = result.substring(0,result.length()-1);
            		
            	}
            	
            	//parser constructor in class
            	if(!constructorList.isEmpty()){
            		for(int i=0; i<constructorList.size();i++){
            			if(constructorList.get(i).modifier == 1){
            				result +="+";
            				result +=constructorList.get(i).name;
                			if(constructorList.get(i).parameter == null &&constructorList.get(i).parameterType == null){
                				result += "()";
                			}
                			else{
                				result += "("+constructorList.get(i).parameter.toString()+":"+
                			constructorList.get(i).parameterType + ");";
                 				Relation r5 = this.getRelation(interfaceOrClassList.get(j).Name, constructorList.get(i).parameterType);
            					this.relationList.add(r5);
                				
                			}
            			}
            			else{
            				result +="";
            			}
            			
            		}
            		
            	}
            	//parser method in class
            	if(methodList.isEmpty() && constructorList.isEmpty() )
            		result = result.substring(0, result.length()-1);
            	else{
            		for(int i=0;i<methodList.size();i++){// only public method
            			if(methodList.get(i).modifiers == 1){
            				result += "+";
            				result += methodList.get(i).name;
            				if(methodList.get(i).parameter == null && methodList.get(i).parameterType == null){
            					result +="():"+methodList.get(i).type+";";	
            				}	 
               				else{
               					result += "("+methodList.get(i).parameter.toString()+":"+ methodList.get(i).parameterType+"):" + methodList.get(i).type+";";
               				}
           				}
           				if(methodList.get(i).modifiers == 9){// main modifiers is 9
           					result += "+";
           					result += methodList.get(i).name;
           					result += "(" + methodList.get(i).parameter.toString()+":"+methodList.get(i).parameterType.toString().replace("[", "［").replace("]", "］")+"):"+methodList.get(i).type;
           				}
           				else{
           					result += "";            			
           					}
            		}
        		}
        		result += "]";
        		
        		map.put(interfaceOrClassList.get(j).Name, result);
        		
        		//r3 inherit relation
        		for(int i=0;i<interfaceOrClassList.get(j).ExtendsList.size();i++){
        			Relation r3 = this.getRelation(interfaceOrClassList.get(j).Name, interfaceOrClassList.get(j).ExtendsList.get(i));
        			this.relationList.add(r3);
        		}
        		
        		// r4 implement interface relation
        		for(int i=0;i<interfaceOrClassList.get(j).ImplementsList.size();i++){
        			Relation r4 = this.getRelation1(interfaceOrClassList.get(j).Name, interfaceOrClassList.get(j).ImplementsList.get(i));
        			this.relationList.add(r4);
        		}
        		// r5 uses relation
        		for(int i=0; i< methodList.size();i++){
        			if(methodList.get(i).parameterType == null ){
        				Relation r2 = this.getRelation(interfaceOrClassList.get(j).Name);
                		this.relationList.add(r2);//r2 class without relation
        			}
        			else{
        				if(methodList.get(i).name.equals("main")){//main with the uses component 
        					Relation r6 = this.getRelation(interfaceOrClassList.get(j).Name, "Component");
        					this.relationList.add(r6);
        				}
        				else{
        					Relation r5 = this.getRelation(interfaceOrClassList.get(j).Name, methodList.get(i).parameterType);
        					this.relationList.add(r5);
        				}		 
        			}
            	}
        						
        	}
        	else{// r2 parser interface
        		result += "＜＜interface＞＞;"+ interfaceOrClassList.get(j).Name;
        		Relation r2 = this.getRelation(interfaceOrClassList.get(j).Name);// interface without relation
        		this.relationList.add(r2);
        		result += "||";
        		// parser method in interface
        		if(methodList.isEmpty()){
            		result = result.substring(0, result.length()-2);
            	}
            	else{
            		for(int i=0;i<methodList.size();i++){
            			if(methodList.get(i).modifiers == 1){
            				result += "+";
            			}
            			else{
            				result += "-";
            			}
            		
            			result += methodList.get(i).name;
            			if(methodList.get(i).parameter == null && methodList.get(i).parameterType == null){
            				result +="():"+methodList.get(i).type+";";
        				
            			}
            			else{
            				result += "("+methodList.get(i).parameter.toString()+":"+ methodList.get(i).parameterType+");";
            			}
            		}
            	}
        		result += "]";
        		map.put(interfaceOrClassList.get(j).Name,result);
        	}
        }
	}
	

	boolean relationExist(Relation r){
		for(Relation relation:this.relationList){
			if(relation.class2 == null){
				return false;
			}
			else{
				if(relation.class1.equals(r.class1) && relation.class2.equals(r.class2)){
					return true;
				}
				if(relation.class1.equals(r.class2) && relation.class2.equals(r.class1)){
					return true;
				}
			}
		}
		return false;
	}
}

