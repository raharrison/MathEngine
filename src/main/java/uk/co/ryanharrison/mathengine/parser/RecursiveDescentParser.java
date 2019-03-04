package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.Operator;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public final class RecursiveDescentParser implements Parser<Node, NodeConstant> {

    private EvaluationContext context;

    RecursiveDescentParser(EvaluationContext context) {
        this.context = context;
    }

    @Override
    public NodeConstant parse(Node tree) {
        if (tree instanceof NodeSet) {
            return ((NodeSet) tree).resolve(this::parse);
        } else if (tree instanceof NodeConstant) {
            return (NodeConstant) tree;
        } else if (tree instanceof NodeAddVariable) {
            NodeAddVariable nab = (NodeAddVariable) tree;
            NodeConstant result = parse(nab.getNode());
            context.addConstant(nab.getVariable(), result);
            return result;
        } else if (tree instanceof NodeVariable) {
            String var = ((NodeVariable) tree).getVariable();

            if (handleCustomOperator(var)) {
                return null;
            } else if (context.isConstant(var)) {
                return context.getConstant(var);
            } else {
                throw new IllegalArgumentException("No value associated with \"" + var + "\"");
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

    private boolean handleCustomOperator(String op) {
        if (op.equalsIgnoreCase("clearvars")) {
            context.clearConstants();
            return true;
        }
        return false;
    }
}
