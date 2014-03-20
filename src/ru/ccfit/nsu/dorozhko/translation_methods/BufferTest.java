package ru.ccfit.nsu.dorozhko.translation_methods;

import junit.framework.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Created by Anton on 20.03.14.
 */
public class BufferTest extends Assert {

    private IBuffer buffer = new Buffer(new StringReader("4+5\nabcd/34"));

    @Test
    public void testGetChar() throws Exception {
        assertEquals(buffer.getChar(), '4');
        assertEquals(buffer.getChar(), '+');
        assertEquals(buffer.getChar(), '5');
        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }
        assertEquals(buffer.getChar(), 'c');
    }

    @Test
    public void testPick() throws Exception {
        assertEquals(buffer.pick(0), '4');
        assertEquals(buffer.pick(2), '5');
        assertEquals(buffer.pick(4), 'a');
    }

    @Test
    public void testReturnChar() throws Exception {
        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }
        for (int i = 0; i < 3; i++) {
            buffer.returnChar();
        }
        assertEquals('4', buffer.getChar());
    }

    @Test
    public void testGetRow() throws Exception {
        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }

        assertEquals(0, buffer.getRow());

        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }
        assertEquals(1, buffer.getRow());


    }

    @Test
    public void testGetColumn() throws Exception {
        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }
        assertEquals(3, buffer.getColumn());
        for (int i = 0; i < 3; i++) {
            buffer.getChar();
        }
        assertEquals(2, buffer.getColumn());
    }
}
