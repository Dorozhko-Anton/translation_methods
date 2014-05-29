package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class NameAtom extends Atom {
    private String name;

    public String getName() {
        return name;
    }

    public NameAtom(String name) {
        this.name = name;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("atom");
        elem.setAttribute("type", "name");
        elem.setTextContent(name);
        parent.appendChild(elem);
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {

        visitor.visit(this);
    }
}
