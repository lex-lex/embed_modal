package util.thf_manipulation;

import exceptions.ParseException;
import org.antlr.v4.runtime.ANTLRInputStream;
import parser.ParseContext;
import parser.ThfAstGen;
import util.tree.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConjectureStripper {

    public static Node getProblemWithoutConjecture(Path file) throws IOException, ParseException {
        String problem = new String(Files.readAllBytes(file));
        return getProblemWithoutConjecture(problem);
    }

    public static Node getProblemWithoutConjecture(String problem) throws ParseException {
        ANTLRInputStream inputStream = new ANTLRInputStream(problem);
        ParseContext parseContext = ThfAstGen.parse(inputStream, "tPTP_file", "");
        Node root = parseContext.getRoot();
        root.dfsRuleAll("thf_annotated").forEach(n->{
            Node nameLeaf = n.getChild(2).getFirstLeaf();
            if (nameLeaf.equals("conjecture") || nameLeaf.equals("negated_conjecture"))
                n.getParent().delChild(n);
        });
        System.out.println(root.toStringWithLinebreaks());
        return root;
    }
}