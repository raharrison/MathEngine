package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Utils;

final class Vector
{
	private NodeNumber[] values;
	private int size;

	public Vector(int size)
	{
		this.size = size;
		values = new NodeNumber[size];
	}

	public Vector(NodeNumber d)
	{
		this.size = 1;
		values = new NodeNumber[1];
		values[0] = d.clone();
	}

	public Vector(NodeNumber[] values)
	{
		this.size = values.length;
		this.values = values.clone();
	}

	public Vector add(NodeNumber d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].add(d));
		}

		return result;
	}

	public Vector add(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].add(vector.values[i]));
		}

		return result;
	}

	@Override
	public Object clone()
	{
		Vector vector = new Vector(values.clone());
		return vector;
	}

	public Vector divide(NodeNumber d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].divide(d));
		}

		return result;
	}

	public Vector divide(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].divide(vector.values[i]));
		}

		return result;
	}

	public NodeNumber dotProduct(Vector vector)
	{
		normalizeVectorSizes(vector);
		NodeNumber result = NodeFactory.createZeroNumber();

		for (int i = 0; i < size; i++)
		{
			result = result.add(values[i].multiply(vector.values[i])).getTransformer().toNodeNumber();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Vector)
		{
			return this.equals((Vector) obj);
		}

		return false;
	}

	public boolean equals(Vector vector)
	{
		if (getSize() != vector.getSize())
			return false;

		for (int i = 0; i < size; i++)
		{
			if (!get(i).equals(vector.get(i)))
				return false;
		}

		return true;
	}

	public NodeNumber get(int index)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException("Requested vector index is out of range");
		}

		return values[index];
	}

	public NodeNumber[] getElements()
	{
		return this.values;
	}

	public int getSize()
	{
		return size;
	}

	@Override
	public int hashCode()
	{
		return this.values.hashCode();
	}

	public Vector multiply(NodeNumber d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].multiply(d));
		}

		return result;
	}

	public Vector multiply(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].multiply(vector.values[i]));
		}

		return result;
	}

	private void normalizeVectorSizes(Vector b)
	{
		if (this.size == b.size)
			return;

		int longest = Math.max(this.size, b.size);

		NodeNumber[] results = new NodeNumber[longest];

		if (this.size != longest)
		{
			for (int i = 0; i < this.size; i++)
				results[i] = this.values[i];
			for (int i = this.size; i < longest; i++)
				results[i] = NodeFactory.createZeroNumber();

			setElements(results);
		}
		else
		{
			for (int i = 0; i < b.size; i++)
				results[i] = b.values[i];
			for (int i = b.size; i < longest; i++)
				results[i] = NodeFactory.createZeroNumber();

			b.setElements(results);
		}
	}

	public Vector pow(NodeNumber k)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].pow(k));
		}

		return result;
	}

	public Vector pow(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].pow(vector.values[i]));
		}

		return result;
	}

	public void set(int index, NodeConstant value)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException("Requested vector index is out of range");
		}

		values[index] = value.getTransformer().toNodeNumber();
	}

	public void setElements(NodeNumber[] elements)
	{
		this.values = elements;
		this.size = elements.length;
	}

	public Vector subtract(NodeNumber d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].subtract(d));
		}

		return result;
	}

	public Vector subtract(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i].subtract(vector.values[i]));
		}

		return result;
	}

	public NodeNumber sum()
	{
		NodeNumber result = NodeFactory.createZeroNumber();
		for (int i = 0; i < values.length; i++)
		{
			result = result.add(values[i]).getTransformer().toNodeNumber();
		}
		return result;
	}

	public NodeNumber[] toArray()
	{
		return values;
	}

	@Override
	public String toString()
	{
		return Utils.join(values, ",");
	}
}
