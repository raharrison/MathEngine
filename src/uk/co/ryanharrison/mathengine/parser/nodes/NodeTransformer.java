package uk.co.ryanharrison.mathengine.parser.nodes;

public interface NodeTransformer
{
	NodeVector toNodeVector();
	NodeNumber toNodeNumber();
}
