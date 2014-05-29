package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton on 03.04.14.
 */
public class Arglist implements XMLable {
    private List<Argument> argumentList = new LinkedList<Argument>();

    @Override
    public void toDOM(Document doc, Element parent) {
        Element elem = doc.createElement("arglist");
        for (Argument a : argumentList) {
            a.toDOM(doc, elem);
        }
        parent.appendChild(elem);
    }

    public List<Argument> getArgumentList() {
        return argumentList;
    }

    public static class Argument implements XMLable {
        private Type type;
        private String name;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void toDOM(Document doc, Element parent) {
            Element elem = doc.createElement("argument");
            elem.setAttribute("type", type.toString());
            elem.setAttribute("name", name);
            parent.appendChild(elem);
        }
    }
}
