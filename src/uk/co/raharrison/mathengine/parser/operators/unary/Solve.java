package uk.co.raharrison.mathengine.parser.operators.unary;

import java.util.List;

import uk.co.raharrison.mathengine.MathUtils;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;
import uk.co.raharrison.mathengine.solvers.BrentSolver;

public class Solve extends UnaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if(arg1 instanceof NodeFunction)
		{
			BrentSolver solver = new BrentSolver(((NodeFunction) arg1).toFunction());
			solver.setIterations(150);
			solver.setLowerBound(-25);
			solver.setUpperBound(25);
			List<Double> roots = solver.solveAll();
			Node[] results = new Node[roots.size()];
			for (int i = 0; i < roots.size(); i++)
			{
				results[i] = new NodeDouble(MathUtils.round(roots.get(i), 5));
			}
			return new NodeVector(results);
		}
		else if(arg1 instanceof NodeVector)
		{
			NodeVector vec = (NodeVector) arg1;
			if(vec.getSize() == 3)
			{
				Node[] vals = vec.getValues();
				if(vals[0] instanceof NodeFunction)
				{
					BrentSolver solver = new BrentSolver(((NodeFunction) vals[0]).toFunction());
					solver.setIterations(150);
					solver.setLowerBound(vals[1].toNodeNumber().doubleValue());
					solver.setUpperBound(vals[2].toNodeNumber().doubleValue());
					List<Double> roots = solver.solveAll();
					Node[] results = new Node[roots.size()];
					for (int i = 0; i < roots.size(); i++)
					{
						results[i] = new NodeDouble(MathUtils.round(roots.get(i), 5));
					}
					return new NodeVector(results);	
				}
				else
					throw new IllegalArgumentException("Expected first argument to be a function");
			}
			else
				throw new IllegalArgumentException("Expected 3 arguments (function, lower, upper)");
		}
		else
			throw new IllegalArgumentException("Operator must have a function argument");
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "solve", "roots" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "solve";
	}
}
