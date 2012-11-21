package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodeUnit;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;
import uk.co.raharrison.mathengine.unitconversion.units.Conversion;

public class Convert extends ConversionOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2, ConversionEngine engine)
	{
		if (arg1 instanceof NodeUnit && arg2 instanceof NodeUnit)
		{
			NodeUnit unit1 = (NodeUnit) arg1;
			NodeUnit unit2 = (NodeUnit) arg2;

			Conversion result = engine.convert(unit1.doubleValue(), unit1.getUnit().getBaseAliasSingular(), unit2.getUnit().getBaseAliasSingular());
			NodeNumber value = NodeFactory.createNodeNumberFrom(result.getResult());
			
			NodeUnit unit = new NodeUnit(result.getTo(), value);
			return unit;
		}

		throw new IllegalArgumentException("Incorrect conversion parameters");
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "in", "to", "as", "convert" };
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
