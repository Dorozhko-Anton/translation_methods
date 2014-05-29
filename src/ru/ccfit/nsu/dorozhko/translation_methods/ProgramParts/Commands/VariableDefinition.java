package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Type;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class VariableDefinition extends Command {

    private Type t;
    private String name;

    public VariableDefinition(Type t, String name) {
        this.t = t;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type getT() {
        return t;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        // TODO:
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
