package uk.co.raharrison.mathengine.parser.nodes;

import java.util.Arrays;

import uk.co.raharrison.mathengine.Function;
import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.RecursiveDescentParser;
import uk.co.raharrison.mathengine.parser.operators.Determinable;

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

	public NodeFunction(String identifier, String[] vars, String function, Node node)
	{
		this.identifier = identifier;
		this.variables = vars;
		this.function = function;
		this.node = node;
	}

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		throw generateInvalidArithmeticException();
	}

	private UnsupportedOperationException generateInvalidArithmeticException()
	{
		return new UnsupportedOperationException("Cannot do arithmetic on functions");
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		throw generateInvalidArithmeticException();
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
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		throw generateInvalidArithmeticException();
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
		NodeConstant constant = recParser.getConstantFromKey(variables[0]);
		recParser.addConstant(variables[0], n);
		NodeConstant result = recParser.parse(node);
		if(constant != null)
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
			if(constants[i] != null)
				recParser.addConstant(variables[i], constants[i]);
			else
				recParser.removeConstant(variables[i]);
		}
		
		return result;
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
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		throw generateInvalidArithmeticException();
	}

	public void setParser(RecursiveDescentParser parser)
	{
		this.recParser = parser;
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		throw generateInvalidArithmeticException();
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		throw generateInvalidArithmeticException();
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
				Utils.removeOuterParenthesis(node.toString()));
	}

	@Override
	public String toTypeString()
	{
		return "function";
	}
}
