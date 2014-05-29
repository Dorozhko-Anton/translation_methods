package ru.ccfit.nsu.dorozhko.translation_methods;

import java.io.IOException;

/**
 * Created by Anton on 20.03.14.
 */
public class Lexer {
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
                return new Token(Type.MINUS, "-", buffer.getRow(), buffer.getColumn());
            case '*':
                return new Token(Type.MULTIPLY, "*", buffer.getRow(), buffer.getColumn());
            case '/':
                return new Token(Type.DIVIDE, "/", buffer.getRow(), buffer.getColumn());
            case '^':
                return new Token(Type.POWER, "^", buffer.getRow(), buffer.getColumn());
            case '!':
                if (buffer.pick(0) == '=') {
                    buffer.getChar();
                    return new Token(Type.NOT_EQUALS, "!=", buffer.getRow(), buffer.getColumn());
                } else {
                    throw new IOException("Lexer error row: " + buffer.getRow() + "column: " + buffer.getColumn());
                }
            case '=':
                if (buffer.pick(0) == '=') {
                    buffer.getChar();
                    return new Token(Type.DOUBLE_EQUALS, "==", buffer.getRow(), buffer.getColumn());
                }
                return new Token(Type.EQUALS, "=", buffer.getRow(), buffer.getColumn());
            case ',':
                return new Token(Type.COMMA, ",", buffer.getRow(), buffer.getColumn());
            case ';':
                return new Token(Type.SEMICOLON, ";", buffer.getRow(), buffer.getColumn());
            case '{':
                return new Token(Type.LEFT_BRACES, "{", buffer.getRow(), buffer.getColumn());
            case '}':
                return new Token(Type.RIGHT_BRACES, "}", buffer.getRow(), buffer.getColumn());
            case '<':
                if (buffer.pick(0) == '=') {
                    buffer.getChar();
                    return new Token(Type.LESS_OR_EQUALS, "<=", buffer.getRow(), buffer.getColumn());
                }
                return new Token(Type.LESS, "<", buffer.getRow(), buffer.getColumn());
            case '>':
                if (buffer.pick(0) == '=') {
                    buffer.getChar();
                    return new Token(Type.MORE_OR_EQUALS, ">=", buffer.getRow(), buffer.getColumn());
                }
                return new Token(Type.MORE, ">", buffer.getRow(), buffer.getColumn());
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
                    while (Character.isAlphabetic(c) || Character.isDigit(c)) {
                        builder.append((char) c);
                        c = buffer.getChar();
                    }
                    buffer.returnChar();

                    String word = builder.toString();
                    if ("int".equals(word)) {
                        return new Token(Type.INT, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("double".equals(word)) {
                        return new Token(Type.DOUBLE, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("void".equals(word)) {
                        return new Token(Type.VOID, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("return".equals(word)) {
                        return new Token(Type.RETURN, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("if".equals(word)) {
                        return new Token(Type.IF, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("else".equals(word)) {
                        return new Token(Type.ELSE, word, buffer.getRow(), buffer.getColumn());
                    }
                    if ("while".equals(word)) {
                        return new Token(Type.WHILE, word, buffer.getRow(), buffer.getColumn());
                    }


                    return new Token(Type.NAME, builder.toString(), buffer.getRow(), buffer.getColumn());
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
                            while (!((buffer.getChar() == '*') && (buffer.pick(0) == '/'))) {
                                if (buffer.pick(0) == -1) {
                                    throw new IOException("Wrong multiline commentary");
                                }
                            }
                            buffer.getChar();
                            break;
                        default:
                            buffer.returnChar();
                            return;
                    }
                    break;
                default:
                    return;
            }
        }
    }

    public static enum Type {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
        EQUALS,
        NAME,
        NUMBER,
        POWER,
        COMMA,
        SEMICOLON,
        LEFT_BRACES,
        RIGHT_BRACES,
        END_OF_STREAM,
        RETURN,
        INT,
        DOUBLE,
        VOID,

        IF, ELSE,
        WHILE,
        LESS,
        LESS_OR_EQUALS,
        MORE,
        MORE_OR_EQUALS,
        DOUBLE_EQUALS,
        NOT_EQUALS
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
}
