package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.parser.Evaluator;
import uk.co.raharrison.mathengine.parser.operators.Determinable;

public class NodeFunction extends NodeConstant
{
	private String variable;
	private String function;
	private static Evaluator evaluator;

	static
	{
		evaluator = Evaluator.newEvaluator();
	}

	public NodeFunction(String var, String function)
	{
		this.variable = var;
		this.function = function;
	}

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		function += "+ " + arg2.toShortString();
		return this;
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		function += "+ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		function += "+ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		function += "+ " + arg2.toString();
		return this;
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
		return this;
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		function += "/ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		function += "/ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		function += "/ " + arg2.toString();
		return this;
	}

	@Override
	public double doubleValue()
	{
		return evaluator.evaluateDouble(function);
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeFunction)
		{
			return function.equals(((NodeFunction) object).function);
		}

		return false;
	}

	@Override
	public NodeConstant multiply(NodeMatrix arg2)
	{
		function += "* " + arg2.toShortString();
		return this;
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		function += "* " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		function += "* " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		function += "* " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		function += "^ " + arg2.toShortString();
		return this;
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		function += "^ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		function += "^ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		function += "^ " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		function += "- " + arg2.toShortString();
		return this;
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		function += "- " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		function += "- " + arg2.toString();
		return this;
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		function += "- " + arg2.toString();
		return this;
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		NodeConstant res = evaluator.evaluateConstant(function);
		if (res instanceof NodeNumber)
			return (NodeNumber) res;
		throw new RuntimeException("Function does not resolve to a number");
	}

	@Override
	public String toString()
	{
		return function;
	}

	@Override
	public String toTypeString()
	{
		return "function";
	}
}
