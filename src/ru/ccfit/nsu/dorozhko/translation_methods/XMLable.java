package ru.ccfit.nsu.dorozhko.translation_methods;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Anton on 03.04.14.
 */
public interface XMLable {
    void toDOM(Document doc, Element parent);

}
