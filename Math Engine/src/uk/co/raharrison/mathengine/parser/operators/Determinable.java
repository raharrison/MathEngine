package uk.co.raharrison.mathengine.parser.operators;

import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;

public interface Determinable
{
	NodeNumber getResult(NodeNumber number);
}
