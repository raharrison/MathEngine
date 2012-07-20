package uk.co.raharrison.mathengine.special;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class Primes
{
	public static List<Long> distinctPrimeFactors(long n)
	{
		List<Long> factors = primeFactors(n);

		LinkedHashSet<Long> distinct = new LinkedHashSet<>(factors);

		return new ArrayList<Long>(distinct);
	}

	public static boolean isPrime(long n)
	{
		n = Math.abs(n);

		boolean prime = true;

		for (long i = 3; i <= Math.sqrt(n); i += 2)
		{
			if (n % i == 0)
			{
				prime = false;
				break;
			}
		}

		if ((n % 2 != 0 && prime && n > 2) || n == 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void main(String[] args)
	{
		System.out.println(primeFactors(-24783));
	}

	public static long nextPrime(long n)
	{
		n++;

		if (n % 2 == 0)
		{
			n++;
		}

		long result = n;

		while (!isPrime(result))
		{
			result += 2;
		}

		return result;
	}

	public static long previousPrime(long n)
	{
		n--;

		if (n % 2 == 0)
		{
			n--;
		}

		long result = n;

		while (!isPrime(result))
		{
			result -= 2;
		}

		return result;
	}

	public static List<Long> primeFactors(long n)
	{
		long num = Math.abs(n);

		List<Long> factors = new ArrayList<Long>();

		if (num == 1)
		{
			factors.add(num);
		}

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

		// negate first factors if necessary
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
