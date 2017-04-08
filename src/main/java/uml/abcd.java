
package codeparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Input {
	private static List<CompilationUnit> cuSet=new ArrayList<CompilationUnit>();
	public static void main (String[] args){
	File mainFolder = new File("G:\\Study\\M.S\\202\\UML-Parser\\resources\\input_files");
	getFiles(mainFolder);	
	for(CompilationUnit cus:cuSet)
		generateParse(cus);
	
}
public static void getFiles(File f)
{
   File files[];
   try{
   if(f.isFile() && f.getName().endsWith(".java")){
	   FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // get the CU for the file
       CompilationUnit cu = JavaParser.parse(in);       
	   cuSet.add(cu);	   
   }
   else
   {
      files = f.listFiles();
      for (int i = 0; i < files.length; i++) 
      {
         getFiles(files[i]);
      }
  }
  }
   catch(Exception e){
	   System.out.println("not a java file");
   }
}

public static void generateParse(CompilationUnit cu){		
	       
	        //naming the node as the initial class name 
	        Node node=cu.getTypes().get(0);	        
	        //checking if the present node is a class or an interface
	        ClassOrInterfaceDeclaration coit= (ClassOrInterfaceDeclaration)node;	       
	        if(coit.isInterface())
	        	System.out.println(coit.getName()+" is interface");
	        else
	        	System.out.println(coit.getName()+" is Class");    
	        // visit and print the methods names	        	       
	        boolean nextParam = false;	      
	        String methods = "";	        
	        for (BodyDeclaration bd : ((TypeDeclaration<?>) node).getMembers()) {
	            // Get Methods
	            
	            if (bd instanceof ConstructorDeclaration) {
	            	
	            	ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
	                if (cd.getDeclarationAsString().startsWith("public")
	                        && !coit.isInterface()) {
	                    if (nextParam){
	                        methods += ";";
	                        System.out.println(methods);
	                    }
	                    methods += "+ " + cd.getName() + "(";
	                    for (Object gcn : cd.getChildNodes()) {
	                        if (gcn instanceof Parameter) {
	                            Parameter paramCast = (Parameter) gcn;
	                            String paramClass = paramCast.getType().toString();
	                            String paramName = paramCast.getChildNodes()
	                                    .get(0).toString();
	                            methods += paramName + " : " + paramClass;	                            
	                        }
	                    }
	                    methods += ")";
	                    nextParam = true;
	                }
	            }
	        }		       
	        System.out.println(methods);
	        //	new MethodVisitor().visit(cu, null);
	/*
        new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                super.visit(n, arg);
                System.out.println(" * " + n.getName());
            }
        }.visit(cu, null);
        System.out.println(); // empty line*/
   
	    }
	        

	    /**
	     * Simple visitor implementation for visiting MethodDeclaration nodes.
	     */
	    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
	        @Override
	        public void visit(MethodDeclaration n, Void arg) {
	            /* here you can access the attributes of the method.
	             this method will be called for all methods in this 
	             CompilationUnit, including inner class methods */
	            System.out.println(n.getName());
	            super.visit(n, arg);
	        }
	       /* @Override
	        public void visit(FieldDeclaration n,Object arg)
		    {     
		    	String newstr;
		    	List<VariableDeclarator> list = n.getVariables();
		    	//as getVariables() returns a list we need to implement that way 
		    	for (VariableDeclarator var : list) {

		    	    String item = var.toString();

		    	    if (item.contains("=")) {

		    	        if (item != null && item.length() > 0) {

		    	            int index = item.lastIndexOf("=");
		    	            newstr = item.substring(0, index);
		    	            newstr = newstr.trim();
		    	            		    	            
		    	        }
		    	    } 
		    	}
		    }*/
	    }


	}

