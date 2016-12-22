package parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Comparator;
import java.util.Map;
import java.util.Stack;

/**
 * default tree listener
 */
public class DefaultListener implements ParseTreeListener {

    protected Parser parser;
    private Map<String, Integer> rmap;
    private Stack<Comparator> compstack = new Stack<Comparator>();
    private Stack<String> context = new Stack<String>();

    private static parser.DefaultListener listener = null;

    /**
     * constructor
     */
    public DefaultListener() {
        this.parser = null;
        this.rmap = null;
    }

    /**
     * maps rule index to its actual name
     * @param key rule index
     * @return the corresponding rule name
     */
    public String getRuleByKey(int key) {

        for (Map.Entry<String, Integer> e : this.rmap.entrySet()) {
            if (e.getValue() == key)
                return e.getKey();
        }
        return null;
    }

    /**
     * set parser
     * @param p parser
     */
    protected void setParser(Parser p) {
        this.parser = p;
        this.rmap = this.parser.getRuleIndexMap();
    }


    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
