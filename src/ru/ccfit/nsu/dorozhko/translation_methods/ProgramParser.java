package ru.ccfit.nsu.dorozhko.translation_methods;

import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.*;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.*;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands.*;

import java.io.IOException;

import static ru.ccfit.nsu.dorozhko.translation_methods.Lexer.Token;
import static ru.ccfit.nsu.dorozhko.translation_methods.Lexer.Type.*;

/**
 * Created by Anton on 03.04.14.
 */
public class ProgramParser implements Parser {
    private Lexer lexer;
    private Token curToken;

    public ProgramParser(Lexer lexer) {
        this.lexer = lexer;
        try {
            curToken = lexer.getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Program parseProgram() throws IOException {

        Program p = new Program();

        while (curToken.type != Lexer.Type.END_OF_STREAM) {
            Method m = parseMethod();
            p.getMethodList().add(m);
        }

        return p;
    }

    @Override
    public Method parseMethod() throws IOException {
        Method m = new Method();
        m.setReturnType(parseType());

        if (curToken.type == Lexer.Type.NAME) {
            m.setName(curToken.c);
            curToken = lexer.getToken();
        } else {
            throw new IOException("missing method name");
        }

        if (curToken.type == Lexer.Type.LEFT_PARENTHESIS) {
            curToken = lexer.getToken();
            m.setArguments(parseArglist());
        } else {
            throw new IOException("missing ( after method name");
        }

        if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
            curToken = lexer.getToken();
        } else {
            throw new IOException("missing )");
        }

        if (curToken.type == Lexer.Type.LEFT_BRACES) {
            curToken = lexer.getToken();
            m.setBody(parseBody());
        } else {
            throw new IOException("missing {");
        }

        if (curToken.type == Lexer.Type.RIGHT_BRACES) {
            curToken = lexer.getToken();
        } else {
            throw new IOException("missing }");
        }
        return m;
    }

    @Override
    public Type parseType() throws IOException {
        Type t = new Type();

        switch (curToken.type) {
            case INT:
                curToken = lexer.getToken();
                t.setType(Type.PossibleType.INT);
                break;
            case DOUBLE:
                curToken = lexer.getToken();
                t.setType(Type.PossibleType.DOUBLE);
                break;
            case VOID:
                curToken = lexer.getToken();
                t.setType(Type.PossibleType.VOID);
                break;
            default:
                throw new IOException("type expected");
        }
        return t;
    }

    @Override
    public Arglist parseArglist() throws IOException {
        if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
            return null;
        }
        return parseNotEmptyArglist();
    }

    private Arglist parseNotEmptyArglist() throws IOException {
        Arglist a = new Arglist();

        Arglist.Argument arg = new Arglist.Argument();
        arg.setType(parseType());

        if (curToken.type == Lexer.Type.NAME) {
            arg.setName(curToken.c);
            curToken = lexer.getToken();
        } else {
            throw new IOException("argument name expected");
        }
        a.getArgumentList().add(arg);

        while (curToken.type == Lexer.Type.COMMA) {
            curToken = lexer.getToken();

            arg = new Arglist.Argument();
            arg.setType(parseType());

            if (curToken.type == Lexer.Type.NAME) {
                arg.setName(curToken.c);
                curToken = lexer.getToken();
            } else {
                throw new IOException("argument name expected");
            }
            a.getArgumentList().add(arg);
        }

        return a;

    }

    @Override
    public Body parseBody() throws IOException {
        Body b = new Body();

        while (curToken.type != Lexer.Type.RIGHT_BRACES) {
            Command command = parseCommand();
            b.getCommandList().add(command);
            if (curToken.type == SEMICOLON) {
                curToken = lexer.getToken();
            } else {
                if (command.getClass() != IfStatementCommand.class
                        && command.getClass() != WhileStatementCommand.class) {
                    throw new IOException("missing ;");
                }
            }
        }

        return b;
    }

    @Override
    public Command parseCommand() throws IOException {
        Command c = null;
        switch (curToken.type) {
            case INT:
            case DOUBLE:
            case VOID:
                Type t = parseType();
                //curToken = lexer.getToken();
                if (curToken.type == Lexer.Type.NAME) {
                    c = new VariableDefinition(t, curToken.c);
                    curToken = lexer.getToken();
                }
                break;
            case RETURN:
                curToken = lexer.getToken();
                if (curToken.type == Lexer.Type.SEMICOLON) {
                    c = new ReturnCommand(null);

                    break;
                }
                c = new ReturnCommand(parseExpression());
                break;
            case NAME:
                String name = curToken.c;
                curToken = lexer.getToken();

                switch (curToken.type) {
                    case EQUALS:
                        curToken = lexer.getToken();
                        c = new AssignmentCommand(name, parseExpression());
                        break;
                    case LEFT_PARENTHESIS:
                        curToken = lexer.getToken();
                        c = new MethodCallCommand(name, parseParamList());
                        if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
                            curToken = lexer.getToken();
                        } else {
                            throw new IOException("missing )");
                        }
                        break;
                }
                break;
            case IF:
                curToken = lexer.getToken();
                if (curToken.type != LEFT_PARENTHESIS) {
                    throw new IOException("missing (");
                }
                curToken = lexer.getToken();
                IfStatementCommand ifStatementCommand = new IfStatementCommand();
                ifStatementCommand.setExpression1(parseExpression());
                switch (curToken.type) {
                    case LESS:
                    case MORE:
                    case DOUBLE_EQUALS:
                    case LESS_OR_EQUALS:
                    case MORE_OR_EQUALS:
                    case NOT_EQUALS:
                        ifStatementCommand.setOp(curToken.type);
                        curToken = lexer.getToken();
                        break;
                    default:
                        throw new IOException("expected < > ==");
                }
                ifStatementCommand.setExpression2(parseExpression());
                if (curToken.type != RIGHT_PARENTHESIS) {
                    throw new IOException("missing )");
                }
                curToken = lexer.getToken();
                if (curToken.type != LEFT_BRACES) {
                    throw new IOException("missing {");
                }
                curToken = lexer.getToken();
                ifStatementCommand.setBody(parseBody());
                if (curToken.type != RIGHT_BRACES) {
                    throw new IOException("missing {");
                }
                curToken = lexer.getToken();
                if (curToken.type == ELSE) {
                    curToken = lexer.getToken();
                    if (curToken.type != LEFT_BRACES) {
                        throw new IOException("missing {");
                    }
                    curToken = lexer.getToken();
                    ifStatementCommand.setElseBody(parseBody());
                    if (curToken.type != RIGHT_BRACES) {
                        throw new IOException("missing {");
                    }
                    curToken = lexer.getToken();

                }
                return ifStatementCommand;
            case WHILE:
                curToken = lexer.getToken();
                if (curToken.type != LEFT_PARENTHESIS) {
                    throw new IOException("missing (");
                }
                curToken = lexer.getToken();
                WhileStatementCommand whileStatementCommand = new WhileStatementCommand();
                whileStatementCommand.setExpression1(parseExpression());
                switch (curToken.type) {
                    case LESS:
                    case MORE:
                    case DOUBLE_EQUALS:
                    case LESS_OR_EQUALS:
                    case MORE_OR_EQUALS:
                    case NOT_EQUALS:
                        whileStatementCommand.setOp(curToken.type);
                        curToken = lexer.getToken();
                        break;
                    default:
                        throw new IOException("expected < > ==");
                }
                whileStatementCommand.setExpression2(parseExpression());
                if (curToken.type != RIGHT_PARENTHESIS) {
                    throw new IOException("missing )");
                }
                curToken = lexer.getToken();
                if (curToken.type != LEFT_BRACES) {
                    throw new IOException("missing {");
                }
                curToken = lexer.getToken();
                whileStatementCommand.setBody(parseBody());
                if (curToken.type != RIGHT_BRACES) {
                    throw new IOException("missing {");
                }
                curToken = lexer.getToken();
                return whileStatementCommand;
        }

        return c;
    }

    @Override
    public ParamList parseParamList() throws IOException {
        if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
            return null;
        }
        return parseNotEmptyParamList();
    }

    private ParamList parseNotEmptyParamList() throws IOException {
        ParamList paramList = new ParamList();

        paramList.getExpressionList().add(parseExpression());

        while (curToken.type == Lexer.Type.COMMA) {
            curToken = lexer.getToken();
            paramList.getExpressionList().add(parseExpression());
        }

        return paramList;
    }

    @Override
    public Expression parseExpression() throws IOException {
        Expression expression = new Expression();
        expression.getTermList().add(parseTerm());
        while (curToken.type == PLUS ||
                curToken.type == MINUS) {
            switch (curToken.type) {
                case PLUS:
                    curToken = lexer.getToken();
                    expression.getOperatios().add(PLUS);
                    expression.getTermList().add(parseTerm());
                    break;
                case MINUS:
                    curToken = lexer.getToken();
                    expression.getOperatios().add(MINUS);
                    expression.getTermList().add(parseTerm());
                    break;
            }
        }

        return expression;
    }

    @Override
    public Term parseTerm() throws IOException {
        Term term = new Term();

        term.getFactorList().add(parseFactor());
        while (curToken.type == DIVIDE ||
                curToken.type == MULTIPLY) {
            switch (curToken.type) {
                case DIVIDE:
                    curToken = lexer.getToken();
                    term.getOpList().add(DIVIDE);
                    term.getFactorList().add(parseFactor());
                    break;
                case MULTIPLY:
                    curToken = lexer.getToken();
                    term.getOpList().add(MULTIPLY);
                    term.getFactorList().add(parseFactor());
                    break;

            }
        }
        return term;
    }

    @Override
    public Factor parseFactor() throws IOException {
        Factor result = new Factor();
        result.setBase(parsePower());

        if (curToken.type == Lexer.Type.POWER) {
            curToken = lexer.getToken();
            result.setPower(parseFactor());
        }

        return result;
    }

    @Override
    public Power parsePower() throws IOException {
        Power power = new Power();

        if (curToken.type == MINUS) {
            power.setSign(curToken.c);
            curToken = lexer.getToken();

        }
        power.setAtom(parseAtom());
        return power;
    }

    @Override
    public Atom parseAtom() throws IOException {
        Atom atom = null;

        switch (curToken.type) {
            case NUMBER:
                if (curToken.c.contains(".")) {
                    atom = new NumberAtom(Double.parseDouble(curToken.c));
                } else {
                    atom = new NumberAtom(Integer.parseInt(curToken.c));
                }
                curToken = lexer.getToken();
                break;
            case NAME:
                String name = curToken.c;

                curToken = lexer.getToken();
                if (curToken.type == Lexer.Type.LEFT_PARENTHESIS) {
                    curToken = lexer.getToken();
                    ParamList paramList = parseParamList();
                    if (curToken.type == Lexer.Type.RIGHT_PARENTHESIS) {
                        curToken = lexer.getToken();
                    } else {
                        throw new IOException("missing RIGHT_PARENTHESIS - ) : "
                                + curToken.row + ":" + curToken.column);
                    }
                    atom = new MethodCallAtom(name, paramList);
                } else {
                    atom = new NameAtom(name);
                }

                break;
            case LEFT_PARENTHESIS:
                curToken = lexer.getToken();
                atom = new ExpressionAtom(parseExpression());

                if (curToken.type == RIGHT_PARENTHESIS) {
                    curToken = lexer.getToken();
                } else {
                    throw new IOException("missing )");
                }
                break;
            default:
                throw new IOException("missing atom");
        }

        return atom;
    }
}
