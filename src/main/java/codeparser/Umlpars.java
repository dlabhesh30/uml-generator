package codeparser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Umlpars {


    public static void main(String[] args) throws Exception {
        // parse a file
        CompilationUnit cu = JavaParser.parse(new File("G:\\Study\\M.S\\202\\UML-Parser\\resources\\input_files\\first.java"));


        private static class MethodChangerVisitor extends VoidVisitorAdapter<Void> {
            @Override
            public void visit(MethodDeclaration n, Void arg) {
                // change the name of the method to upper case
                n.setName(n.getNameAsString().toUpperCase());

            
            }
        }
       
    }
    }