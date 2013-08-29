package uk.co.ryanharrison.mathengine;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.Evaluator;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;

public final class Function
{
	private String equation;
	private AngleUnit angleUnit;
	private String variable;
	private Evaluator evaluator;

	public Function(String equation)
	{
		this(equation, "x", AngleUnit.Radians);
	}

	public Function(String equation, AngleUnit angleUnit)
	{
		this(equation, "x", angleUnit);
	}

	public Function(String equation, String variable)
	{
		this(equation, variable, AngleUnit.Radians);
	}

	public Function(String equation, String variable, AngleUnit angleUnit)
	{
		this.equation = equation;
		this.variable = variable;
		this.angleUnit = angleUnit;
		this.evaluator = null;
	}
	
	private void initEvaluator()
	{
		this.evaluator = Evaluator.newSimpleEvaluator();

		this.evaluator.addVariable(this.variable, "0");
		this.evaluator.compileTree(this.equation);
		this.evaluator.setAngleUnit(this.angleUnit);
	}

	public double evaluateAt(double at)
	{
		return evaluateAt(Double.toString(at));
	}

	public double evaluateAt(String at)
	{
		if(this.evaluator == null)
			initEvaluator();
		
		this.evaluator.addVariable(variable, at);
		return this.evaluator.evaluateCachedTreeDouble();
	}

	public AngleUnit getAngleUnit()
	{
		return this.angleUnit;
	}

	public String getEquation()
	{
		return this.equation;
	}

	public String getVariable()
	{
		return this.variable;
	}
	
	public Node getCompiledExpression()
	{
		if(this.evaluator == null)
			initEvaluator();
		
		return evaluator.getCachedTree();
	}

	public void setAngleUnit(AngleUnit angleUnit)
	{
		this.angleUnit = angleUnit;
	}

	@Override
	public String toString()
	{
		return String.format("f(%s) = %s", this.variable, this.equation);
	}
}
