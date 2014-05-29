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
public class Term implements XMLable {
    private List<Factor> factorList = new ArrayList<Factor>();
    private List<Lexer.Type> opList = new ArrayList<Lexer.Type>();

    public List<Factor> getFactorList() {
        return factorList;
    }

    public List<Lexer.Type> getOpList() {
        return opList;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("term");
        Element rootNode;
        Element curNode;
        if (opList.size() > 0) {
            rootNode = doc.createElement("operation");
            elem.appendChild(rootNode);
            curNode = rootNode;
            for (int i = 0; i < opList.size() - 1; i++) {
                curNode.setAttribute("opcode", opList.get(i).toString());
                factorList.get(i).toDOM(doc, curNode);
                Element nextNode = doc.createElement("operation");
                curNode.appendChild(nextNode);
                curNode = nextNode;
            }
        } else {
            factorList.get(0).toDOM(doc, elem);
        }

        parent.appendChild(elem);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
