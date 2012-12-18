package uk.co.raharrison.mathengine.parser.nodes;

import java.util.Arrays;
import java.util.Map;

import uk.co.raharrison.mathengine.Function;
import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.ExpressionParser;
import uk.co.raharrison.mathengine.parser.RecursiveDescentParser;
import uk.co.raharrison.mathengine.parser.operators.Determinable;

public class NodeFunction extends NodeConstant
{
	private String identifier;
	private String[] variables;
	private String function;

	private Node node;
	private ExpressionParser parser;
	private RecursiveDescentParser recParser;

	public NodeFunction(NodeFunction function)
	{
		this.identifier = function.identifier;
		this.variables = function.variables;
		this.function = function.function;
		this.node = function.node;
		this.parser = function.parser;
		this.recParser = function.recParser;
		reparse();
	}

	public NodeFunction(String identifier, String function, ExpressionParser parser)
	{
		this(identifier, null, function, parser);
	}

	public NodeFunction(String identifier, String[] vars, String function, ExpressionParser parser)
	{
		this.identifier = identifier;
		this.variables = vars;
		this.function = function;
		this.parser = parser;
		reparse();
	}

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		function += "+ " + arg2.toShortString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		function += "+ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		function += "+ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		function += "+ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant applyDeterminable(Determinable deter)
	{
		return deter.getResult(toNodeNumber());
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		if (cons instanceof NodeFunction)
			return Double.compare(this.doubleValue(), cons.doubleValue());

		// negate as switching the comparator
		return -cons.compareTo(this);
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		function += "/ " + arg2.toShortString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		function += "/ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		function += "/ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		function += "/ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public double doubleValue()
	{
		NodeConstant res = recParser.parse(node);
		return res.doubleValue();
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

	public NodeConstant evaluate(NodeNumber n)
	{
		recParser.addConstant(variables[0], n);
		return recParser.parse(node);
	}

	public NodeConstant evaluate(NodeVector v)
	{
		Node[] nodes = v.getValues();
		for (int i = 0; i < variables.length; i++)
		{
			recParser.addConstant(variables[i], recParser.parse(nodes[i]));
		}
		return recParser.parse(node);
	}

	public NodeNumber evaluateNumber(NodeNumber n)
	{
		return evaluate(n).toNodeNumber();
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

	@Override
	public NodeConstant multiply(NodeMatrix arg2)
	{
		function += "* " + arg2.toShortString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		function += "* " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		function += "* " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		function += "* " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		function += "^ " + arg2.toShortString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		function += "^ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		function += "^ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		function += "^ " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	private void reparse()
	{
		if (this.variables != null)
		{
			for (int i = 0; i < this.variables.length; i++)
			{
				parser.addVariable(variables[i]);
			}
		}
		this.node = parser.parse(function);
	}

	public void setParser(RecursiveDescentParser parser)
	{
		this.recParser = parser;
	}

	public void setVariables(Map<String, NodeConstant> vars)
	{
		parser.setVariables(vars.keySet());
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		function += "- " + arg2.toShortString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		function += "- " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		function += "- " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		function += "- " + arg2.toString();
		reparse();
		return new NodeFunction(this);
	}

	public Function toFunction()
	{
		if (getArgNum() == 1)
			return new Function(function, variables[0], recParser.getAngleUnit());
		else
			throw new RuntimeException("Function must have one argument");
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		NodeConstant res = recParser.parse(node);
		if (res instanceof NodeNumber)
			return (NodeNumber) res;
		throw new RuntimeException("Function does not resolve to a number");
	}

	@Override
	public String toString()
	{
		return String.format("%s(%s) = %s", getIdentifier(), Utils.join(getVariables(), ","),
				node.toString());
	}

	@Override
	public String toTypeString()
	{
		return "function";
	}
}
