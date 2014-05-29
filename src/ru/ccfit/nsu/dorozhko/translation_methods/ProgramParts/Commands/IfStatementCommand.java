package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.Lexer;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Body;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Expression;

import java.io.IOException;

/**
 * Created by Anton on 22.05.2014.
 */
public class IfStatementCommand extends Command {
    private Expression expression1;
    private Lexer.Type op;
    private Expression expression2;
    private Body body;
    private Body elseBody;

    public Body getElseBody() {
        return elseBody;
    }

    public void setElseBody(Body elseBody) {
        this.elseBody = elseBody;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public void setExpression1(Expression expression1) {
        this.expression1 = expression1;
    }

    public Lexer.Type getOp() {
        return op;
    }

    public void setOp(Lexer.Type op) {
        this.op = op;
    }

    public Expression getExpression2() {
        return expression2;
    }

    public void setExpression2(Expression expression2) {
        this.expression2 = expression2;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }

    @Override
    public void toDOM(Document doc, Element parent) {

    }
}
