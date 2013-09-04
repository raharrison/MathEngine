package uk.co.ryanharrison.mathengine.parser.nodes;

public interface NodeTransformer
{
	NodeVector toNodeVector();
	NodeMatrix toNodeMatrix();
	NodeNumber toNodeNumber();
}
