package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

/**
 * Created by Anton on 03.04.14.
 */
public class Type {
    private PossibleType type;

    public PossibleType getType() {
        return type;
    }

    public void setType(PossibleType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum PossibleType {
        INT("INT"),
        DOUBLE("DOUBLE"),
        VOID("VOID");
        private final String value;

        PossibleType(final String s) {
            this.value = s;

        }

        @Override
        public String toString() {
            return value;
        }

    }
}
