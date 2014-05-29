package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.ParamList;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public class MethodCallAtom extends Atom {
    private String name;

    public String getName() {
        return name;
    }

    public ParamList getParamList() {
        return paramList;
    }

    private ParamList paramList;

    public MethodCallAtom(String name, ParamList paramList) {
        this.name = name;
        this.paramList = paramList;
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
