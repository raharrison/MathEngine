package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.linearalgebra.Vector;
import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.Operator;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class RecursiveDescentParser implements Parser<Node, NodeConstant> {

    private Map<String, NodeConstant> constants;
    private EvaluationContext context = EvaluationContext.defaultContext();

    RecursiveDescentParser() {
        constants = new HashMap<>();
        fillDefaultConstants();
    }

    public void addConstant(String constant, NodeConstant value) {
        if (constant != null && !constant.equals("")) {
            constants.put(constant, value);
        } else {
            throw new IllegalArgumentException("Constant is null or empty");
        }
    }

    public void removeConstant(String constant) {
        constants.remove(constant);
    }

    public NodeConstant getConstantFromKey(String constant) {
        return constants.get(constant);
    }

    public void clearConstants() {
        constants.clear();
        fillDefaultConstants();
    }

    private void fillDefaultConstants() {
        constants.put("pi", new NodeDouble(Math.PI));
        constants.put("euler", new NodeDouble(Math.E));
        constants.put("infinity", new NodeDouble(Double.POSITIVE_INFINITY));
        constants.put("nan", new NodeDouble(Double.NaN));
        constants.put("goldenratio", new NodeDouble(1.6180339887));
        constants.put("true", new NodeBoolean(true));
        constants.put("false", new NodeBoolean(false));

        constants.put("zero", NodeFactory.createZeroNumber());
        constants.put("one", NodeFactory.createNodeNumberFrom(1.0));
        constants.put("two", NodeFactory.createNodeNumberFrom(2.0));
        constants.put("three", NodeFactory.createNodeNumberFrom(3.0));
        constants.put("four", NodeFactory.createNodeNumberFrom(4.0));
        constants.put("five", NodeFactory.createNodeNumberFrom(5.0));
        constants.put("six", NodeFactory.createNodeNumberFrom(6.0));
        constants.put("seven", NodeFactory.createNodeNumberFrom(7.0));
        constants.put("eight", NodeFactory.createNodeNumberFrom(8.0));
        constants.put("nine", NodeFactory.createNodeNumberFrom(9.0));
        constants.put("ten", NodeFactory.createNodeNumberFrom(10.0));

        constants.put("hundred", NodeFactory.createNodeNumberFrom(100.0));
        constants.put("thousand", NodeFactory.createNodeNumberFrom(1000.0));
        constants.put("million", NodeFactory.createNodeNumberFrom(1000000.0));
        constants
                .put("billion", NodeFactory.createNodeNumberFrom(1000000000.0));

        // constants.put("d", new NodeDouble(38.6));
        constants.put("t", new NodeDouble(4.6));
        constants.put("v", new NodeVector(new Vector(new double[]{458.6, 1,
                2, 8, 3, 7, 21, 4})));
        constants.put("m", new NodeMatrix(new Matrix(new double[][]{
                {1, 2, 3}, {4, 5, 6}, {7, 8, 9}})));
        constants.put("m2", new NodeMatrix(new Matrix(new double[][]{
                {-5, 7, 3}, {-2, 1, 3}, {9, 4.5, 2}})));
        constants.put("mm", new NodeMatrix(new Matrix(new double[][]{
                {6, 18}, {4, -5}})));

        constants.put("c", new NodeVector(new Node[]{constants.get("v"),
                new NodeDouble(38.6), constants.get("m"), constants.get("t")}));
    }

    private NodeConstant getResult(Node tree) {
        if (tree instanceof NodeSet) {
            return ((NodeSet) tree).resolve(this::parse);
        } else if (tree instanceof NodeFunction) {
            NodeFunction func = (NodeFunction) tree;
            func.setParser(this);

            addConstant(func.getIdentifier(), func);
            return (NodeFunction) tree;
        } else if (tree instanceof NodeConstant) {
            return (NodeConstant) tree;
        } else if (tree instanceof NodeAddVariable) {
            NodeAddVariable nab = (NodeAddVariable) tree;

            NodeConstant result = parse(nab.getNode());
            addConstant(nab.getVariable(), result);

            return result;
        } else if (tree instanceof NodeVariable) {
            String tmp = ((NodeVariable) tree).getVariable();

            if (handleCustomOperator(tmp)) {
                return null;
            } else if (constants.containsKey(tmp)) {
                return constants.get(tmp);
            } else {
                throw new IllegalArgumentException("No value associated with \"" + tmp + "\"");
            }
        } else if (tree instanceof NodeExpression) {
            NodeExpression expression = (NodeExpression) tree;
            Operator operator = ((NodeExpression) tree).getOperator();

            if (operator instanceof UnaryOperator) {
                UnaryOperator unop = (UnaryOperator) operator;
                return unop.toResult(context, parse(expression.getArgOne()));
            } else {
                BinaryOperator binop = (BinaryOperator) operator;
                return binop.toResult(context, parse(expression.getArgOne()),
                        parse(expression.getArgTwo()));
            }
        }

        throw new IllegalArgumentException("Unknown Operator");
    }

    Set<String> getVariableList() {
        return constants.keySet();
    }

    private boolean handleCustomOperator(String operator) {
        if (operator.equals("clearvars")) {
            clearConstants();
            return true;
        }

        return false;
    }

    @Override
    public NodeConstant parse(Node tree) {
        return getResult(tree);
    }

    void setAngleUnit(AngleUnit angleUnit) {
        this.context.setAngleUnit(angleUnit);
    }
}
