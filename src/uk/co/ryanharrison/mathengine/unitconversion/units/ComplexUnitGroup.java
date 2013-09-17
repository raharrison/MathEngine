package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.BigRational;
import uk.co.ryanharrison.mathengine.parser.Evaluator;

public class ComplexUnitGroup extends UnitGroup
{
	private static Evaluator evaluator = Evaluator.newSimpleBinaryEvaluator();

	@Override
	protected BigRational doConversion(Conversion params)
	{
		ComplexSubUnit from, to;

		if ((from = (ComplexSubUnit) params.getFrom()) != null
				&& (to = (ComplexSubUnit) params.getTo()) != null)
		{
			evaluator.addVariable(from.getVariable(), params.getValue().doubleValue());

			String equation = from.getEquationFor(to);
			if (equation != null)
			{
				return new BigRational(evaluator.evaluateDouble(equation));
			}
			
			return params.getValue();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
}