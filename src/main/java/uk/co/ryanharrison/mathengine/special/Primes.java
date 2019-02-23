package uk.co.ryanharrison.mathengine.special;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Class representing the infinite sequence of prime numbers
 * 
 * @author Ryan Harrison
 * 
 */
public final class Primes
{
	/**
	 * Get a List of the distinct prime factors of a number.
	 * 
	 * That is a list of the prime factors of a number that are all unique
	 * 
	 * @param n
	 *            The number to use
	 * @return A List of the distinct prime factors of n
	 */
	public static List<Long> distinctPrimeFactors(long n)
	{
		// generate the prime factors
		List<Long> factors = primeFactors(n);

		// add the factors to a set which maintains order. This will remove all
		// duplicates
		LinkedHashSet<Long> distinct = new LinkedHashSet<>(factors);

		// return the unique factors as a list
		return new ArrayList<Long>(distinct);
	}

	/**
	 * Determines whether or not a number is prime
	 * 
	 * @param n
	 *            The number to test
	 * @return True if the number n is prime, otherwise false
	 */
	public static boolean isPrime(long n)
	{
		n = Math.abs(n);

		boolean prime = true;

		// go through each odd number up to the square root of n
		for (long i = 3; i <= Math.sqrt(n); i += 2)
		{
			// if the number is a factor, then the number is not prime
			if (n % i == 0)
			{
				prime = false;
				break;
			}
		}

		// account for the special cases and return a result
		if ((n % 2 != 0 && prime && n > 2) || n == 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Get the next prime number in the sequence of primes after the specified
	 * number
	 * 
	 * @param n
	 *            The number to generate the next prime after
	 * @return The next prime number after n
	 */
	public static long nextPrime(long n)
	{
		// we want the next prime number
		n++;

		// make sure the number is not even
		if (n % 2 == 0)
		{
			n++;
		}

		long result = n;

		// while the number is not prime move to the next odd number
		while (!isPrime(result))
		{
			result += 2;
		}

		return result;
	}

	/**
	 * Get the previous prime number in the sequence of primes before the
	 * specified number
	 * 
	 * @param n
	 *            The number to generate the next prime before
	 * @return The previous prime number before n
	 */
	public static long previousPrime(long n)
	{
		// we want the prime number before n
		n--;

		// make sure the number is not even
		if (n % 2 == 0)
		{
			n--;
		}

		long result = n;

		// while the number is not prime move to the previous odd number
		while (!isPrime(result))
		{
			result -= 2;
		}

		return result;
	}

	/**
	 * Generate a List of the prime factors of a number
	 * 
	 * @param n
	 *            The number to generate the prime factors of
	 * @return a List of the prime factors of the number n
	 */
	public static List<Long> primeFactors(long n)
	{
		long num = Math.abs(n);

		List<Long> factors = new ArrayList<Long>();

		// account for special case
		if (num == 1)
		{
			factors.add(num);
		}

		// generate the factors
		for (long i = 2; i <= num / i; i++)
		{
			while (num % i == 0)
			{
				factors.add(i);
				num /= i;
			}
		}

		if (num >= 0 && num != 1)
		{
			factors.add(num);
		}

		// negate first factors if necessary (to make them all positive)
		if (n < 0 && factors.size() != 0)
		{
			long first = factors.get(0);

			for (int i = 0; i < factors.size(); i++)
			{
				long current = factors.get(i);

				if (current == first)
					factors.set(i, -current);
				else
					break;
			}
		}

		return factors;
	}
}
