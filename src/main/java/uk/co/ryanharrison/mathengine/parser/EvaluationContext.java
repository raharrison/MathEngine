package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.Utils;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.Operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EvaluationContext {

    private AngleUnit angleUnit = AngleUnit.Radians;
    private Map<String, NodeConstant> constants = new HashMap<>();
    private Map<String, Operator> operators = new HashMap<>();

    void fillOperators(Collection<Operator> ops) {
        for (Operator operator : ops) {
            addOperator(operator);
        }
    }

    void addOperator(Operator operator) {
        for (String alias : operator.getAliases()) {
            operators.put(alias, operator);
        }
    }

    int findLongestOperator() {
        return operators.keySet().stream()
                .mapToInt(String::length)
                .max().orElse(0);
    }

    boolean isOperator(String str) {
        return operators.containsKey(Utils.standardiseString(str));
    }

    Operator getOperator(String str) {
        return operators.get(Utils.standardiseString(str));
    }

    void addConstant(String variable, NodeConstant constant) {
        if (!operators.containsKey(Utils.standardiseString(variable)))
            constants.put(variable, constant);
        else
            throw new IllegalArgumentException("Constant is an operator");
    }

    NodeConstant getConstant(String variable) {
        return constants.get(variable);
    }

    boolean isConstant(String variable) {
        return constants.containsKey(variable);
    }

    void clearConstants() {
        constants.clear();
    }

    public AngleUnit getAngleUnit() {
        return angleUnit;
    }

    void setAngleUnit(AngleUnit angleUnit) {
        this.angleUnit = angleUnit;
    }

    public NodeConstant evaluateFunc(Node node, Map<String, NodeConstant> args) {
        throw new UnsupportedOperationException("Cannot eval functions right now");
    }
}
