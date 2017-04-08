package codeparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class second {
	private static List<CompilationUnit> cuSet=new ArrayList<CompilationUnit>();
	public static void main (String[] args){
	File mainFolder = new File("G:\\Study\\M.S\\202\\UML-Parser\\resources\\input_files");
	getFiles(mainFolder);	
	generateParse(cuSet.get(0));
	//for(String files:fileSet)
		//System.out.println(files);
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
	        //getting the entire code in the the form of a list
	        List<TypeDeclaration<?>> codeBody=cu.getTypes();
	        //naming the node as the initial class name 
	        Node node=codeBody.get(0);
	        //checking if the present node is a class or an interface
	        ClassOrInterfaceDeclaration coit= (ClassOrInterfaceDeclaration)node;
	        if(coit.isInterface())
	        	System.out.println(coit.getName()+" is interface");
	        else
	        	System.out.println(coit.getName()+" is Class");
	        
	        // visit and print the methods names	        
	        new MethodVisitor().visit(cu, null);
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
	    }
}

