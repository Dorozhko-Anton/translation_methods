package ru.ccfit.nsu.dorozhko.translation_methods.ProgramParts;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton on 03.04.14.
 */
public class ParamList {
    private List<Expression> expressionList = new LinkedList<Expression>();


    public List<Expression> getExpressionList() {
        return expressionList;
    }
}
