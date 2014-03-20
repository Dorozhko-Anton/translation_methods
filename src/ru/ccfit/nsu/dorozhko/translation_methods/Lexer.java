package ru.ccfit.nsu.dorozhko.translation_methods;

import java.io.IOException;

/**
 * Created by Anton on 20.03.14.
 */
public class Lexer {
    public static enum Type {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
        EQUALS,
        ATOM,
        NUMBER,
        POWER,
        COMMA,
        SEMICOLON,
        LEFT_BRACES,
        RIGHT_BRACES,
        END_OF_STREAM
    }

    public static class Token {
        public final Type type;
        public final String c;
        public final int row;
        public final int column;

        public Token(Type type, String c, int row, int column) {
            this.type = type;
            this.c = c;
            this.row = row;
            this.column = column;
        }
    }

    private IBuffer buffer;

    public Lexer(IBuffer buffer) {
        this.buffer = buffer;
    }

    public Token getToken() throws IOException {
        readSpacesAndComments();
        int c = buffer.getChar();
        switch (c) {
            case '(':
                return new Token(Type.LEFT_PARENTHESIS, "(", buffer.getRow(), buffer.getColumn());
            case ')':
                return new Token(Type.RIGHT_PARENTHESIS, ")", buffer.getRow(), buffer.getColumn());
            case '+':
                return new Token(Type.PLUS, "+", buffer.getRow(), buffer.getColumn());
            case '-':
                return new Token(Type.MINUS, "+", buffer.getRow(), buffer.getColumn());
            case '*':
                return new Token(Type.MULTIPLY, "*", buffer.getRow(), buffer.getColumn());
            case '/':
                return new Token(Type.DIVIDE, "/", buffer.getRow(), buffer.getColumn());
            case '^':
                return new Token(Type.POWER, "^", buffer.getRow(), buffer.getColumn());
            case '=':
                return new Token(Type.EQUALS, "=", buffer.getRow(), buffer.getColumn());
            case ',':
                return new Token(Type.COMMA, ",", buffer.getRow(), buffer.getColumn());
            case ';':
                return new Token(Type.SEMICOLON, ";", buffer.getRow(), buffer.getColumn());
            case '{':
                return new Token(Type.LEFT_BRACES, "{", buffer.getRow(), buffer.getColumn());
            case '}':
                return new Token(Type.RIGHT_BRACES, "}", buffer.getRow(), buffer.getColumn());
            case -1:
                return new Token(Type.END_OF_STREAM, "", buffer.getRow(), buffer.getColumn());
            default:
                StringBuilder builder = new StringBuilder();
                if (Character.isDigit(c)) {
                    while (Character.isDigit(c)) {
                        builder.append((char) c);
                        c = buffer.getChar();
                    }
                    if (c == '.') {
                        builder.append((char) c);
                        c = buffer.getChar();
                        while (Character.isDigit(c)) {
                            builder.append((char) c);
                            c = buffer.getChar();
                        }
                    }
                    buffer.returnChar();
                    return new Token(Type.NUMBER, builder.toString(), buffer.getRow(), buffer.getColumn());
                }

                if (Character.isAlphabetic(c)) {
                    while (Character.isAlphabetic(c)) {
                        builder.append((char) c);
                        c = buffer.getChar();
                    }
                    buffer.returnChar();
                    return new Token(Type.ATOM, builder.toString(), buffer.getRow(), buffer.getColumn());
                }
                throw new IOException("Lexer error row: " + buffer.getRow() + "column: " + buffer.getColumn());
        }
    }

    public void readSpacesAndComments() throws IOException {
        int c;
        while (true) {
            c = buffer.pick(0);

            switch (c) {
                case ' ':
                case '\t':
                case '\n':
                    buffer.getChar();
                    break;
                case '/':
                    buffer.getChar();
                    switch (buffer.pick(0)) {
                        case '/':
                            while (buffer.getChar() != '\n') {
                            }
                            break;
                        case '*':
                            while (!(buffer.getChar() == '*' && buffer.pick(1) == '/')) {
                                if (buffer.getChar() == -1) {
                                    throw new IOException("Wrong multiline commentary");
                                }
                            }
                            break;
                        default:
                            throw new IOException("Wrong commentary");
                    }
                    break;
                default:
                    return;
            }
        }
    }
}
