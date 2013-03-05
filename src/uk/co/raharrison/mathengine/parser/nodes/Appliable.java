package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.parser.operators.Determinable;

public interface Appliable
{
	NodeConstant applyDeterminable(Determinable deter);
}