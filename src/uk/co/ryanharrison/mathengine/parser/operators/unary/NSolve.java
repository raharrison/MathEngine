package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;
import uk.co.ryanharrison.mathengine.solvers.ConvergenceCriteria;
import uk.co.ryanharrison.mathengine.solvers.NewtonRaphsonSolver;

public class NSolve extends UnaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeFunction)
		{
			Function func = ((NodeFunction) arg1).toFunction();
			NewtonRaphsonSolver solver = new NewtonRaphsonSolver(func);
			solver.setIterations(25);
			solver.setConvergenceCriteria(ConvergenceCriteria.NumberOfIterations);
			return new NodeDouble(solver.solve());
		}
		else if (arg1 instanceof NodeVector)
		{
			NodeVector vec = (NodeVector) arg1;
			Node[] elements = vec.getValues();
			if (vec.getSize() == 1)
			{
				if (elements[0] instanceof NodeFunction)
				{
					NewtonRaphsonSolver solver = new NewtonRaphsonSolver(((NodeFunction) elements[0]).toFunction());
					solver.setIterations(25);
					solver.setConvergenceCriteria(ConvergenceCriteria.NumberOfIterations);
					return new NodeDouble(solver.solve());
				}
				throw new IllegalArgumentException("Must have function in arguments");
			}
			else if (vec.getSize() == 2)
			{
				if (elements[0] instanceof NodeFunction)
				{
					NewtonRaphsonSolver solver = new NewtonRaphsonSolver(((NodeFunction) elements[0]).toFunction());
					solver.setIterations(25);
					solver.setInitialGuess((int) elements[1].getTransformer().toNodeNumber().doubleValue());
					solver.setConvergenceCriteria(ConvergenceCriteria.NumberOfIterations);
					return new NodeDouble(solver.solve());
				}
				throw new IllegalArgumentException("Must have function in arguments");
			}
			else
				throw new IllegalArgumentException("Must have one or two arguments");
		}

		throw new IllegalArgumentException("Operator must have a function argument");
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "nsolve" };
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
		return "nsolve";
	}
}
