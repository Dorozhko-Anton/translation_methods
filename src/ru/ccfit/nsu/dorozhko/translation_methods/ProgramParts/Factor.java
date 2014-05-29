package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class Factor implements XMLable {
    public Power base;
    public Factor power = null;

    public void setBase(Power base) {
        this.base = base;
    }

    public void setPower(Factor power) {
        this.power = power;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("factor");
        base.toDOM(doc, elem);
        if (power != null) {
            power.toDOM(doc, elem);
        }
        parent.appendChild(elem);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
