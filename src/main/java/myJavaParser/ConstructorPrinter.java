package myJavaParser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
/**
* Given CompilationUnit cu, get Constructor list
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class ConstructorPrinter {
	
	private CompilationUnit cu;
	
	public ConstructorPrinter(CompilationUnit cu){ 
		this.cu = cu ;
	}
	public ArrayList<Constructor> getConstructor() throws Exception{
		ArrayList<Constructor> constructorList = new ArrayList<Constructor>();
		new ConstructorVisitor().visit(this.cu, constructorList);
		return constructorList;
		
	}
	
	public static class ConstructorVisitor extends VoidVisitorAdapter<Object>{
		public void visit(ConstructorDeclaration n, Object arg){
			@SuppressWarnings("unchecked")
			ArrayList<Constructor> list= (ArrayList<Constructor>) arg;
			Constructor a = new Constructor();
			a.modifier = n.getModifiers();
			a.name = n.getName();
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
	public  static class Constructor{
		Type parameterType;
		List<TypeParameter> typeParameter;
		VariableDeclaratorId parameter;
		String name;
		int modifier;
		
		
	}
}
