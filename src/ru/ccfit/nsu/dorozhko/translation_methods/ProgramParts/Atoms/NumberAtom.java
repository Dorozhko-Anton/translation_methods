package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class NumberAtom extends Atom {
    public Boolean isDouble = false;
    public double doubleValue;
    public int intValue;

    public NumberAtom(double doubleValue) {
        this.doubleValue = doubleValue;
        isDouble = true;
    }

    public NumberAtom(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("atom");
        elem.setAttribute("type", "number");
        if (isDouble) {
            elem.setTextContent(String.valueOf(doubleValue));
        } else {
            elem.setTextContent(String.valueOf(intValue));
        }
        parent.appendChild(elem);
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
