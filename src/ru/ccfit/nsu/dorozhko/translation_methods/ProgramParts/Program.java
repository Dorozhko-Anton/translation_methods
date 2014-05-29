package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 03.04.14.
 */
public class Program implements XMLable {
    private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder docBuilder;
    private Document IRT;
    private Element rootElement;

    private List<Method> methodList = new ArrayList<Method>();


    public List<Method> getMethodList() {
        return methodList;
    }


    @Override
    public void toDOM(Document doc, Element parent) {
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        IRT = docBuilder.newDocument();
        rootElement = IRT.createElement("program");
        IRT.appendChild(rootElement);

        for (Method m : methodList) {
            m.toDOM(IRT, rootElement);
        }


    }

    public void outputIRT(StreamResult streamResult) throws TransformerException {

        this.toDOM(IRT, null);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(IRT);

        OutputFormat format = new OutputFormat(IRT);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer;
        serializer = new XMLSerializer(out, format);
        try {
            serializer.serialize(IRT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(out.toString());


        transformer.transform(source, streamResult);
    }

    public void acceptVisitor(CodeGenVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
