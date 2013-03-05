package uk.co.raharrison.mathengine.parser;

public interface Parser<T, R>
{
	public R parse(T t);
}
