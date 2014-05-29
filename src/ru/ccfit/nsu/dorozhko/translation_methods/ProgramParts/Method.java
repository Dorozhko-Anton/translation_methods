package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class Method implements XMLable {

    private Type returnType;
    private String name;
    private Arglist arguments;
    private Body body;

    public int getNumberOfLocals() {
        return numberOfLocals;
    }

    public void setNumberOfLocals(int numberOfLocals) {
        this.numberOfLocals = numberOfLocals;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    private int numberOfLocals;
    private int stackSize;

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Arglist getArguments() {
        return arguments;
    }

    public void setArguments(Arglist arguments) {
        this.arguments = arguments;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("method");
        elem.setAttribute("return", returnType.toString());
        elem.setAttribute("name", name);
        arguments.toDOM(doc, elem);
        body.toDOM(doc, elem);
        parent.appendChild(elem);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
