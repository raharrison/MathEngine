package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;

public abstract class ConversionOperator extends BinaryOperator
{
	private ConversionEngine engine;
	
	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		return toResult(arg1, arg2, engine);
	}
	
	public abstract NodeConstant toResult(NodeConstant arg1, NodeConstant arg2, ConversionEngine engine);
	
	public void setConversionEngine(ConversionEngine engine)
	{
		this.engine = engine;
	}
}
