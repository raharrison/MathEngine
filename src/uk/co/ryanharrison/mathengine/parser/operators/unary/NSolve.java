package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.solvers.ConvergenceCriteria;
import uk.co.ryanharrison.mathengine.solvers.NewtonRaphsonSolver;

public class NSolve extends VectorOperator
{
	@Override
	protected NodeConstant calculateResultFromVector(NodeVector arg1)
	{
		Node[] elements = arg1.getValues();
		
		if(!(elements[0] instanceof NodeFunction))
			throw new IllegalArgumentException("First argument must be a function");
		
		NewtonRaphsonSolver solver = new NewtonRaphsonSolver(((NodeFunction) elements[0]).toFunction());
		solver.setIterations(25);
		solver.setConvergenceCriteria(ConvergenceCriteria.NumberOfIterations);
		
		if (arg1.getSize() == 2)
		{
			solver.setInitialGuess((int) elements[1].getTransformer().toNodeNumber().doubleValue());
			return new NodeDouble(solver.solve());
		}
		
		return new NodeDouble(solver.solve());
	}

	@Override
	protected String getExpectedArgumentsString()
	{
		return "function, initialGuess";
	}

	@Override
	protected void fillAcceptedArguments()
	{
		acceptedArgumentLengths.add(1);
		acceptedArgumentLengths.add(2);
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
