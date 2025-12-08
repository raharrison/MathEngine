package uk.co.ryanharrison.mathengine.parser.nodes;

import org.apache.commons.lang3.StringUtils;
import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class NodeFunction extends NodeConstant {

    private String identifier;
    private String[] variables;
    private String function;

    private Node node;

    NodeFunction(String identifier, String function, Node node) {
        this(identifier, null, function, node);
    }

    NodeFunction(String identifier, String[] vars, String function,
                 Node node) {
        this.identifier = identifier;
        this.variables = vars;
        this.function = function;
        this.node = node;
    }

    public NodeFunction(Function function) {
        this("", new String[]{function.getVariable()}, function
                .getEquation(), function.getCompiledExpression());
    }

    @Override
    public NodeConstant applyUniFunc(java.util.function.Function<NodeNumber, NodeConstant> func) {
        return func.apply(getTransformer().toNodeNumber());
    }

    @Override
    public NodeConstant applyBiFunc(NodeConstant b, BiFunction<NodeNumber, NodeNumber, NodeConstant> func) {
        return func.apply(getTransformer().toNodeNumber(), b.getTransformer().toNodeNumber());
    }

    @Override
    public int compareTo(NodeConstant cons) {
        throw new UnsupportedOperationException("Cannot compare a function");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeFunction that = (NodeFunction) o;
        return Objects.equals(identifier, that.identifier) &&
                Arrays.equals(variables, that.variables) &&
                Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(identifier, function);
        result = 31 * result + Arrays.hashCode(variables);
        return result;
    }

    public int getArgNum() {
        if (this.variables == null)
            return 0;
        else
            return variables.length;
    }

    public String getFunction() {
        return this.function;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String[] getVariables() {
        return this.variables;
    }

    public Node getNode() {
        return node;
    }

    public Function toFunction() {
        if (getArgNum() == 1)
            return new Function(function, variables[0]);
        else
            throw new RuntimeException("Function must have one argument");
    }

    public Map<String, NodeConstant> createArgsFrom(NodeConstant arg) {
        return Map.of(variables[0], arg);
    }

    public Map<String, NodeConstant> createArgsFrom(NodeVector args) {
        Map<String, NodeConstant> argMap = new HashMap<>();
        NodeConstant[] constants = args.toNodeConstants();
        for (int i = 0; i < constants.length; i++) {
            argMap.put(variables[i], constants[i]);
        }
        return argMap;
    }

    @Override
    public String toString() {
        if (getIdentifier().equals(""))
            return Utils.removeOuterParenthesis(node.toString());

        return String.format("%s(%s) = %s", getIdentifier(),
                StringUtils.join(getVariables(), ","),
                Utils.removeOuterParenthesis(node.toString()));
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeFunctionTransformer();
    }

    @Override
    public NodeFunction copy() {
        return new NodeFunction(identifier, variables, function, node);
    }

    private class NodeFunctionTransformer extends DefaultNodeTransformer {

        @Override
        public NodeNumber toNodeNumber() {
            throw new UnsupportedOperationException("Cannot convert function to a number");
        }
    }
}
