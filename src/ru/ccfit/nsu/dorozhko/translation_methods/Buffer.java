package ru.ccfit.nsu.dorozhko.translation_methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Anton on 20.03.14.
 */
public class Buffer implements IBuffer {
    private String buffer;
    private int currentPos = 0;
    private int row = 0;
    private int column = 0;

    public Buffer(Reader reader) {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = builder.toString();
    }

    @Override
    public int getChar() {
        if (currentPos == buffer.length()) {
            return -1;
        }
        if (buffer.charAt(currentPos) == '\n') {
            row++;
            column = 0;
        } else {
            column++;
        }
        int res = buffer.charAt(currentPos);
        currentPos++;
        return res;
    }

    @Override
    public int pick(int index) {
        if (currentPos + index >= buffer.length()) {
            return -1;
        }
        return buffer.charAt(currentPos + index);
    }

    @Override
    public void returnChar() {
        currentPos--;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
