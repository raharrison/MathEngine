package uk.co.ryanharrison.mathengine.parser.operators;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;

public interface Determinable
{
	NodeConstant getResult(NodeConstant number);
}
