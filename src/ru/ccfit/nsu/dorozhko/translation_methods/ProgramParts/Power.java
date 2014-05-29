package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.Atom;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class Power implements XMLable {
    public String sign;
    public Atom atom;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setAtom(Atom atom) {
        this.atom = atom;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("power");
        elem.setAttribute("sign", sign);
        atom.toDOM(doc, elem);
        parent.appendChild(elem);
    }


    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
