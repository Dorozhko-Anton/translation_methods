package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Atoms;

import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public abstract class Atom implements XMLable {

    public abstract void acceptVisitor(CodeGenVisitor visitor) throws IOException;
}
