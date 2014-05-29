package ru.ccfit.nsu.dorozhko.translation_methods;

import org.junit.Before;
import org.junit.Test;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Program;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Created by Anton on 03.04.14.
 */
public class ProgramParserTest {
    private IBuffer bufferRight;
    private Lexer lexerRight;
    private Parser parser;
    private Program program;


    private IBuffer bufferWithExpression;
    private Lexer lexerWithExpression;
    private Parser expressionParser;

    @Before
    public void setUp() throws Exception {
        bufferRight = new Buffer(new StringReader("double ICE(int arg) {\n" +
                "\tx = 5;\n" +
                "\treturn 8.3;\n" +
                "}"));

        lexerRight = new Lexer(bufferRight);
        parser = new ProgramParser(lexerRight);


        bufferWithExpression = new Buffer(new StringReader("(2^2)/2+2*2+10*(20-10*2/4/5+10/10-10)+2.*2+10.2"));
        lexerWithExpression = new Lexer(bufferWithExpression);
        expressionParser = new ProgramParser(lexerWithExpression);

    }

    @Test
    public void testXMLoutput() throws Exception {
        StreamResult streamResult = new StreamResult(new File("IRT.xml"));
        program = parser.parseProgram();
        program.outputIRT(streamResult);
    }


//    @Test
//    public void testExpressionCompile() throws Exception {
//        PrintWriter writer = new PrintWriter("test.j", "UTF-8");
//
//        Expression expr = expressionParser.parseExpression();
//        CodeGenVisitor visitor = new CodeGenVisitor();
//        visitor.visit(expr);
//
//        writer.write(".class public HelloWorld\n" +
//                "    .super java/lang/Object\n" +
//                "\n" +
//                "    ;\n" +
//                "    ; standard initializer (calls java.lang.Object's initializer)\n" +
//                "    ;\n" +
//                "    .method public <init>()V\n" +
//                "       aload_0\n" +
//                "       invokenonvirtual java/lang/Object/<init>()V\n" +
//                "       return\n" +
//                "    .end method\n" +
//                "\n" +
//                "    ;\n" +
//                "    ; main() - prints out Hello World\n" +
//                "    ;\n" +
//                "    .method public static main([Ljava/lang/String;)V\n" +
//                "       .limit stack " + (visitor.getStackDepth() + 2) + "   ; up to two items can be pushed\n" +
//                "\n" +
//                "       ; push System.out onto the stack\n" +
//                "       getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
//                "\n" +
//                "       ; push a expr onto the stack\n");
//
//
//        writer.write(visitor.getJasmin());
//
//        String outputType;
//        if (visitor.getTopIsDoubleStack().pop()) {
//            outputType = "F";
//        } else {
//            outputType = "I";
//        }
//        writer.write(
//                "\n" +
//                        "       ; call the PrintStream.println() method.\n" +
//                        "       invokevirtual java/io/PrintStream/println(" + outputType + ")V\n" +
//                        "\n" +
//                        "       ; done\n" +
//                        "       return\n" +
//                        "    .end method"
//        );
//
//        writer.close();
//    }

    @Test
    public void testProgramCompile() throws Exception {
//        Buffer buffer = new Buffer(new StringReader("void voidMethod(int arg1, int arg2) {\n" +
//                "\t\n" +
//                "}\n" +
//                "\n" +
//                "int intMethod(int a) {\n" +
//                "\treturn a + 1;\n" +
//                "}\n" +
//                "\n" +
//                "double doubleMethod(int a, double b) {\n" +
//                "\treturn b + intMethod(a);\n" +
//                "}\n" +
//                "/* skamdaskdkasndaksdn */" +
//                "\n" +
//                "void main() {\n" +
//                "\tvoidMethod(1, 2);\n" +
//                "\t\n" +
//                "\tdouble b;\n" +
//                "\tb = doubleMethod(intMethod(1), 4) + 23;\n" +
//                "\twhile (b > 0) {\n" +
//                "\t\tprint(b);\n" +
//                "\t\tb = b - 1;\t\n" +
//                "\t};" +
//                "if ( b > 0 ) {\n" +
//                "\t\tb = 100.1;\n" +
//                "\t\tprint(b);\n" +
//                "\t} else {\n" +
//                "\t\tb = -100.1;\n" +
//                "\t\tprint(b);\n" +
//                "\t}" +
//                "}"
//                ));

        //Buffer buffer = new Buffer(new StringReader(prog1));
        Buffer buffer = new Buffer(new StringReader(fibonacci));
        Lexer lexer = new Lexer(buffer);
        ProgramParser programParser = new ProgramParser(lexer);


        CodeGenVisitor visitor = new CodeGenVisitor();
        programParser.parseProgram().acceptVisitor(visitor);

        PrintWriter writer = new PrintWriter("JasperTest.j", "UTF-8");

        writer.write(".source                  JasperTest.j\n" +
                ".class                   public JasperTest\n" +
                ".super                   java/lang/Object\n" +
                "\n" +
                ".method                  public <init>()V\n" +
                "   .limit stack          1\n" +
                "   .limit locals         1\n" +
                "   aload_0\n" +
                "   invokespecial         java/lang/Object/<init>()V\n" +
                "   return\n" +
                ".end method   \n");
        writer.write(visitor.getJasmin());

        writer.close();
    }

    public static final String prog1 = "void voidMethod(int arg1, int arg2) {\n" +
            "\t\n" +
            "}\n" +
            "\n" +
            "int intMethod(int a) {\n" +
            "\treturn a + 1;\n" +
            "}\n" +
            "\n" +
            "double doubleMethod(int a, double b) {\n" +
            "\treturn b + intMethod(a);\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "\tvoidMethod(1, 2);\n" +
            "\t\n" +
            "\tdouble b;\n" +
            "\tb = doubleMethod(intMethod(1), 4)+23;\n" +
            "\n" +
            "int r;\n" +
            "\tr = 1024;" +
            "\twhile ( r > 1 ) {\n" +
            "\t\tr = r / 2;\n" +
            "\t\tprint(r);\n" +
            "\t}\n" +
            "\n" +
            "\twhile (b > 0) {\n" +
            "\t\tprint(b);\n" +
            "\t\tb = b - 1;\t\n" +
            "\t}\t\n" +
            "\n" +
            "\tif ( b > 0 ) {\n" +
            "\t\tb = 100.1;\n" +
            "\t\tprint(b);\n" +
            "\t} else {\n" +
            "\t\tb = -100.1;\n" +
            "\t\tprint(b);\n" +
            "\t}\n" +
            "\t\n" +
            "}";

    public static final String fibonacci =
            "int fibonacci(int number) {\n" +
                    "\tif (number == 0) {\n" +
                    "\t\treturn 0;\n" +
                    "\t}\n" +
                    "\tif (number == 1) {\n" +
                    "\t\treturn 1;\n" +
                    "\t}\n" +
                    "\treturn fibonacci(number - 1) + fibonacci(number - 2);\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "\n" +
                    "\tint b;\n" +
                    "\tb = 0;\n" +
                    "\twhile (b != 20) {\n" +
                    "\t\tprint(fibonacci(b));\n" +
                    "\t\tb = b + 1;\n" +
                    "\t}\n" +
                    "\t\n" +
                    "}";
}
