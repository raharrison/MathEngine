package uk.co.ryanharrison.mathengine.linearalgebra;

import uk.co.ryanharrison.mathengine.StatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a vector of values. Various operations can then be carried
 * out on the vectors
 * 
 * @author Ryan Harrison
 * 
 */
public final class Vector
{
	/** The values that this vector holds */
	private double[] values;

	/** The size of this vector */
	private int size;

	/**
	 * Construct a new Vector with one value
	 * 
	 * @param d
	 *            The one value
	 */
	public Vector(double d)
	{
		this.size = 1;
		values = new double[1];
		values[0] = d;
	}

	/**
	 * Construct a new Vector with the specified values
	 * 
	 * @param values
	 *            The values of the vector
	 */
	public Vector(double[] values)
	{
		this.size = values.length;
		this.values = values;
	}

	/**
	 * Construct a new Vector with specified size. All elements will default to
	 * zero
	 * 
	 * @param size
	 *            The size of the vector
	 */
	public Vector(int size)
	{
		this.size = size;
		values = new double[size];
	}

	/**
	 * Construct a new Vector from a string representation of the vector
	 * 
	 * @param vector
	 *            The string representation of the vector. This is a curly brace
	 *            followed by comma separated values followed by the ending
	 *            curly brace. e.g - "{1,2,3,4,5}"
	 */
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

	/**
	 * Add a number to each of the values in this vector
	 * 
	 * @param d
	 *            The number to add
	 * @return A new {@link Vector} with the number added on to the current
	 *         values
	 */
	public Vector add(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] + d);
		}

		return result;
	}

	/**
	 * Add a vector to the current vector
	 * 
	 * @param vector
	 *            The vector to add
	 * @return The result of this vector + the parameter vector
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone()
	{
		Vector vector = new Vector(values.clone());
		return vector;
	}

	/**
	 * Calculate the cross product of this and another vector
	 * 
	 * @param vector
	 *            The other vector
	 * @return The cross product of the two vectors
	 * @exception IllegalArgumentException
	 *                If either vector has a size that is not three
	 */
	public Vector crossProduct(Vector vector)
	{
		if (this.size != 3)
		{
			throw new IllegalArgumentException(
					"Vector v1 must be 3 dimensional!");
		}
		if (vector.getSize() != 3)
		{
			throw new IllegalArgumentException(
					"Vector v2 must be 3 dimensional!");
		}
		Vector result = new Vector(3);

		result.set(0, values[1] * vector.values[2] - values[2]
				* vector.values[1]);
		result.set(1, values[2] * vector.values[0] - values[0]
				* vector.values[2]);
		result.set(2, values[0] * vector.values[1] - values[1]
				* vector.values[0]);

		return result;
	}

	/**
	 * Divide a number to each of the values in this vector
	 * 
	 * @param d
	 *            The number to divide
	 * @return A new {@link Vector} with each number of the current vector
	 *         divided by d
	 */
	public Vector divide(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] / d);
		}

		return result;
	}

	/**
	 * Divide a vector with the current vector
	 * 
	 * @param vector
	 *            The vector to divide
	 * @return The result of this vector / the parameter vector
	 */
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

	/**
	 * Calculate the dot product of this and another vector
	 * 
	 * @param vector
	 *            The other vector
	 * @return The dot product of the two vectors
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Vector)
		{
			return this.equals((Vector) obj);
		}

		return false;
	}

	/**
	 * Determines if two Vectors are equal. That is they contain the same values
	 * 
	 * @param vector
	 *            The other vector to test
	 * @return True if both vectors contain the same values, otherwise false
	 */
	public boolean equals(Vector vector)
	{
		return Arrays.equals(values, vector.values);
	}

	/**
	 * Calculate the exponent of each of the current vector values
	 * 
	 * @return A new vector where each number is raised from e
	 */
	public Vector exp()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.exp(values[i]));
		}

		return result;
	}

	/**
	 * Get a specific value in the vector
	 * 
	 * @param index
	 *            The index of the value
	 * @return The value at the specified index
	 * @exception IllegalArgumentException
	 *                If the size is invalid
	 */
	public double get(int index)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException(
					"Requested vector index is out of range");
		}

		return values[index];
	}

	/**
	 * Get the values that this vector represents
	 * 
	 * @return The values that this vector represents
	 */
	public double[] getElements()
	{
		return this.values;
	}

	/**
	 * Get the normal of this vector
	 * 
	 * @return The normal of this vector
	 */
	public double getNorm()
	{
		double result = 0.0;

		for (int i = 0; i < size; i++)
		{
			result += values[i] * values[i];
		}
		return Math.sqrt(result);
	}

	/**
	 * Get the squared normal of this vector
	 * 
	 * @return The squared normal of this vector
	 */
	public double getNormSquare()
	{
		double result = 0.0;

		for (int i = 0; i < size; i++)
		{
			result += values[i] * values[i];
		}
		return result;
	}

	/**
	 * Get the size of this vector
	 * 
	 * @return The size of this vector
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Get this vector as a unit vector
	 * 
	 * @return A new vector representing this vector as a unit vector
	 */
	public Vector getUnitVector()
	{
		Vector result = new Vector(values);
		result.normalize();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return this.values.hashCode();
	}

	/**
	 * Determines if a character can be considered to be numeric whilst parsing
	 * the vector
	 * 
	 * @param c
	 *            The character to test
	 * @return Whether or not the character can be considered to be numeric
	 */
	private boolean isNumericCharacter(char c)
	{
		return Character.isDigit(c) || c == '.' || c == '-' || c == 'e';
	}

	/**
	 * Get the natural logarithm of each of this vectors values
	 * 
	 * @return A new vector containing the natural logarithm of each of this
	 *         vectors values
	 */
	public Vector log()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.log(values[i]));
		}

		return result;
	}

	/**
	 * Get the maximum value in this vector
	 * 
	 * @return The maximum value in this vector
	 */
	public double max()
	{
		return StatUtils.max(values);
	}

	/**
	 * Get the mean of this vector
	 * 
	 * @return The mean of this vector
	 */
	public double mean()
	{
		return StatUtils.mean(values);
	}

	/**
	 * Get the minimum value in this vector
	 * 
	 * @return The minimum value in this vector
	 */
	public double min()
	{
		return StatUtils.min(values);
	}

	/**
	 * Multiply a number to each of the values in this vector
	 * 
	 * @param d
	 *            The number to multiply
	 * @return A new {@link Vector} with each number of the current vector
	 *         values multiplied by d
	 */
	public Vector multiply(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] * d);
		}

		return result;
	}

	/**
	 * Multiply a vector with the current vector
	 * 
	 * @param vector
	 *            The vector to multiply
	 * @return The result of this vector * the parameter vector
	 */
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

	/**
	 * Normalise the current vector
	 * 
	 * @exception IllegalArgumentException
	 *                If the normal of this vector is zero
	 */
	public void normalize()
	{
		double norm = getNorm();

		if (norm == 0)
		{
			throw new IllegalArgumentException(
					"Tried to normalize a vector with norm of zero");
		}
		for (int i = 0; i < size; i++)
		{
			values[i] /= norm;
		}
	}

	/**
	 * Ensure that this vector and b have the same sizes. If the sizes are
	 * different, both vectors will take on the size of the largest vector. All
	 * new values will be zero.
	 * 
	 * @param b
	 *            The second vector
	 */
	private void normalizeVectorSizes(Vector b)
	{
		if (this.size == b.size)
			return;

		int longest = Math.max(this.size, b.size);

		double[] results = new double[longest];

		if (this.size != longest)
		{
			for (int i = 0; i < this.size; i++)
				results[i] = this.values[i];
			for (int i = this.size; i < longest; i++)
				results[i] = 0.0;

			setElements(results);
		}
		else
		{
			for (int i = 0; i < b.size; i++)
				results[i] = b.values[i];
			for (int i = b.size; i < longest; i++)
				results[i] = 0.0;

			b.setElements(results);
		}
	}

	/**
	 * Raise each of this vectors values to the power of d
	 * 
	 * @param k
	 *            The number to raise to
	 * @return A new {@link Vector} with each number of the current vector
	 *         raised to the power of k
	 */
	public Vector pow(double k)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.pow(values[i], k));
		}

		return result;
	}

	/**
	 * Raise each of this vectors values to the power of the corresponding value
	 * in the second vector
	 * 
	 * @param vector
	 *            The vector to raise to
	 * @return The result of this vector ^ the parameter vector
	 */
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

	/**
	 * Set a value in this vector at the specified index
	 * 
	 * @param index
	 *            The index to set
	 * @param value
	 *            The new value to set
	 * @exception IllegalArgumentException
	 *                If the index is out of range
	 */
	public void set(int index, double value)
	{
		if (index < 0 || index > size)
		{
			throw new IllegalArgumentException(
					"Requested vector index is out of range");
		}

		values[index] = value;
	}

	/**
	 * Set the elements of this vector
	 * 
	 * @param elements
	 *            The new elements
	 */
	public void setElements(double[] elements)
	{
		this.values = elements;
		this.size = elements.length;
	}

	/**
	 * Get the square root of each of this vectors values
	 * 
	 * @return A new vector containing square root of each of this vectors
	 *         values
	 */
	public Vector sqrt()
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, Math.sqrt(values[i]));
		}

		return result;
	}

	/**
	 * Subtract a number from each of the values in this vector
	 * 
	 * @param d
	 *            The number to subtract
	 * @return A new {@link Vector} with each number of the current vector
	 *         values subtracted by d
	 */
	public Vector subtract(double d)
	{
		Vector result = new Vector(size);

		for (int i = 0; i < size; i++)
		{
			result.set(i, values[i] - d);
		}

		return result;
	}

	/**
	 * Subtract a vector from the current vector
	 * 
	 * @param vector
	 *            The vector to subtract
	 * @return The result of this vector - the parameter vector
	 */
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

	/**
	 * Get the sum of this vectors values
	 * 
	 * @return The sum of the values
	 */
	public double sum()
	{
		return StatUtils.sum(values);
	}

	/**
	 * Convert this vector to an array of doubles
	 * 
	 * @return An array of doubles representing this vectors values
	 */
	public double[] toArray()
	{
		return values;
	}

	/**
	 * Convert this vector to a String form. This takes the form
	 * "{value1, value2, value3}" {@inheritDoc}
	 */
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
