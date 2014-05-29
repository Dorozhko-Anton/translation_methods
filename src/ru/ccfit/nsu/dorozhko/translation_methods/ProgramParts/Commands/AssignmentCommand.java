package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Expression;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class AssignmentCommand extends Command {
    private String name;
    private Expression expression;

    public AssignmentCommand(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("assign");
        elem.setAttribute("var", name);
        expression.toDOM(doc, elem);
        parent.appendChild(elem);
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
