package myJavaParser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
* AttibutesPrinter
* Given CompilationUnit cu, get Attribute list
*
* @author  Xiaoyu Liang
* @date   2016/03/31
*/
public class AttributesPrinter {

	private CompilationUnit cu;
	
	public AttributesPrinter(CompilationUnit cu){
		this.cu = cu;
	}
	// attributes
	public  ArrayList<Attribute> getAttribute() throws Exception {
        ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
        new AttributesVisitor().visit(this.cu, attributeList);
        return attributeList;
  
    }
	
   
    public static class AttributesVisitor extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(FieldDeclaration n, Object arg) {   
            @SuppressWarnings("unchecked")
			ArrayList<Attribute> list = (ArrayList<Attribute>) arg;
            Attribute at = new Attribute();
            at.modifier = n.getModifiers();
            at.type = n.getType();
            at.variable = n.getVariables();
            list.add(at);
            super.visit(n, arg);
        }
    }
    /**
     * Define an attribute
     * @author Xiaoyu Liang
     *
     */
    
    public static class Attribute{
		Type type;
		int modifier;
		List<VariableDeclarator> variable;
	}
}
