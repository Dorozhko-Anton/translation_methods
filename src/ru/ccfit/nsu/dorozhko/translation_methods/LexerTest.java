package ru.ccfit.nsu.dorozhko.translation_methods;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Anton on 20.03.14.
 */
public class LexerTest extends Assert {
    private IBuffer bufferWithBadComments;
    private Lexer lexerWithBadBuffer;

    private IBuffer bufferRight;
    private Lexer lexerRight;

    @Before
    public void setUp() throws Exception {
        bufferWithBadComments = new Buffer(new StringReader("4+5=9\n" +
                "\n" +
                "    (abcd) // lololo comments\n" +
                "/* multiline\n" +
                "   comments\n" +
                "   /\n" +
                "end"));
        lexerWithBadBuffer = new Lexer(bufferWithBadComments);

        bufferRight = new Buffer(new StringReader("double ICE(int arg) {\n" +
                "\tx = 5;\n" +
                "\treturn 8.3;\n" +
                "}"));

        lexerRight = new Lexer(bufferRight);
    }

    @Test(expected = IOException.class)
    public void testGetToken() throws Exception {
        for (int i = 0; i < 5; i++) {
            lexerWithBadBuffer.getToken();
        }
        assertEquals(Lexer.Type.LEFT_PARENTHESIS, lexerWithBadBuffer.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerWithBadBuffer.getToken().type);
        assertEquals(Lexer.Type.RIGHT_PARENTHESIS, lexerWithBadBuffer.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerWithBadBuffer.getToken().type);

    }

    @Test
    public void testGetTokenWithRightBuffer() throws Exception {
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        assertEquals(Lexer.Type.LEFT_PARENTHESIS, lexerRight.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        assertEquals(Lexer.Type.RIGHT_PARENTHESIS, lexerRight.getToken().type);
        assertEquals(Lexer.Type.LEFT_BRACES, lexerRight.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        assertEquals(Lexer.Type.EQUALS, lexerRight.getToken().type);
        assertEquals(Lexer.Type.NUMBER, lexerRight.getToken().type);
        assertEquals(Lexer.Type.SEMICOLON, lexerRight.getToken().type);
        assertEquals(Lexer.Type.ATOM, lexerRight.getToken().type);
        Lexer.Token token = lexerRight.getToken();
        //assertEquals(Lexer.Type.NUMBER, lexerRight.getToken().type);

        assertEquals(Lexer.Type.SEMICOLON, lexerRight.getToken().type);
        assertEquals(Lexer.Type.RIGHT_BRACES, lexerRight.getToken().type);
        assertEquals(Lexer.Type.END_OF_STREAM, lexerRight.getToken().type);

    }
}
