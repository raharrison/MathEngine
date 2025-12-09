package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.ExpressionParser;
import uk.co.ryanharrison.mathengine.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public final class NodeFactory {
    private static final int maxInt = Integer.MAX_VALUE;
    private static final int precision = 4;

    private static NodeMatrix createMatrixFrom(List<NodeVector> vals) {
        Node[][] results = new Node[vals.size()][vals.get(0).getSize()];

        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < results[0].length; j++) {
                results[i][j] = vals.get(i).getValues()[j];
            }
        }
        return new NodeMatrix(results);
    }

    public static NodeMatrix createMatrixFrom(String expression, ExpressionParser parser) {
        if (Utils.isEmpty(expression))
            return new NodeMatrix(new Node[0][0]);

        List<NodeVector> vals = new ArrayList<>();
        NodeVector v = createVectorFrom(expression, parser);
        int len = 0;
        boolean containsNoVectors = true;

        for (Node n : v.getValues()) {
            NodeVector vec;
            if (n instanceof NodeVector) {
                vec = (NodeVector) n;
                containsNoVectors = false;
            } else
                vec = new NodeVector(new Node[]{n});

            if (len == 0)
                len = vec.getSize();
            else if (len != vec.getSize())
                throw new RuntimeException("Invalid matrix dimensions. Expected rows of " + len);

            vals.add(vec);
        }

        if (containsNoVectors) {
            vals.clear();
            vals.add(v);
        }

        return createMatrixFrom(vals);
    }

    public static NodeFunction createNodeFunctionFrom(String var, String expr, Node node) {
        int index = var.indexOf("(");
        if (index == -1)
            return new NodeFunction(var.trim(), expr.trim(), node);
        else {
            String identifier = var.substring(0, index);
            String[] vars = var.substring(index + 1, var.length() - 1).split(",");
            for (int i = 0; i < vars.length; i++) {
                vars[i] = vars[i].trim();
            }
            return new NodeFunction(identifier.trim(), vars, expr.trim(), node);
        }
    }

    public static NodeNumber createNodeNumberFrom(double value) {
        double absValue = Math.abs(value);

        // Greater than max possible number
        if (absValue > maxInt)
            return new NodeDouble(value);
            // Too small
        else if (absValue < 1.0 / maxInt && absValue != 0)
            return new NodeDouble(value);
            // Too much precision
        else if (Double.toString(absValue).split("\\.")[1].length() > precision)
            return new NodeDouble(value);
        else
            try {
                return new NodeRational(value);
            } catch (RuntimeException e) {
                return new NodeDouble(value);
            }
    }

    public static NodeVector createVectorFrom(String expression, ExpressionParser parser) {
        if (Utils.isEmpty(expression))
            return new NodeVector(new Node[0]);

        // expression = Utils.removeOuterParenthesis(expression);
        if (expression.charAt(0) == '{'
                && Utils.matchingCharacterIndex(expression, 0, '{', '}') == expression.length() - 1)
            expression = expression.substring(1, expression.length() - 1);

        List<Node> vals = new ArrayList<>();
        int i = 0;
        StringBuilder b = new StringBuilder();
        while (i < expression.length()) {
            if (expression.charAt(i) == ' ') {
                i++;
            } else if (expression.charAt(i) == '{') {
                int ma;
                vals.add(parser.parse(expression.substring(i,
                        1 + (ma = Utils.matchingCharacterIndex(expression, i, '{', '}')))));
                i = ++ma;
            } else {
                int j = i;
                while (j < expression.length() && expression.charAt(j) != ',') {
                    b.append(expression.charAt(j));
                    j++;
                }
                if (b.length() != 0)
                    vals.add(parser.parse(b.toString()));
                i += b.length() + 1;
                b.setLength(0);
            }
        }

        return new NodeVector(vals.toArray(new Node[0]));
    }

    public static NodeNumber createZeroNumber() {
        return createNodeNumberFrom(0.0);
    }

}
