package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.linearalgebra.Vector;
import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.OperatorProvider;

import java.util.Map;

public final class Evaluator {

    public static Evaluator newEvaluator() {
        Evaluator evaluator = newSimpleEvaluator();
        evaluator.context.fillOperators(OperatorProvider.logicalOperators());
        evaluator.context.fillOperators(OperatorProvider.vectorOperators());
        evaluator.context.fillOperators(OperatorProvider.matrixOperators());
        evaluator.context.fillOperators(OperatorProvider.customOperators());

        return evaluator;
    }

    public static Evaluator newSimpleBinaryEvaluator() {
        Evaluator evaluator = new Evaluator();
        evaluator.context.fillOperators(OperatorProvider.simpleBinaryOperators());

        return evaluator;
    }

    public static Evaluator newSimpleEvaluator() {
        Evaluator evaluator = newSimpleBinaryEvaluator();
        evaluator.context.fillOperators(OperatorProvider.simpleUnaryOperators());
        evaluator.context.fillOperators(OperatorProvider.trigOperators());

        return evaluator;
    }

    private EvaluationContext context = new EvaluationContext(this);

    private Evaluator() {
        fillDefaultConstants();
    }

    private void fillDefaultConstants() {
        context.addConstant("pi", new NodeDouble(Math.PI));
        context.addConstant("euler", new NodeDouble(Math.E));
        context.addConstant("infinity", new NodeDouble(Double.POSITIVE_INFINITY));
        context.addConstant("nan", new NodeDouble(Double.NaN));
        context.addConstant("goldenratio", new NodeDouble(1.6180339887));
        context.addConstant("true", new NodeBoolean(true));
        context.addConstant("false", new NodeBoolean(false));

        context.addConstant("zero", NodeFactory.createZeroNumber());
        context.addConstant("one", NodeFactory.createNodeNumberFrom(1.0));
        context.addConstant("two", NodeFactory.createNodeNumberFrom(2.0));
        context.addConstant("three", NodeFactory.createNodeNumberFrom(3.0));
        context.addConstant("four", NodeFactory.createNodeNumberFrom(4.0));
        context.addConstant("five", NodeFactory.createNodeNumberFrom(5.0));
        context.addConstant("six", NodeFactory.createNodeNumberFrom(6.0));
        context.addConstant("seven", NodeFactory.createNodeNumberFrom(7.0));
        context.addConstant("eight", NodeFactory.createNodeNumberFrom(8.0));
        context.addConstant("nine", NodeFactory.createNodeNumberFrom(9.0));
        context.addConstant("ten", NodeFactory.createNodeNumberFrom(10.0));

        context.addConstant("hundred", NodeFactory.createNodeNumberFrom(100.0));
        context.addConstant("thousand", NodeFactory.createNodeNumberFrom(1000.0));
        context.addConstant("million", NodeFactory.createNodeNumberFrom(1000000.0));
        context.addConstant("billion", NodeFactory.createNodeNumberFrom(1000000000.0));

        // context.addConstant("d", new NodeDouble(38.6));
        context.addConstant("t", new NodeDouble(4.6));
        context.addConstant("v", new NodeVector(new Vector(new double[]{458.6, 1,
                2, 8, 3, 7, 21, 4})));
        context.addConstant("m", new NodeMatrix(new Matrix(new double[][]{
                {1, 2, 3}, {4, 5, 6}, {7, 8, 9}})));
        context.addConstant("m2", new NodeMatrix(new Matrix(new double[][]{
                {-5, 7, 3}, {-2, 1, 3}, {9, 4.5, 2}})));
        context.addConstant("mm", new NodeMatrix(new Matrix(new double[][]{
                {6, 18}, {4, -5}})));

        context.addConstant("c", new NodeVector(new Node[]{context.getConstant("v"),
                new NodeDouble(38.6), context.getConstant("m"), context.getConstant("t")}));
    }

    public void addVariable(String variable, String value) {
        context.addConstant(variable, evaluateConstant(value));
    }

    public void addVariable(String variable, double value) {
        addVariable(variable, Double.toString(value));
    }

    public NodeConstant evaluateConstant(String expression) {
        Node tree = generateTree(expression);
        return parseTree(tree);
    }

    public double evaluateDouble(String expression) {
        NodeConstant r = evaluateConstant(expression);

        if (r instanceof NodeNumber) {
            return r.getTransformer().toNodeNumber().doubleValue();
        } else {
            throw new UnsupportedOperationException("Expression does not return a double value");
        }
    }

    public Node generateTree(String expression) {
        ExpressionParser parser = new ExpressionParser(context);
        return parser.parse(expression.trim());
    }

    public NodeConstant parseTree(Node tree) {
        RecursiveDescentParser parser =  new RecursiveDescentParser(context);
        NodeConstant result = parser.parse(tree);
        context.addConstant("ans", result);
        return result;
    }

    NodeConstant parseTreeWithArgs(Node tree, Map<String, NodeConstant> args) {
        EvaluationContext contextWithArgs = context.withArgs(args);
        return new RecursiveDescentParser(contextWithArgs).parse(tree);
    }

    public void setAngleUnit(AngleUnit angleUnit) {
        context.setAngleUnit(angleUnit);
    }
}
