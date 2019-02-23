package uk.co.ryanharrison.mathengine.parser.nodes;

public interface NodeTransformer
{
	NodeNumber toNodeNumber();
	NodeVector toNodeVector();
	NodeMatrix toNodeMatrix();
}
