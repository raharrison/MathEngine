package uk.co.ryanharrison.mathengine.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EvaluatorSimpleBinaryTest
{
	private Evaluator evaluator;
	private double result;

	@Before
	public void init()
	{
		evaluator = Evaluator.newSimpleEvaluator();
	}

	@Test
	public void calculation001()
	{
		result = evaluator.evaluateDouble("4+3");
		Assert.assertEquals(7.0, result, 0.0);
	}

	@Test
	public void calculation002()
	{
		result = evaluator.evaluateDouble("5+((1+2)*4)-3");
		Assert.assertEquals(14.0, result, 0.0);
	}

	@Test
	public void calculation003()
	{
		result = evaluator.evaluateDouble("6+2*5");
		Assert.assertEquals(16.0, result, 0.0);
	}

	@Test
	public void calculation004()
	{
		result = evaluator.evaluateDouble("-8/2-5");
		Assert.assertEquals(-9.0, result, 0.0);
	}

	@Test
	public void calculation005()
	{
		result = evaluator.evaluateDouble("5*3+(6+1)");
		Assert.assertEquals(22.0, result, 0.0);
	}

	@Test
	public void calculation006()
	{
		result = evaluator.evaluateDouble("-5+7-(5*1)");
		Assert.assertEquals(-3.0, result, 0.0);
	}

	@Test
	public void calculation007()
	{
		result = evaluator.evaluateDouble("2-(-(7-2)+1)-4");
		Assert.assertEquals(2.0, result, 0.0);
	}

	@Test
	public void calculation008()
	{
		result = evaluator.evaluateDouble("-5*((-3*2)/(-3)+1)");
		Assert.assertEquals(-15.0, result, 0.0);
	}

	@Test
	public void calculation009()
	{
		result = evaluator.evaluateDouble("18+(9-(-3)+5)");
		Assert.assertEquals(35.0, result, 0.0);
	}

	@Test
	public void calculation010()
	{
		result = evaluator.evaluateDouble("-(4-(-16))");
		Assert.assertEquals(-20.0, result, 0.0);
	}

	@Test
	public void calculation011()
	{
		result = evaluator.evaluateDouble("3-(4-(5-7))-(9-(5-(-4)))");
		Assert.assertEquals(-3.0, result, 0.0);
	}

	@Test
	public void calculation012()
	{
		result = evaluator.evaluateDouble("14-(8+7)-(4+2-3-(-4+5))");
		Assert.assertEquals(-3.0, result, 0.0);
	}

	@Test
	public void calculation013()
	{
		result = evaluator.evaluateDouble("15/(-3)");
		Assert.assertEquals(-5.0, result, 0.0);
	}

	@Test
	public void calculation014()
	{
		result = evaluator.evaluateDouble("7*(-3)+(2+3*(-5))");
		Assert.assertEquals(-34.0, result, 0.0);
	}

	@Test
	public void calculation015()
	{
		result = evaluator.evaluateDouble("8+10/2-4*2");
		Assert.assertEquals(5.0, result, 0.0);
	}

	@Test
	public void calculation016()
	{
		result = evaluator.evaluateDouble("29*((-10)+1)");
		Assert.assertEquals(-261.0, result, 0.0);
	}

	@Test
	public void calculation017()
	{
		result = evaluator.evaluateDouble("(-12)*7-13*(-5)");
		Assert.assertEquals(-19.0, result, 0.0);
	}

	@Test
	public void calculation018()
	{
		result = evaluator.evaluateDouble("(4-20)*13");
		Assert.assertEquals(-208.0, result, 0.0);
	}

	@Test
	public void calculation019()
	{
		result = evaluator.evaluateDouble("(-5)*7-9*(-4)");
		Assert.assertEquals(1.0, result, 0.0);
	}

	@Test
	public void calculation020()
	{
		result = evaluator.evaluateDouble("(-48+32)-(67-82)");
		Assert.assertEquals(-1.0, result, 0.0);
	}

	@Test
	public void calculation021()
	{
		result = evaluator.evaluateDouble("-(-13+(24-68))-(-48+95)");
		Assert.assertEquals(10.0, result, 0.0);
	}

	@Test
	public void calculation022()
	{
		result = evaluator.evaluateDouble("12*-7*-12");
		Assert.assertEquals(1008.0, result, 0.0);
	}

	@Test
	public void calculation023()
	{
		result = evaluator.evaluateDouble("48-(15-(43-38)-27)");
		Assert.assertEquals(65.0, result, 0.0);
	}

	@Test
	public void calculation024()
	{
		result = evaluator.evaluateDouble("-32-(19-(24-46))");
		Assert.assertEquals(-73.0, result, 0.0);
	}

	@Test
	public void calculation025()
	{
		result = evaluator.evaluateDouble("-(24-89+18)+(-91+24)");
		Assert.assertEquals(-20.0, result, 0.0);
	}

	@Test
	public void calculation026()
	{
		result = evaluator.evaluateDouble("-2^2");
		Assert.assertEquals(-4.0, result, 0.0);
	}

	@Test
	public void calculation027()
	{
		result = evaluator.evaluateDouble("5*2^4+4*2^2-6*2+4");
		Assert.assertEquals(88.0, result, 0.0);
	}

	@Test
	public void calculation028()
	{
		result = evaluator.evaluateDouble("3^3*3^4*3");
		Assert.assertEquals(6561.0, result, 0.0);
	}

	@Test
	public void calculation029()
	{
		result = evaluator.evaluateDouble("5^7/5^3");
		Assert.assertEquals(625.0, result, 0.0);
	}

	@Test
	public void calculation030()
	{
		result = evaluator.evaluateDouble("(5^3)^4");
		Assert.assertEquals(244140625.0, result, 0.0);
	}

	@Test
	public void calculation031()
	{
		result = evaluator.evaluateDouble("(5*2*3)^4");
		Assert.assertEquals(810000.0, result, 0.0);
	}

	@Test
	public void calculation032()
	{
		result = evaluator.evaluateDouble("(3^4)^4");
		Assert.assertEquals(43046721.0, result, 0.0);
	}

	@Test
	public void calculation033()
	{
		result = evaluator
				.evaluateDouble("(((2-1/5)^2)/((3-2/9)^(-1))) / (((6/7)*(5/4)-(2/7)/(1/2))^3)/((1/2)-(1/3)*(1/4)/(1/5))-5*(1/7)");
		Assert.assertEquals(863.2857142857146, result, 0);
	}
}
