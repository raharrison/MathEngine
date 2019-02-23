package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.Utils;
import uk.co.ryanharrison.mathengine.parser.RecursiveDescentParser;
import uk.co.ryanharrison.mathengine.parser.operators.Determinable;

import java.util.Arrays;

public class NodeFunction extends NodeConstant
{
	private String identifier;
	private String[] variables;
	private String function;

	private Node node;
	private RecursiveDescentParser recParser;

	public NodeFunction(NodeFunction function)
	{
		this.identifier = function.identifier;
		this.variables = function.variables;
		this.function = function.function;
		this.node = function.node;
		this.recParser = function.recParser;
	}

	public NodeFunction(String identifier, String function, Node node)
	{
		this(identifier, null, function, node);
	}

	public NodeFunction(String identifier, String[] vars, String function,
			Node node)
	{
		this.identifier = identifier;
		this.variables = vars;
		this.function = function;
		this.node = node;
	}

	public NodeFunction(Function function)
	{
		this("", new String[] { function.getVariable() }, function
				.getEquation(), function.getCompiledExpression());
	}

	private UnsupportedOperationException generateInvalidArithmeticException()
	{
		return new UnsupportedOperationException(
				"Cannot do arithmetic on functions");
	}

	@Override
	public NodeConstant applyDeterminable(Determinable deter)
	{
		return deter.getResult(getTransformer().toNodeNumber());
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		if (cons instanceof NodeFunction)
			return Double.compare(
					this.evaluateNumber(NodeFactory.createNodeNumberFrom(1))
							.doubleValue(), cons.getTransformer()
							.toNodeNumber().doubleValue());

		// negate as switching the comparator
		return -cons.compareTo(this);
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeFunction)
		{
			NodeFunction func = (NodeFunction) object;

			return this.function.equals(func.function)
					&& Arrays.equals(this.variables, func.variables);
		}

		return false;
	}

	public NodeConstant evaluate(NodeConstant n)
	{
		NodeConstant constant = recParser.getConstantFromKey(variables[0]);
		recParser.addConstant(variables[0], n);
		NodeConstant result = recParser.parse(node);
		if (constant != null)
			recParser.addConstant(variables[0], constant);
		else
			recParser.removeConstant(variables[0]);
		return result;
	}

	public NodeConstant evaluate(NodeVector v)
	{
		Node[] nodes = v.getValues();
		NodeConstant[] constants = new NodeConstant[variables.length];
		for (int i = 0; i < constants.length; i++)
		{
			constants[i] = recParser.getConstantFromKey(variables[i]);
		}
		for (int i = 0; i < variables.length; i++)
		{
			recParser.addConstant(variables[i], recParser.parse(nodes[i]));
		}

		NodeConstant result = recParser.parse(node);

		for (int i = 0; i < variables.length; i++)
		{
			if (constants[i] != null)
				recParser.addConstant(variables[i], constants[i]);
			else
				recParser.removeConstant(variables[i]);
		}

		return result;
	}

	public NodeNumber evaluateNumber(NodeNumber n)
	{
		return evaluate(n).getTransformer().toNodeNumber();
	}

	public int getArgNum()
	{
		if (this.variables == null)
			return 0;
		else
			return variables.length;
	}

	public String getFunction()
	{
		return this.function;
	}

	public String getIdentifier()
	{
		return this.identifier;
	}

	public String[] getVariables()
	{
		return this.variables;
	}

	public void setParser(RecursiveDescentParser parser)
	{
		this.recParser = parser;
	}

	public Function toFunction()
	{
		if (getArgNum() == 1)
			return new Function(function, variables[0],
					recParser.getAngleUnit());
		else
			throw new RuntimeException("Function must have one argument");
	}

	@Override
	public String toString()
	{
		if (getIdentifier().equals(""))
			return Utils.removeOuterParenthesis(node.toString());

		return String.format("%s(%s) = %s", getIdentifier(),
				Utils.join(getVariables(), ","),
				Utils.removeOuterParenthesis(node.toString()));
	}

	@Override
	public String toTypeString()
	{
		return "function";
	}

	@Override
	public NodeTransformer getTransformer()
	{
		if (this.transformer == null)
			this.transformer = new NodeVectorTransformer();

		return this.transformer;
	}

	@Override
	public NodeFunction copy() {
		return new NodeFunction(identifier, variables, function, node);
	}

	private class NodeVectorTransformer implements NodeTransformer
	{

		@Override
		public NodeVector toNodeVector()
		{
			return new NodeVector(new Node[] { NodeFunction.this });
		}

		@Override
		public NodeNumber toNodeNumber()
		{
			if(getArgNum() > 0)
				throw new IllegalStateException("Cannot convert function to a number");
			
			NodeConstant res = recParser.parse(node);
			if (res instanceof NodeNumber)
				return (NodeNumber) res;
			throw new RuntimeException("Function does not resolve to a number");
		}
	}
}
