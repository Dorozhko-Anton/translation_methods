package ru.ccfit.nsu.dorozhko.translation_methods;

/**
 * Created by Anton on 20.03.14.
 */
public interface IBuffer {
    public int getChar();

    public int pick(int index);

    public void returnChar();

    public int getRow();

    public int getColumn();

}
