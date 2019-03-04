package uk.co.ryanharrison.mathengine.parser;

import org.apache.commons.lang3.StringUtils;
import uk.co.ryanharrison.mathengine.Utils;
import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.CustomOperator;

public final class ExpressionParser implements Parser<String, Node> {

    private int maxoplength;
    private EvaluationContext context;

    ExpressionParser(EvaluationContext context) {
        this.context = context;
        maxoplength = context.findLongestOperator();
    }

    private String backTrack(String str) {
        try {
            for (int i = 0; i <= this.maxoplength; i++) {
                String op;
                if ((op = findOperator(str, (str.length() - 1 - maxoplength + i))) != null
                        && (str.length() - maxoplength - 1 + i + op.length()) == str.length()) {
                    return op;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String findOperator(String expression, int index) {
        String tmp;
        int i = 0;
        int len = expression.length();

        for (i = 0; i < maxoplength; i++) {
            if (index >= 0 && index + maxoplength - i <= len) {
                tmp = expression.substring(index, index + maxoplength - i);
                if (context.isOperator(tmp)) {
                    return tmp;
                }
            }
        }
        return null;
    }

    private Argument getArgumentsDefault(String operator, String exp, int index) {
        int ma, i, prec = -1;
        int len = exp.length();
        String op = null;
        StringBuilder str = new StringBuilder();

        i = index;
        ma = 0;

        if (operator == null) {
            prec = -1;
        } else {
            prec = context.getOperator(operator).getPrecedence();
        }

        while (i < len) {
            if (exp.charAt(i) == '(') {
                ma = Utils.matchingCharacterIndex(exp, i, '(', ')');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if (exp.charAt(i) == '{') {
                ma = Utils.matchingCharacterIndex(exp, i, '{', '}');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if (exp.charAt(i) == '[') {
                ma = Utils.matchingCharacterIndex(exp, i, '[', ']');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if ((op = findOperator(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
                        && context.getOperator(op).getPrecedence() >= prec) {
                    return new Argument(str.toString(), null, str.length());
                }
                str.append(op);
                i += op.length();
            } else {
                str.append(exp.charAt(i));
                i++;
            }
        }

        return new Argument(str.toString(), null, str.length());
    }

    private Argument getArgumentsRecursive(String operator, String exp, int index) {
        int ma, i, prec = -1;
        int len = exp.length();
        String op = null;
        StringBuilder str = new StringBuilder();

        i = index;
        ma = 0;

        if (operator == null) {
            prec = -1;
        } else {
            prec = context.getOperator(operator).getPrecedence();
        }

        while (i < len) {
            if (exp.charAt(i) == '(') {
                ma = Utils.matchingCharacterIndex(exp, i, '(', ')');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if (exp.charAt(i) == '{') {
                ma = Utils.matchingCharacterIndex(exp, i, '{', '}');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if (exp.charAt(i) == '[') {
                ma = Utils.matchingCharacterIndex(exp, i, '[', ']');
                str.append(exp.substring(i, ma + 1));
                i = ma + 1;
            } else if ((op = findOperator(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
                        && context.getOperator(op).getPrecedence() >= prec) {
                    try {
                        Node n = parseTree(str.toString(), new DefaultArgumentStrategy());
                        return new Argument(str.toString(), n, str.toString().length());
                    } catch (Exception e) {
                    }
                }
                str.append(op);
                i += op.length();
            } else {
                str.append(exp.charAt(i));
                i++;
            }
        }

        return new Argument(str.toString(), parseTree(str.toString(), new DefaultArgumentStrategy()), str.toString().length());
    }

    private class Argument {
        Argument(String source, Node node, int length) {
            this.source = source;
            this.node = node;
            this.length = length;
        }

        String source;
        int length;
        Node node;
    }

    private boolean isTwoArgOp(String operator) {
        return context.getOperator(operator) instanceof BinaryOperator;
    }

    private boolean isVariable(String expression) {
        if (Utils.isNumeric(expression))
            return false;

        if(context.isConstant(expression))
             return true;

        for (int i = 0; i < expression.length(); i++) {
            if (findOperator(expression, i) != null)
                return false;
            else if (!isAllowedSym(expression.charAt(i)))
                return false;
        }

        // Used to be false
        return true;
    }

    private boolean isAllowedSym(char s) {
        return !(s == ')' || s == '(' || s == '}' || s == ']' || s == '[' || s == '{' || s == '.'
                || s == '>' || s == '<' || s == '&' || s == '=' || s == '|');
    }

    @Override
    public Node parse(String expression) {
        int index = expression.indexOf(":=");
        ArgumentStrategy strategy = new RecursiveArgumentStrategy();

        if (index != -1) {
            String variable = expression.substring(0, index);
            if (context.isOperator(variable))
                throw new IllegalArgumentException("Variable is an operator");

            String expr = expression.substring(index + 2, expression.length()).trim();
            Node parsed = parseTree(expr, strategy);

            NodeFunction func = NodeFactory.createNodeFunctionFrom(variable.trim(), expr, parsed);

            if (func.getArgNum() > 0) {
                context.addOperator(new CustomOperator(func));
                maxoplength = context.findLongestOperator();
                return func;
            } else
                return new NodeAddVariable(func.getIdentifier(), parsed);
        }

        return parseTree(expression, strategy);
    }

    private Node parseTree(String expression, ArgumentStrategy strategy) {
        Node tree = null;

        Argument farg, sarg;
        String fop = "", cleanfop = "";
        int ma = 0, i = 0;

        int len = expression.length();

        if (len == 0) {
            throw new IllegalArgumentException("Wrong number of arguments to operator");
        } else if (Utils.isNumeric(expression)) {
            return NodeFactory.createNodeNumberFrom(Double.parseDouble(StringUtils.deleteWhitespace(expression)));
        } else if (expression.charAt(0) == '('
                && (ma = Utils.matchingCharacterIndex(expression, 0, '(', ')')) == len - 1) {
            return parseTree(expression.substring(1, ma), strategy);
        } else if (expression.charAt(0) == '{'
                && (ma = Utils.matchingCharacterIndex(expression, 0, '{', '}')) == len - 1) {
            return NodeFactory.createVectorFrom(expression.substring(1, ma), this);
        }
        else if (expression.charAt(0) == '['
                && (ma = Utils.matchingCharacterIndex(expression, 0, '[', ']')) == len - 1) {
            return NodeFactory.createMatrixFrom(expression.substring(1, ma), this);
        } else if (isVariable(expression)) {
            return new NodeVariable(expression);
        }

        while (i < len) {
            if ((fop = findOperator(expression, i)) == null) {
                farg = strategy.getArgumentsFrom(null, expression, i);
                fop = findOperator(expression, i + farg.length);

                if (fop == null)
                    throw new IllegalArgumentException("Missing operator," + " expression is \""
                            + expression + "\"");

                if (isTwoArgOp(fop)) {
                    sarg = strategy.getArgumentsFrom(fop, expression, i + farg.length + fop.length());
                    if (sarg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator "
                                + fop);

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
                    if (sarg.node == null)
                        sarg.node = parseTree(sarg.source, new DefaultArgumentStrategy());

                    tree = new NodeExpression(context.getOperator(fop), farg.node, sarg.node);
                    i += farg.length + fop.length() + sarg.length;
                } else {
                    // Never using this check
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator " + fop);

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
                    tree = new NodeExpression(context.getOperator(fop), farg.node);
                    i += farg.length + fop.length();
                }
            } else {
                cleanfop = Utils.standardiseString(fop);
                if (isTwoArgOp(fop)) {
                    farg = strategy.getArgumentsFrom(fop, expression, i + fop.length());
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator "
                                + fop);
                    if (tree == null) {
                        if (cleanfop.equals("+") || cleanfop.equals("-")) {
                            tree = NodeFactory.createNodeNumberFrom(0D);
                        } else {
                            throw new IllegalArgumentException(
                                    "Wrong number of arguments to operator " + fop);
                        }
                    }

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
                    tree = new NodeExpression(context.getOperator(cleanfop), tree, farg.node);
                    i += farg.length + fop.length();
                } else {
                    farg = strategy.getArgumentsFrom(fop, expression, i + fop.length());
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator "
                                + fop);
                    if (farg.node == null)
                        farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
                    tree = new NodeExpression(context.getOperator(cleanfop), farg.node);
                    i += farg.length + fop.length();
                }
            }
        }

        return tree;
    }

    private interface ArgumentStrategy {
        Argument getArgumentsFrom(String operator, String exp, int index);
    }

    private class DefaultArgumentStrategy implements ArgumentStrategy {
        @Override
        public Argument getArgumentsFrom(String operator, String exp, int index) {
            return getArgumentsDefault(operator, exp, index);
        }
    }

    private class RecursiveArgumentStrategy implements ArgumentStrategy {
        @Override
        public Argument getArgumentsFrom(String operator, String exp, int index) {
            return getArgumentsRecursive(operator, exp, index);
        }
    }
}
