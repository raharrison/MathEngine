package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.operators.Determinable;

public interface Appliable
{
	NodeConstant applyDeterminable(Determinable deter);
}