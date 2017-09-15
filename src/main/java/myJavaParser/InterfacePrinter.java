package myJavaParser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
/**
* Given CompilationUnit cu, get Interface list
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class InterfacePrinter {
	
	private CompilationUnit cu;

	public InterfacePrinter(CompilationUnit cu){
		this.cu=cu;
	}
	//Interface or class
	public ArrayList<ClassOrInterface> getInterfaceOrClass() throws Exception {
       
        ArrayList<ClassOrInterface> ClassOrInterface = new ArrayList<ClassOrInterface>();
        
        new ClassOrInterfaceVistor().visit(cu,ClassOrInterface);
        return ClassOrInterface;
        
    }
	public ArrayList<ClassOrInterface> getInterface() throws Exception{//put interface in interfaceList;
		ArrayList<ClassOrInterface> Interface = new ArrayList<ClassOrInterface>();
		new ClassOrInterfaceVistor().visit(cu,Interface);
		ArrayList<ClassOrInterface> interfaceList = new ArrayList<ClassOrInterface>();
		for(ClassOrInterface inter:Interface){
			//ArrayList<ClassOrInterface> interfaceList = new ArrayList<ClassOrInterface>();
			if(inter.isInterface){
				interfaceList.add(inter);
			}
			else{
				interfaceList = null;
			}
		}
		return interfaceList;
		
	}

    public static class ClassOrInterfaceVistor extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        	@SuppressWarnings("unchecked")
			ArrayList<ClassOrInterface> list = (ArrayList<ClassOrInterface>) arg;
        	ClassOrInterface a = new ClassOrInterface();
        	a.Name=n.getName();
        	a.Modifiers= n.getModifiers();
        	a.isInterface=n.isInterface();
        	a.ImplementsList = n.getImplements();
        	a.ExtendsList = n.getExtends();
        	list.add(a);
        	super.visit(n, arg);
        	
        	
          
        }
      }
    
    public static class ClassOrInterface{
		public boolean isInterface;
		String Name;
		int Modifiers;
		List<ClassOrInterfaceType> ImplementsList;
		List<ClassOrInterfaceType> ExtendsList;
		
	}
}
