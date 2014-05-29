package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Expression;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class ReturnCommand extends Command {
    private Expression expression;

    public ReturnCommand(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("return");
        expression.toDOM(doc, elem);
        parent.appendChild(elem);
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
