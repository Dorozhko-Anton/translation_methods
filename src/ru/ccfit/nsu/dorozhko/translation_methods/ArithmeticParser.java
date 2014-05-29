package ru.ccfit.nsu.dorozhko.translation_methods;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

import static ru.ccfit.nsu.dorozhko.translation_methods.Lexer.Type.*;

/**
 * Created by Anton on 27.03.14.
 */
public class ArithmeticParser {
    private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder docBuilder;
    private Lexer lexer;
    private Lexer.Token curToken;
    private Document IRT;
    private Element rootElement;
    private Element curRootElement;

    public ArithmeticParser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        curToken = lexer.getToken();

        IRT = docBuilder.newDocument();
        rootElement = IRT.createElement("IRT");
        IRT.appendChild(rootElement);
        curRootElement = rootElement;

    }

    public void outputIRT(StreamResult streamResult) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(IRT);

        transformer.transform(source, streamResult);
    }

    public int parseExpression() throws IOException {
        Element localRoot = IRT.createElement(String.valueOf(Tags.OPERATION));
        curRootElement.appendChild(localRoot);
        curRootElement = localRoot;

        int result = parseTerm();
        while (curToken.type == PLUS ||
                curToken.type == MINUS) {
            switch (curToken.type) {
                case PLUS:
                    localRoot.setAttribute("opcode", "+");
                    curToken = lexer.getToken();
                    result += parseTerm();
                    break;
                case MINUS:
                    localRoot.setAttribute("opcode", "-");
                    curToken = lexer.getToken();
                    result -= parseTerm();
                    break;
            }
        }
//        if (curToken.type != Lexer.Type.END_OF_STREAM) {
//            throw new IOException(curToken.row + ":" + curToken.column +
//                    " ожидалось: END_OF_STREAM. получено: " + curToken.c);
//        }
        return result;
    }

    public int parseTerm() throws IOException {
        Element localRoot = IRT.createElement(String.valueOf(Tags.TERM));
        curRootElement.appendChild(localRoot);
        curRootElement = localRoot;

        int result = parseFactor();
        //curToken = lexer.getToken();
        while (curToken.type == DIVIDE ||
                curToken.type == MULTIPLY) {
            switch (curToken.type) {
                case DIVIDE:
                    localRoot.setAttribute("opcode", "/");
                    curToken = lexer.getToken();
                    result /= parseFactor();
                    break;
                case MULTIPLY:
                    localRoot.setAttribute("opcode", "*");
                    curToken = lexer.getToken();
                    result *= parseFactor();
                    break;
//                default:
//                    throw new IOException(curToken.row + ":" + curToken.column +
//                            "ожидалось: *, /. получено: " + curToken.c);
            }
            // curToken = lexer.getToken();
        }
        return result;
    }

    public int parseFactor() throws IOException {
        Element localRoot = IRT.createElement(String.valueOf(Tags.FACTOR));
        curRootElement.appendChild(localRoot);
        curRootElement = localRoot;

        int result = parsePower();
        //curToken = lexer.getToken();
        if (curToken.type == Lexer.Type.POWER) {
            localRoot.setAttribute("opcode", "^");
            curToken = lexer.getToken();
            result = (int) Math.pow(result, parseFactor());

        }

        return result;
    }

    public int parsePower() throws IOException {
        Element localRoot = IRT.createElement(String.valueOf(Tags.POWER));
        curRootElement.appendChild(localRoot);
        curRootElement = localRoot;

        if (curToken.type == MINUS) {
            localRoot.setAttribute("opcode", "-");
            curToken = lexer.getToken();
            return -parseAtom();
        }
        return parseAtom();
    }

    public int parseAtom() throws IOException {
        Element localRoot = IRT.createElement(String.valueOf(Tags.ATOM));
        curRootElement.appendChild(localRoot);
        curRootElement = localRoot;

        int result;
        if (curToken.type == Lexer.Type.NUMBER) {
            result = Integer.parseInt(curToken.c);
            curToken = lexer.getToken();
            localRoot.setAttribute("value", String.valueOf(result));
            return result;
        } else if (curToken.type == Lexer.Type.LEFT_PARENTHESIS) {
            curToken = lexer.getToken();
            result = parseExpression();
            if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
                curToken = lexer.getToken();
                return result;
            } else {
                throw new IOException("нет закрывающей скобки " + curToken.row + ":" + curToken.column);
            }
        }
        throw new IOException("не число " + curToken.row + ":" + curToken.column);
    }

    static enum Tags {
        OPERATION("operation"),
        TERM("term"),
        POWER("power"), ATOM("atom"), FACTOR("factor");
        private String name;

        Tags(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static {
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


}
