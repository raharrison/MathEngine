package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodePercent;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class AsPercentOf extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "as%of", "aspercentof", "aspercentageof", "asa%of", "asapercentof",
				"asapercentageof" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return "as a percentage of";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		NodeNumber a = arg1.toNodeNumber();
		NodeNumber b = arg2.toNodeNumber();
		NodeNumber hundred = NodeFactory.createNodeNumberFrom(100.0);

		return new NodePercent(a.divide(b).multiply(hundred).doubleValue());
	}

	@Override
	public String toString()
	{
		return "as % of";
	}

}
