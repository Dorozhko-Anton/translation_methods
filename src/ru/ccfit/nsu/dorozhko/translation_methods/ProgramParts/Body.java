package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands.Command;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 03.04.14.
 */
public class Body implements XMLable {
    private List<Command> commandList = new ArrayList<Command>();

    public List<Command> getCommandList() {
        return commandList;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("body");
        for (Command c : commandList) {
            c.toDOM(doc, elem);
        }
        parent.appendChild(elem);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
