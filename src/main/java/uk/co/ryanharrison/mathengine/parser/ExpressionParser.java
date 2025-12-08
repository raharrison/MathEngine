package uk.co.ryanharrison.mathengine.parser;

import org.apache.commons.lang3.StringUtils;
import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.CustomOperator;
import uk.co.ryanharrison.mathengine.utils.Utils;

public final class ExpressionParser implements Parser<String, Node> {

    private int maxOpLength;
    private EvaluationContext context;

    ExpressionParser(EvaluationContext context) {
        this.context = context;
        maxOpLength = context.findLongestOperator();
    }

    private String backTrack(String str) {
        for (int i = 0; i <= this.maxOpLength; i++) {
            String op;
            if ((op = findOperator(str, (str.length() - 1 - maxOpLength + i))) != null
                    && (str.length() - maxOpLength - 1 + i + op.length()) == str.length()) {
                return op;
            }
        }
        return null;
    }

    private String findOperator(String expression, int index) {
        int len = expression.length();
        for (int i = 0; i < maxOpLength; i++) {
            if (index >= 0 && index + maxOpLength - i <= len) {
                String tmp = expression.substring(index, index + maxOpLength - i);
                if (context.isOperator(tmp)) {
                    return tmp;
                }
            }
        }
        return null;
    }

    private Argument getArguments(String operator, String exp, int index, boolean recurse) {
        int ma;
        int len = exp.length();
        String op;
        StringBuilder str = new StringBuilder();

        int prec = -1;
        if (operator != null) {
            prec = context.getOperator(operator).getPrecedence();
        }

        int i = index;
        while (i < len) {
            if (exp.charAt(i) == '(') {
                ma = Utils.matchingCharacterIndex(exp, i, '(', ')');
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if (exp.charAt(i) == '{') {
                ma = Utils.matchingCharacterIndex(exp, i, '{', '}');
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if (exp.charAt(i) == '[') {
                ma = Utils.matchingCharacterIndex(exp, i, '[', ']');
                str.append(exp, i, ma + 1);
                i = ma + 1;
            } else if ((op = findOperator(exp, i)) != null) {
                if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
                        && context.getOperator(op).getPrecedence() >= prec) {
                    if (recurse) {
                        Node n = parseTree(str.toString(), false);
                        return new Argument(str.toString(), n, str.toString().length());
                    } else {
                        return new Argument(str.toString(), null, str.length());
                    }
                }
                str.append(op);
                i += op.length();
            } else {
                str.append(exp.charAt(i));
                i++;
            }
        }

        if (recurse) {
            return new Argument(str.toString(), parseTree(str.toString(), false), str.toString().length());
        } else {
            return new Argument(str.toString(), null, str.length());
        }
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

        if (context.isConstant(expression))
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

        if (index != -1) {
            String variable = expression.substring(0, index);
            if (context.isSystemOperator(variable))
                throw new IllegalArgumentException("Variable is an operator");

            String expr = expression.substring(index + 2).trim();
            Node parsed = parseTree(expr, true);

            NodeFunction func = NodeFactory.createNodeFunctionFrom(variable.trim(), expr, parsed);

            if (func.getArgNum() > 0) {
                context.addConstant(func.getIdentifier(), func);
                context.addCustomOperator(new CustomOperator(func));
                maxOpLength = context.findLongestOperator();
                return func;
            } else
                return new NodeAddVariable(func.getIdentifier(), parsed);
        }

        return parseTree(expression, true);
    }

    private Node parseTree(String expression, boolean recurse) {
        Node tree = null;

        Argument farg, sarg;
        String fop, cleanfop;
        int ma, i = 0;

        int len = expression.length();

        if (len == 0) {
            throw new IllegalArgumentException("Wrong number of arguments to operator");
        } else if (Utils.isNumeric(expression)) {
            return NodeFactory.createNodeNumberFrom(Double.parseDouble(StringUtils.deleteWhitespace(expression)));
        } else if (expression.charAt(0) == '('
                && (ma = Utils.matchingCharacterIndex(expression, 0, '(', ')')) == len - 1) {
            return parseTree(expression.substring(1, ma), recurse);
        } else if (expression.charAt(0) == '{'
                && (ma = Utils.matchingCharacterIndex(expression, 0, '{', '}')) == len - 1) {
            return NodeFactory.createVectorFrom(expression.substring(1, ma), this);
        } else if (expression.charAt(0) == '['
                && (ma = Utils.matchingCharacterIndex(expression, 0, '[', ']')) == len - 1) {
            return NodeFactory.createMatrixFrom(expression.substring(1, ma), this);
        } else if (isVariable(expression)) {
            return new NodeVariable(expression);
        }

        while (i < len) {
            if ((fop = findOperator(expression, i)) == null) {
                farg = getArguments(null, expression, i, recurse);
                fop = findOperator(expression, i + farg.length);

                if (fop == null)
                    throw new IllegalArgumentException("Missing operator," + " expression is \""
                            + expression + "\"");

                if (isTwoArgOp(fop)) {
                    sarg = getArguments(fop, expression, i + farg.length + fop.length(), recurse);
                    if (sarg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator "
                                + fop);

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, false);
                    if (sarg.node == null)
                        sarg.node = parseTree(sarg.source, false);

                    tree = new NodeExpression(context.getOperator(fop), farg.node, sarg.node);
                    i += farg.length + fop.length() + sarg.length;
                } else {
                    // Never using this check
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator " + fop);

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, false);
                    tree = new NodeExpression(context.getOperator(fop), farg.node);
                    i += farg.length + fop.length();
                }
            } else {
                cleanfop = Utils.standardiseString(fop);
                if (isTwoArgOp(fop)) {
                    farg = getArguments(fop, expression, i + fop.length(), recurse);
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator " + fop);
                    if (tree == null) {
                        if (cleanfop.equals("+") || cleanfop.equals("-")) {
                            tree = NodeFactory.createNodeNumberFrom(0D);
                        } else {
                            throw new IllegalArgumentException("Wrong number of arguments to operator " + fop);
                        }
                    }

                    if (farg.node == null)
                        farg.node = parseTree(farg.source, false);
                    tree = new NodeExpression(context.getOperator(cleanfop), tree, farg.node);
                    i += farg.length + fop.length();
                } else {
                    farg = getArguments(fop, expression, i + fop.length(), recurse);
                    if (farg.source.equals(""))
                        throw new IllegalArgumentException("Wrong number of arguments to operator " + fop);
                    if (farg.node == null)
                        farg.node = parseTree(farg.source, false);
                    tree = new NodeExpression(context.getOperator(cleanfop), farg.node);
                    i += farg.length + fop.length();
                }
            }
        }

        return tree;
    }
}
