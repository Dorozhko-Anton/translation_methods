package ru.ccfit.nsu.dorozhko.translation_methods;

import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.*;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms.Atom;
import ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands.Command;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public interface Parser {
    Program parseProgram() throws IOException;

    Method parseMethod() throws IOException;

    Type parseType() throws IOException;

    Arglist parseArglist() throws IOException;

    Body parseBody() throws IOException;

    Command parseCommand() throws IOException;

    ParamList parseParamList() throws IOException;

    Expression parseExpression() throws IOException;

    Term parseTerm() throws IOException;

    Factor parseFactor() throws IOException;

    Power parsePower() throws IOException;

    Atom parseAtom() throws IOException;


}
