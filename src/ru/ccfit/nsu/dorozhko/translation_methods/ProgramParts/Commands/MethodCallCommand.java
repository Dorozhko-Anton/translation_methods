package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.ParamList;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class MethodCallCommand extends Command {
    private String name;
    private ParamList params;

    public MethodCallCommand(String name, ParamList params) {
        this.name = name;
        this.params = params;
    }

    public ParamList getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        //TODO:
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
