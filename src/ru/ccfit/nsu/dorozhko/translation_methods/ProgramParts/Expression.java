package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.Lexer;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 03.04.14.
 */
public class Expression implements XMLable {
    private List<Term> termList = new ArrayList<Term>();
    private List<Lexer.Type> operatios = new ArrayList<Lexer.Type>();

    public List<Term> getTermList() {
        return termList;
    }

    public List<Lexer.Type> getOperatios() {
        return operatios;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("expr");
        Element rootNode;
        Element curNode;
        if (operatios.size() > 0) {
            rootNode = doc.createElement("operation");
            elem.appendChild(rootNode);
            curNode = rootNode;
            for (int i = 0; i < operatios.size() - 1; i++) {
                curNode.setAttribute("opcode", operatios.get(i).toString());
                termList.get(i).toDOM(doc, curNode);
                Element nextNode = doc.createElement("operation");
                curNode.appendChild(nextNode);
                curNode = nextNode;
            }
        } else {
            termList.get(0).toDOM(doc, elem);
        }

        parent.appendChild(elem);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
