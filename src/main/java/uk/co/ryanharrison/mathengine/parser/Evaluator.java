package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.OperatorProvider;

public final class Evaluator {

    public static Evaluator newEvaluator() {
        Evaluator evaluator = newSimpleEvaluator();
        evaluator.parser.fillOperators(OperatorProvider.logicalOperators());
        evaluator.parser.fillOperators(OperatorProvider.vectorOperators());
        evaluator.parser.fillOperators(OperatorProvider.matrixOperators());
        evaluator.parser.fillOperators(OperatorProvider.customOperators());

        return evaluator;
    }

    public static Evaluator newSimpleBinaryEvaluator() {
        Evaluator evaluator = new Evaluator();
        evaluator.parser.fillOperators(OperatorProvider.simpleBinaryOperators());

        return evaluator;
    }

    public static Evaluator newSimpleEvaluator() {
        Evaluator evaluator = newSimpleBinaryEvaluator();
        evaluator.parser.fillOperators(OperatorProvider.simpleUnaryOperators());
        evaluator.parser.fillOperators(OperatorProvider.trigOperators());

        return evaluator;
    }

    private ExpressionParser parser;

    private RecursiveDescentParser rec;

    private Evaluator() {
        parser = new ExpressionParser();
        rec = new RecursiveDescentParser();
    }

    private void addVariable(String variable, NodeConstant value) {
        variable = variable.trim();

        if (!parser.isOperator(variable, false))
            rec.addConstant(variable, value);
        else
            throw new IllegalArgumentException("Constant is an operator");
    }

    public void addVariable(String variable, String value) {
        NodeConstant r = rec.parse(parser.parse(value.trim()));
        addVariable(variable, r);
    }

    public void addVariable(String variable, double value) {
        addVariable(variable, Double.toString(value));
    }

    public double evaluateDouble(String expression) {
        Node tree = generateTree(expression);
        NodeConstant r = parseTree(tree);

        if (r instanceof NodeNumber) {
            return r.getTransformer().toNodeNumber().doubleValue();
        } else {
            throw new UnsupportedOperationException("Expression does not return a double value");
        }
    }

    public Node generateTree(String expression) {
        expression = expression.trim();
        parser.setVariables(rec.getVariableList());
        return parser.parse(expression);
    }

    public NodeConstant parseTree(Node tree) {
        NodeConstant result = rec.parse(tree);
        addVariable("ans", result);
        return result;
    }

    public void setAngleUnit(AngleUnit angleUnit) {
        this.rec.setAngleUnit(angleUnit);
    }
}
