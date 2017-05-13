package codeparser;

public class UMLApp {

    public static void main(String[] args) throws Exception {
        if (args[0].equals("class")) {
            ClassDiagramParser pe = new ClassDiagramParser(args[1], args[2]);
            pe.init();
        } else if (args[0].equals(("sequence"))) {
            SequenceDiagramParser pse = new SequenceDiagramParser(args[1], args[2], args[3], args[4]);
            pse.init();
        } else {
            System.out.println("Invalid keyword " + args[0]);
        }

    }

}

