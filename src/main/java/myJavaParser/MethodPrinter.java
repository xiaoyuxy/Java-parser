package myJavaParser;

import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
* Given CompilationUnit cu, get Method list
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class MethodPrinter {	
		
		private CompilationUnit cu;
		public  MethodPrinter(CompilationUnit cu){
			this.cu=cu;
		}
		
		public ArrayList<Method> getmethod() throws Exception{
			ArrayList<Method> methodList = new ArrayList<Method>();
			new MethodVisitor().visit(cu,methodList);
			return methodList;
			
		}

		    
		public static class MethodVisitor extends VoidVisitorAdapter<Object> {
			public void visit(MethodDeclaration n, Object arg) {

				@SuppressWarnings("unchecked")
				ArrayList<Method> list = (ArrayList<Method>) arg;
				Method a = new Method();
				a.name = n.getName();
				a.type = n.getType();
				a.modifiers = n.getModifiers();
				if(!n.getParameters().isEmpty()){
					a.parameter = n.getParameters().get(0).getId();
					a.parameterType = n.getParameters().get(0).getType();
				}
				else{
					a.parameter = null;
					a.parameterType = null;
				}
				list.add(a);
				super.visit(n, arg);
		        }
		    }

		public static class Method{
			Type type;
			Type parameterType;
			VariableDeclaratorId parameter;
			String name;
			int modifiers;
			
		}

}
