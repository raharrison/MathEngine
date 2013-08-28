package uk.co.ryanharrison.mathengine.linearalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.ryanharrison.mathengine.StatUtils;

public final class Vector
{
	private double[] values;
	private int size;

	public Vector(double d)
	{
		this.size = 1;
		values = new double[1];
		values[0] = d;
	}

	public Vector(double[] values)
	{
		this.size = values.length;
		this.values = values;
	}

	public Vector(int size)
	{
		this.size = size;
		values = new double[size];
	}

	public Vector(String vector)
	{
		char[] chars = vector.toLowerCase().toCharArray();

		List<Double> nums = new ArrayList<Double>();
		String number = "";

		for (int i = 0; i < chars.length; i++)
		{
			if (isNumericCharacter(chars[i]))
			{
				number += chars[i];
			}
			else
			{
				if (!number.isEmpty())
					nums.add(Double.parseDouble(number));

				number = "";
			}
		}

		if (!number.isEmpty())
			nums.add(Double.parseDouble(number));

		values = new double[nums.size()];
		size = nums.size();

		for (int j = 0; j < size; j++)
		{
			values[j] = nums.get(j);
		}
	}

	public Vector add(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] + d);
		}

		return result;
	}

	public Vector add(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] + vector.values[i]);
		}

		return result;
	}

	@Override
	public Object clone()
	{
		Vector vector = new Vector(values.clone());
		return vector;
	}

	public Vector crossProduct(Vector vector)
	{
		if (this.size != 3)
		{
			throw new IllegalArgumentException("Vector v1 must be 3 dimensional!");
		}
		if (vector.getSize() != 3)
		{
			throw new IllegalArgumentException("Vector v2 must be 3 dimensional!");
		}
		Vector result = new Vector(3);

		result.set(0, values[1] * vector.values[2] - values[2] * vector.values[1]);
		result.set(1, values[2] * vector.values[0] - values[0] * vector.values[2]);
		result.set(2, values[0] * vector.values[1] - values[1] * vector.values[0]);

		return result;
	}

	public Vector divide(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] / d);
		}

		return result;
	}

	public Vector divide(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] / vector.values[i]);
		}

		return result;
	}

	public double dotProduct(Vector vector)
	{
		normalizeVectorSizes(vector);
		double result = 0.0;

		for (int i = 0; i < size; i++)
		{
			result += values[i] * vector.values[i];
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
		return Arrays.equals(values, vector.values);
	}

	public Vector exp()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.exp(values[i]));
		}

		return result;
	}

	public double get(int index)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException("Requested vector index is out of range");
		}

		return values[index];
	}

	public double[] getElements()
	{
		return this.values;
	}

	public double getNorm()
	{
		double result = 0.0;

		for (int i = 0; i < size; i++)
		{
			result += values[i] * values[i];
		}
		return Math.sqrt(result);
	}

	public double getNormSquare()
	{
		double result = 0.0;

		for (int i = 0; i < size; i++)
		{
			result += values[i] * values[i];
		}
		return result;
	}

	public int getSize()
	{
		return size;
	}

	public Vector getUnitVector()
	{
		Vector result = new Vector(values);
		result.normalize();
		return result;
	}

	@Override
	public int hashCode()
	{
		return this.values.hashCode();
	}

	private boolean isNumericCharacter(char c)
	{
		return Character.isDigit(c) || c == '.' || c == '-' || c == 'e';
	}

	public Vector log()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.log(values[i]));
		}

		return result;
	}

	public double max()
	{
		return StatUtils.max(values);
	}

	public double mean()
	{
		return StatUtils.mean(values);
	}

	public double min()
	{
		return StatUtils.min(values);
	}

	public Vector multiply(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] * d);
		}

		return result;
	}

	public Vector multiply(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] * vector.values[i]);
		}

		return result;
	}

	public void normalize()
	{
		double norm = getNorm();

		if (norm == 0)
		{
			throw new IllegalArgumentException("Tried to normalize a vector with norm of zero");
		}
		for (int i = 0; i < size; i++)
		{
			values[i] /= norm;
		}
	}

	private void normalizeVectorSizes(Vector b)
	{
		if (this.size == b.size)
			return;

		int longest = Math.max(this.size, b.size);

		if (this.size != longest)
		{
			double[] results = new double[longest];

			for (int i = 0; i < this.size; i++)
			{
				results[i] = this.values[i];
			}

			this.values = results;
			this.size = results.length;

		}
		else
		{
			double[] results = new double[longest];

			for (int i = 0; i < this.size; i++)
			{
				results[i] = this.values[i];
			}

			this.values = results;
			this.size = results.length;
		}
	}

	public Vector pow(double k)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.pow(values[i], k));
		}

		return result;
	}

	public Vector pow(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.pow(values[i], vector.values[i]));
		}

		return result;
	}

	public void set(int index, double value)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException("Requested vector index is out of range");
		}

		values[index] = value;
	}

	public void setElements(double[] elements)
	{
		this.values = elements;
		this.size = elements.length;
	}

	public Vector sqrt()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.sqrt(values[i]));
		}

		return result;
	}

	public Vector subtract(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] - d);
		}

		return result;
	}

	public Vector subtract(Vector vector)
	{
		normalizeVectorSizes(vector);
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] - vector.values[i]);
		}

		return result;
	}

	public double sum()
	{
		return StatUtils.sum(values);
	}

	public double[] toArray()
	{
		return values;
	}

	@Override
	public String toString()
	{
		if (size == 0)
		{
			return "{}";
		}

		String str = "{";

		for (int i = 0; i < size - 1; i++)
		{
			str += Double.toString(values[i]) + ", ";
		}
		str += Double.toString(values[size - 1]) + "}";
		return str;
	}
}
