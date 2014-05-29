package ru.ccfit.nsu.dorozhko.translation_methods;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;

/**
 * Created by Anton on 27.03.14.
 */
public class ArithmeticParserTest {
    private IBuffer bufferRight;
    private Lexer lexerRight;
    private ArithmeticParser parser;

    @Before
    public void setUp() throws Exception {
        bufferRight = new Buffer(new StringReader("2+2"));

        lexerRight = new Lexer(bufferRight);
        parser = new ArithmeticParser(lexerRight);
    }

    @Test
    public void testParseExpression() throws Exception {
        Assert.assertEquals(4, parser.parseExpression());
    }

    @Test
    public void testXMLoutput() throws Exception {
        StreamResult streamResult = new StreamResult(new File("IRT.xml"));
        parser.parseExpression();
        parser.outputIRT(streamResult);
    }
}
