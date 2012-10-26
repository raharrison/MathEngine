package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeUnit;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;

public class Convert extends BinaryOperator
{
	private ConversionEngine engine;
	
	public Convert()
	{
		engine = new ConversionEngine();
	}
	
	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if(arg1 instanceof NodeUnit && arg2 instanceof NodeUnit)
		{
			NodeUnit unit1 = (NodeUnit) arg1;
			NodeUnit unit2 = (NodeUnit) arg2;
			return new NodeUnit(unit2.getUnit(), engine.convert(unit1.doubleValue(), unit1.getUnit(), unit2.getUnit()));
		}
		
		throw new IllegalArgumentException("Incorrect conversion parameters");
	}

	@Override
	public String[] getAliases()
	{
		return new String[] {"in", "to", "as", "convert"};
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public String toLongString()
	{
		return "conversion";
	}

	@Override
	public String toString()
	{
		return "in";
	}

}
