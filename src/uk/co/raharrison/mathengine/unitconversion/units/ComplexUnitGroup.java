package uk.co.raharrison.mathengine.unitconversion.units;

import uk.co.raharrison.mathengine.parser.Evaluator;

public class ComplexUnitGroup extends UnitGroup
{
	private static Evaluator evaluator = Evaluator.newSimpleBinaryEvaluator();

	@Override
	protected double doConversion(Conversion params)
	{
		ComplexSubUnit from, to;

		if ((from = (ComplexSubUnit) params.getFrom()) != null
				&& (to = (ComplexSubUnit) params.getTo()) != null)
		{
			evaluator.addVariable(from.getVariable(), Double.toString(params.getValue()));

			String equation = from.getEquationFor(to);
			if (equation != null)
			{
				return evaluator.evaluateDouble(equation);
			}
			
			return params.getValue();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
}