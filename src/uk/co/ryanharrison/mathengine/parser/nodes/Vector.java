package uk.co.ryanharrison.mathengine.parser.nodes;

public final class Vector
{
	private NodeNumber[] values;
	private int size;

	public NodeNumber get(int index)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException("Requested vector index is out of range");
		}

		return values[index];
	}

	public int getSize()
	{
		return size;
	}

	public void setElements(NodeNumber[] elements)
	{
		this.values = elements;
		this.size = elements.length;
	}

}
