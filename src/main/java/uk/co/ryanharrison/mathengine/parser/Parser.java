package uk.co.ryanharrison.mathengine.parser;

public interface Parser<T, R>
{
	public R parse(T t);
}
