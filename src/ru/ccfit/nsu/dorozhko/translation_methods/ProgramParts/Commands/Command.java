package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts.Commands;

import ru.ccfit.nsu.dorozhko.translation_methods.CodeGenVisitor;
import ru.ccfit.nsu.dorozhko.translation_methods.XMLable;

import java.io.IOException;

/**
 * Created by Anton on 03.04.14.
 */
public abstract class Command implements XMLable {
    public abstract void acceptVisitor(CodeGenVisitor visitor) throws IOException;
}
