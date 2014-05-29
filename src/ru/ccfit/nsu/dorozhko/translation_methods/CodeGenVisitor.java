package ru.ccfit.nsu.dorozhko.translation_methods;

import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.*;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.MethodCallAtom;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.NameAtom;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.NumberAtom;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Anton on 17.04.14.
 */
public class CodeGenVisitor {
    private int stackDepth = 0;
    private int currentStackDepth = 0;
    private Stack<Boolean> topIsDoubleStack = new Stack<Boolean>();
    private StringWriter output = new StringWriter();
    private static HashMap<String, Variable> variables = new HashMap<String, Variable>();
    private static HashMap<String, MethodSignature> functions = new HashMap<String, MethodSignature>();
    private static int labelNumber = 0;
    private static int localsNumber = 0;
    private boolean hasReturnStatement = false;


    public Stack<Boolean> getTopIsDoubleStack() {
        return topIsDoubleStack;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public String getJasmin() {
        return output.getBuffer().toString();
    }

    public void visit(Expression expression) throws IOException {
        int i = 0;
        expression.getTermList().get(i).acceptVisitor(this);
        i++;
        while (i < expression.getTermList().size()) {
            expression.getTermList().get(i).acceptVisitor(this);
            boolean op1isDouble = topIsDoubleStack.pop();
            boolean op2isDouble = topIsDoubleStack.pop();
            boolean resIsDouble = false;

            if (op1isDouble != op2isDouble) {
                if (op2isDouble) {

                    output.write("i2d\n");

                    currentStackDepth++;
                    if (stackDepth < currentStackDepth) {
                        stackDepth = currentStackDepth;
                    }

                } else {
                    // TODO: swap int and double on stack

                    output.write("dup2_x1\n");
                    output.write("pop2\n");
                    output.write("i2d\n");
                    // and return back
                    output.write("dup2_x2\n");
                    output.write("pop2\n");

                    if (stackDepth - 3 < currentStackDepth) {
                        stackDepth += 3 - (stackDepth - currentStackDepth);
                    }
                    currentStackDepth++;
                }
                resIsDouble = true;
            } else {
                if (op1isDouble) {
                    resIsDouble = true;
                }
            }
            switch (expression.getOperatios().get(i - 1)) {
                case PLUS:
                    if (resIsDouble) {
                        output.write("dadd\n");
                    } else {
                        output.write("iadd\n");
                    }
                    currentStackDepth--;
                    topIsDoubleStack.push(resIsDouble);

                    break;
                case MINUS:
                    if (resIsDouble) {
                        output.write("dsub\n");

                    } else {
                        output.write("isub\n");

                    }
                    topIsDoubleStack.push(resIsDouble);
                    currentStackDepth--;
                    break;
            }
            i++;
        }
    }

    public void visit(Term term) throws IOException {
        int i = 0;
        term.getFactorList().get(i).acceptVisitor(this);
        i++;
        while (i < term.getFactorList().size()) {
            term.getFactorList().get(i).acceptVisitor(this);
            boolean op1isDouble = topIsDoubleStack.pop();
            boolean op2isDouble = topIsDoubleStack.pop();
            boolean resIsDouble = false;

            if (op1isDouble != op2isDouble) {
                if (op2isDouble) {

                    output.write("i2d\n");
                    if (stackDepth < currentStackDepth) {
                        stackDepth++;
                    }
                    currentStackDepth++;

                } else {
                    // TODO: swap int and double on stack

                    output.write("dup2_x1\n");
                    output.write("pop2\n");
                    output.write("i2d\n");

                    if (stackDepth - 2 < currentStackDepth) {
                        stackDepth += 2 - (stackDepth - currentStackDepth);
                    }
                    currentStackDepth++;
                }
                resIsDouble = true;
            } else {
                if (op1isDouble) {
                    resIsDouble = true;
                }
            }
            switch (term.getOpList().get(i - 1)) {
                case MULTIPLY:
                    if (resIsDouble) {
                        output.write("dmul\n");
                    } else {
                        output.write("imul\n");
                    }
                    topIsDoubleStack.push(resIsDouble);
                    currentStackDepth--;
                    break;
                case DIVIDE:
                    if (resIsDouble) {
                        output.write("ddiv\n");

                    } else {
                        output.write("idiv\n");

                    }
                    topIsDoubleStack.push(resIsDouble);
                    currentStackDepth--;
                    break;
            }
            i++;
        }
    }

    public void visit(Factor factor) throws IOException {
        // base to stack
        factor.base.acceptVisitor(this);
        if (factor.power != null) {
            // power to stack
            boolean doubleBase = topIsDoubleStack.pop();
            if (!doubleBase) {
                output.write("i2d\n");
            }

            factor.power.acceptVisitor(this);

            boolean doublePower = topIsDoubleStack.pop();


            topIsDoubleStack.push(doubleBase);

            if (stackDepth - 2 < currentStackDepth) {
                stackDepth += 2 - (stackDepth - currentStackDepth);
            }

            if (!doublePower) {
                output.write("i2d\n");
            }

            output.write("invokestatic          java/lang/Math/pow(DD)D\n");
        }

    }

    public void visit(Power power) throws IOException {
        power.atom.acceptVisitor(this);
        if ("-".equals(power.sign)) {
            if (topIsDoubleStack.peek()) {
                output.write("dneg\n");
            } else {
                output.write("ineg\n");
            }
        }
    }

    public void visit(NumberAtom atom) throws IOException {
        if (atom.isDouble) {
            output.write("ldc2_w " + String.valueOf(atom.doubleValue) + "\n");
            currentStackDepth += 2;
        } else {
            output.write("ldc " + String.valueOf(atom.intValue) + "\n");
            currentStackDepth++;
        }
        if (stackDepth < currentStackDepth) {
            stackDepth = currentStackDepth;
        }

        topIsDoubleStack.push(atom.isDouble);
    }

    public void visit(Program program) throws IOException {
        for (int i = 0; i < program.getMethodList().size(); i++) {
            program.getMethodList().get(i).acceptVisitor(this);
        }
    }

    public void visit(Method method) throws IOException {

        output.write(".method public static ");
        localsNumber = 0;
        variables.clear();
        StringBuilder methodName = new StringBuilder();

        if ("main".equals(method.getName())) {
            methodName.append("main([Ljava/lang/String;)V");
            output.write(methodName.toString() + "\n");
            localsNumber += 1;

        } else {
            methodName.append(method.getName() + "(");

            List<Arglist.Argument> arglist = method.getArguments().getArgumentList();
            for (int i = 0; i < arglist.size(); i++) {
                Arglist.Argument argument = arglist.get(i);

                Variable variable;
                switch (argument.getType().getType()) {
                    case INT:
                        methodName.append("I");
                        variable = new Variable(argument.getType(), localsNumber++);
                        variable.setInitialized(true);
                        variables.put(argument.getName(), variable);
                        break;
                    case DOUBLE:
                        methodName.append("D");
                        variable = new Variable(argument.getType(), localsNumber++);
                        variable.setInitialized(true);
                        variables.put(argument.getName(), variable);
                        localsNumber += 2;
                        break;
                    case VOID:
                        methodName.append("V");
                        break;
                }

            }
            methodName.append(")");
            switch (method.getReturnType().getType()) {
                case INT:
                    methodName.append("I");
                    break;
                case DOUBLE:
                    methodName.append("D");
                    break;
                case VOID:
                    methodName.append("V");
                    break;
            }
            output.write(methodName.toString());
            output.write("\n");

            functions.put(method.getName(), new MethodSignature(methodName.toString(), method));

        }

        CodeGenVisitor bodyVisitor = new CodeGenVisitor();

        method.getBody().acceptVisitor(bodyVisitor);

        output.write(".limit stack " + bodyVisitor.getStackDepth() + "\n");
        output.write(".limit locals " + localsNumber + "\n");
        //TODO: .var table????

        output.write(bodyVisitor.getJasmin());
        //TODO: check return statement existence ??
        String name = methodName.toString();
        if (!bodyVisitor.hasReturnStatement()) {
            if (name.charAt(name.length() - 1) == 'V') {
                output.write("return\n");
            } else {
                throw new IOException("missing RETURN statement in method: " + name);
            }
        }
        output.write(".end method\n");
    }

    private boolean hasReturnStatement() {
        return hasReturnStatement;
    }

    private int getNumberOfLocals() {
        return localsNumber;
    }

    public void visit(Body body) throws IOException {
        for (int i = 0; i < body.getCommandList().size(); i++) {
            body.getCommandList().get(i).acceptVisitor(this);
        }
    }

    public void visit(AssignmentCommand assignmentCommand) throws IOException {
        assignmentCommand.getExpression().acceptVisitor(this);
        Variable localVar = variables.get(assignmentCommand.getName());
        localVar.setInitialized(true);
        boolean rValueIsFloat = topIsDoubleStack.pop();
        if (rValueIsFloat) {
            switch (localVar.type.getType()) {
                case INT:
                    output.write("i2d\n");
                case DOUBLE:
                    output.write("dstore " + localVar.localNumber + "\n");
                    break;
            }
        } else {
            switch (localVar.type.getType()) {
                case INT:
                    output.write("istore " + localVar.localNumber + "\n");
                    break;
                case DOUBLE:
                    throw new IOException("int is assigned a double value");
            }
        }

    }

    public void visit(MethodCallCommand methodCallCommand) throws IOException {

        if ("print".equals(methodCallCommand.getName())) {
            output.write("getstatic             java/lang/System/out Ljava/io/PrintStream;\n");

            // TODO: method call
            // TODO: prepare parameters
            List<Expression> paramList = methodCallCommand.getParams().getExpressionList();
            if (paramList.size() > 1) {
                throw new IOException("Sorry! you can print only 1 value at a time");
            }
            for (int i = 0; i < paramList.size(); i++) {
                paramList.get(i).acceptVisitor(this);
            }

            // TODO: invoke
            output.write("invokevirtual         java/io/PrintStream/println(");
            if (topIsDoubleStack.pop()) {
                output.write("D");
            } else {
                output.write("I");
            }
            output.write(")V");
            output.write("\n");
        } else {

            String name = functions.get(methodCallCommand.getName()).jasminName;
            if (name == null) {
                throw new IOException("function undefined: " + methodCallCommand.getName());
            }

            switch (name.charAt(name.length() - 1)) {
                case 'I':
                    topIsDoubleStack.push(false);
                    break;
                case 'D':
                    topIsDoubleStack.push(true);
                    break;
                case 'V':
                    break;

            }

            //  method call
            //  prepare parameters

            // TODO: check signature
            List<Expression> paramList = methodCallCommand.getParams().getExpressionList();
            Arglist arglist = functions.get(methodCallCommand.getName()).method.getArguments();

            for (int i = 0; i < paramList.size(); i++) {
                paramList.get(i).acceptVisitor(this);
                Arglist.Argument arg = arglist.getArgumentList().get(i);
                switch (arg.getType().getType()) {
                    case INT:
                        if (topIsDoubleStack.peek()) {
                            throw new IOException("can't cast DOUBLE to INT in method : " + methodCallCommand.getName());
                        }
                        break;
                    case DOUBLE:
                        if (!topIsDoubleStack.peek()) {
                            output.write("i2d\n");
                            currentStackDepth++;
                            if (stackDepth < currentStackDepth) {
                                stackDepth = currentStackDepth;
                            }
                        }
                        break;
                }
                topIsDoubleStack.pop();
            }
            currentStackDepth -= paramList.size() - 1;
            //  invoke
            output.write("invokestatic JasperTest/" + CodeGenVisitor.functions.get(methodCallCommand.getName()).jasminName);
            output.write("\n");
        }

    }

    public void visit(ReturnCommand returnCommand) throws IOException {
        hasReturnStatement = true;
        if (returnCommand.getExpression() == null) {
            output.write("return\n");
        } else {
            returnCommand.getExpression().acceptVisitor(this);
            if (getTopIsDoubleStack().pop()) {
                output.write("dreturn\n");
            } else {
                output.write("ireturn\n");
            }
        }
    }

    public void visit(VariableDefinition variableDefinition) throws IOException {
        if (variables.containsKey(variableDefinition.getName())) {
            throw new IOException("double declaration of variable: " + variableDefinition.getName());
        }
        variables.put(variableDefinition.getName(),
                new Variable(variableDefinition.getT(), localsNumber));
        switch (variableDefinition.getT().getType()) {

            case INT:
                localsNumber++;
                break;
            case DOUBLE:
                localsNumber += 2;
                break;
            case VOID:
                throw new IOException("variable of VOID type, are u kidding ?");
        }

    }

    public void visit(NameAtom nameAtom) throws IOException {
        Variable variable = variables.get(nameAtom.getName());

        if (variable == null) {
            throw new IOException("variable undefined: " + nameAtom.getName());
        }
        if (!variable.isInitialized()) {
            throw new IOException("variable not initialized: " + nameAtom.getName());
        }
        switch (variable.type.getType()) {
            case INT:
                output.write("iload " + variable.localNumber);
                topIsDoubleStack.push(false);
                currentStackDepth++;
                if (stackDepth < currentStackDepth) {
                    stackDepth = currentStackDepth;
                }
                break;
            case DOUBLE:
                currentStackDepth += 2;
                if (stackDepth < currentStackDepth) {
                    stackDepth = currentStackDepth;
                }
                output.write("dload " + variable.localNumber);
                topIsDoubleStack.push(true);
                break;
            case VOID:
                break;
        }
        output.write("\n");

    }

    public void visit(MethodCallAtom methodCallAtom) throws IOException {
        MethodSignature methodSignature = functions.get(methodCallAtom.getName());
        if (methodSignature == null) {
            throw new IOException("function undefined: " + methodCallAtom.getName());
        }
        String name = methodSignature.jasminName;

        switch (name.charAt(name.length() - 1)) {
            case 'I':
                topIsDoubleStack.push(false);
                break;
            case 'D':
                topIsDoubleStack.push(true);
                break;
            case 'V':
                break;

        }
        // method call
        // prepare parameters

        List<Expression> paramList = methodCallAtom.getParamList().getExpressionList();
        Arglist arglist = functions.get(methodCallAtom.getName()).method.getArguments();

        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).acceptVisitor(this);
            Arglist.Argument arg = arglist.getArgumentList().get(i);
            switch (arg.getType().getType()) {
                case INT:
                    if (topIsDoubleStack.peek()) {
                        throw new IOException("can't cast DOUBLE to INT in method : " + methodCallAtom.getName());
                    }
                    break;
                case DOUBLE:
                    if (!topIsDoubleStack.peek()) {
                        output.write("i2d\n");
                        currentStackDepth++;
                        if (stackDepth < currentStackDepth) {
                            stackDepth = currentStackDepth;
                        }
                    }
                    break;
            }
            topIsDoubleStack.pop();
        }
        currentStackDepth -= paramList.size() - 1;
        // invoke
        output.write("invokestatic JasperTest/" + CodeGenVisitor.functions.get(methodCallAtom.getName()).jasminName);
        output.write("\n");
    }

    public void visit(IfStatementCommand ifStatementCommand) throws IOException {
        ifStatementCommand.getExpression1().acceptVisitor(this);
        boolean expr1isDouble = getTopIsDoubleStack().pop();
        if (expr1isDouble == false) {
            output.write("i2d\n");
            currentStackDepth++;
            if (stackDepth < currentStackDepth) {
                stackDepth = currentStackDepth;
            }
        }
        ifStatementCommand.getExpression2().acceptVisitor(this);
        boolean expr2isDouble = getTopIsDoubleStack().pop();
        if (expr2isDouble == false) {
            output.write("i2d\n");
            currentStackDepth++;
            if (stackDepth < currentStackDepth) {
                stackDepth = currentStackDepth;
            }
        }
        output.write("dcmpg \n");
        currentStackDepth -= 3;

        int falseLabel = labelNumber++;


        switch (ifStatementCommand.getOp()) {
            case LESS:
                output.write("ifge " + "LABEL" + falseLabel + "\n");
                break;
            case MORE_OR_EQUALS:
                output.write("iflt " + "LABEL" + falseLabel + "\n");
                break;
            case MORE:
                output.write("ifle " + "LABEL" + falseLabel + "\n");
                break;
            case LESS_OR_EQUALS:
                output.write("ifgt " + "LABEL" + falseLabel + "\n");
                break;
            case DOUBLE_EQUALS:
                output.write("ifne " + "LABEL" + falseLabel + "\n");
                break;
            case NOT_EQUALS:
                output.write("ifeq " + "LABEL" + falseLabel + "\n");
                break;
        }
        ifStatementCommand.getBody().acceptVisitor(this);

        if (ifStatementCommand.getElseBody() != null) {
            int endIfLabel = labelNumber++;
            output.write("goto LABEL" + endIfLabel + "\n");
            output.write("LABEL" + falseLabel + ":\n");
            ifStatementCommand.getElseBody().acceptVisitor(this);
            output.write("LABEL" + endIfLabel + ":\n");
        } else {
            output.write("LABEL" + falseLabel + ":\n");
        }
    }

    public void visit(WhileStatementCommand whileStatementCommand) throws IOException {
        int whileBeginLabel = labelNumber++;

        output.write("LABEL" + whileBeginLabel + ":\n");

        whileStatementCommand.getExpression1().acceptVisitor(this);
        boolean expr1isDouble = getTopIsDoubleStack().pop();
        if (expr1isDouble == false) {
            output.write("i2d\n");
            currentStackDepth++;
            if (stackDepth < currentStackDepth) {
                stackDepth = currentStackDepth;
            }
        }
        whileStatementCommand.getExpression2().acceptVisitor(this);
        boolean expr2isDouble = getTopIsDoubleStack().pop();
        if (expr2isDouble == false) {
            output.write("i2d\n");
            currentStackDepth++;
            if (stackDepth < currentStackDepth) {
                stackDepth = currentStackDepth;
            }
        }
        output.write("dcmpg \n");
        currentStackDepth -= 3; // get 2 double return 1 int

        int whileEndLabel = labelNumber++;


        switch (whileStatementCommand.getOp()) {
            case LESS:
                output.write("ifge " + "LABEL" + whileEndLabel + "\n");
                break;
            case MORE_OR_EQUALS:
                output.write("iflt " + "LABEL" + whileEndLabel + "\n");
                break;
            case MORE:
                output.write("ifle " + "LABEL" + whileEndLabel + "\n");
                break;
            case LESS_OR_EQUALS:
                output.write("ifgt " + "LABEL" + whileEndLabel + "\n");
                break;
            case DOUBLE_EQUALS:
                output.write("ifne " + "LABEL" + whileEndLabel + "\n");
                break;
            case NOT_EQUALS:
                output.write("ifeq " + "LABEL" + whileEndLabel + "\n");
                break;
        }
        whileStatementCommand.getBody().acceptVisitor(this);
        output.write("goto LABEL" + whileBeginLabel + "\n");
        output.write("LABEL" + whileEndLabel + ":\n");
    }

    private class Variable {
        public Type type;
        public int localNumber;

        public boolean isInitialized() {
            return initialized;
        }

        public void setInitialized(boolean initialized) {
            this.initialized = initialized;
        }

        public boolean initialized = false;

        private Variable(Type type, int localNumber) {
            this.type = type;
            this.localNumber = localNumber;
        }
    }

    private class MethodSignature {
        public String jasminName;
        public Method method;

        private MethodSignature(String jasminName, Method method) {
            this.jasminName = jasminName;
            this.method = method;
        }
    }
}
