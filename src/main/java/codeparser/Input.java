
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
import com.github.javaparser.ast.Modifier;
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
//this is a input class
public class Input {
	private static List<CompilationUnit> cuSet=new ArrayList<CompilationUnit>();
	public static void main (String[] args){
	File mainFolder = new File("resources\\input_files");
	long length = 0L;
	length=mainFolder.length();
	System.out.println("size "+length/(1024*1024));
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
				
				e.printStackTrace();
			}

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
	//module to parse the input java files 
	public static void parseClassOrInterface(CompilationUnit cu){
		 //naming the node as the initial class name 
		int numberOfClasses=cu.getTypes().size();
		for(int i=0;i<numberOfClasses;i++){
		Node node=cu.getTypes().get(i);	        
	    
	    ClassOrInterfaceDeclaration coit= (ClassOrInterfaceDeclaration)node;	    
	    if(coit.isInterface())
	    	System.out.println(coit.getName()+" is interface");
	    else
	    	System.out.println(coit.getName()+" is Class");    
	    // visit and print the Constructor names	        	       
	    boolean moreClasses = false;	      
	    String methods = "";	        
	    for (BodyDeclaration bodyDec : ((TypeDeclaration<?>) node).getMembers()) {
	        // Get Constructors        
	        if (bodyDec instanceof ConstructorDeclaration) {        	
	        	ConstructorDeclaration consDec = ((ConstructorDeclaration) bodyDec);
	            if (consDec.getDeclarationAsString().startsWith("public")
	                    && !coit.isInterface()) {
	            	
	            	if (moreClasses){                
	                    methods += ";";
	                    System.out.println(methods);
	                }            	
	                methods += "+ " + consDec.getName() + "(";
	                for (Object obj : consDec.getChildNodes()) {
	                	
	                	if (obj instanceof Parameter) {
	                		
	                        Parameter pCast = (Parameter) obj;
	                        String parameterClass = pCast.getType().toString();
	                        String paramterName = pCast.getChildNodes()
	                                .get(0).toString();
	                        methods += paramterName + " : " + parameterClass;	                            
	                    }
	                }
	                methods += ")";
	                moreClasses = true;
	            }
	        }
	    }		       
	    System.out.println(methods);
	    }
		
	} 
	public static void generateParse(CompilationUnit cu){		
		       
		       parseClassOrInterface(cu);
		       new MethodVisitor().visit(cu, null);		
	   
		    }
		        
	
		    /**
		     * Simple visitor implementation for visiting MethodDeclaration nodes.
		     */
		    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		        @Override
		        public void visit(MethodDeclaration n, Void arg) {
		        	String method="";
		                      
		            super.visit(n, arg);
		            if (n.getDeclarationAsString().startsWith("public")
		            		||n.getDeclarationAsString().startsWith("private")){
		            method += "+ " + n.getName() + "(";
	                for (Object gcn : n.getChildNodes()) {                	
	                	if (gcn instanceof Parameter) {             
	                        Parameter paramCast = (Parameter) gcn;
	                        String paramClass = paramCast.getType().toString();
	                        String paramName = paramCast.getChildNodes()
	                                .get(0).toString();
	                        method += paramName + " : " + paramClass;	                            
	                    }
	                }
	                method+=")";
	                System.out.println(method);
		            }
		        }
		       /* @Override
		        public void visit(VariableDeclarationExpr n, Void arg) {
		        	
		        	n.getType().accept(this, arg);
		        	printer.print(" ");

		        	for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
		        		VariableDeclarator v = i.next();
		        		v.accept(this, arg);
		        		if (i.hasNext()) {
		        			printer.print(", ");
		        		}
		        	}
		        }*/
		        
				
		    }
		
}
	
