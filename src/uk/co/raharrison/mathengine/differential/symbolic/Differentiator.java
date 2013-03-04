package uk.co.raharrison.mathengine.differential.symbolic;

import java.util.List;

import uk.co.raharrison.mathengine.Function;
import uk.co.raharrison.mathengine.Utils;

public class Differentiator
{
	private int currentExpressionIndex;

	public static void main(String[] args)
	{
		System.out.println(new Differentiator().differentiate(new Function("sinx"), true));
	}

	public Function differentiate(Function equation, boolean optimize)
	{
		List<ExpressionItem> stack = TreeToStack.treeToStack(equation.getCompiledExpression());
		currentExpressionIndex = 0;
		String result = differentiateStack(stack, currentExpressionIndex);

		if (optimize)
			result = optimize(result.replace(" ", ""));

		return new Function(result);
	}

	private String differentiateStack(List<ExpressionItem> vStack, int nExpression)
	{
		currentExpressionIndex = nExpression;
		ExpressionItem pQi = vStack.get(currentExpressionIndex++);
		String result = "";

		if (pQi.operator != 0)
		{
			// get left operand
			String u = vStack.get(currentExpressionIndex).getInput();
			// get left operand differentiation
			String du = differentiateStack(vStack, currentExpressionIndex++);
			// get right operand
			String v = vStack.get(currentExpressionIndex).getInput();
			// get right operand differentiation
			String dv = differentiateStack(vStack, currentExpressionIndex++);

			if (du.equals("0")) // u is constant
				switch (pQi.operator)
				{
					case '-': // d(u-v) = -dv
						result = "(-" + dv + ')';
						break;
					case '+': // d(u+v) = dv
						result = dv;
						break;
					case '*': // d(u*v) = u*dv
						result = u + '*' + dv;
						break;
					case '/': // d(u/v) = (-u*dv)/v^2
						result = "(-" + u + '*' + dv + ")/(" + v + ")^2";
						break;
					case '^': // d(u^v) = dv*u^v*ln(u)
						result = dv + "*" + u + "^" + v + (u == "e" ? "" : "*ln(" + u + ")");
						break;
				}
			else if (dv.equals("0")) // v is constant
				switch (pQi.operator)
				{
					case '-': // d(u-v) = du
					case '+': // d(u+v) = du
						result = du;
						break;
					case '*': // d(u*v) = du*v
						result = du + '*' + v;
						break;
					case '/': // d(u/v) = du/v
						result = '(' + du + ")/" + v;
						break;
					case '^': // d(u^v) = v*u^(v-1)*du
						result = v + "*" + u + "^" + trim(Double.parseDouble(v) - 1) + "*" + du;
						break;
				}
			else
				switch (pQi.operator)
				{
					case '-': // d(u-v) = du-dv
					case '+': // d(u+v) = du+dv
						result = '(' + du + pQi.operator + dv + ')';
						break;
					case '*': // d(u*v) = u*dv+du*v
						result = '(' + u + '*' + dv + '+' + du + '*' + v + ')';
						break;
					case '/': // d(u/v) = (du*v-u*dv)/v^2
						result = '(' + du + '*' + v + '-' + u + '*' + dv + ")/(" + v + ")^2";
						break;
					case '^': // d(u^v) = v*u^(v-1)*du+u^v*ln(u)*dv
						result = '(' + v + '*' + u + "^(" + v + "-1)*" + du + '+' + u + '^' + v
								+ "*ln(" + u + ")*" + dv + ')';
						break;
				}
		}
		else
			// get Expression differentiation
			result = pQi.getDifferentiation();
		// return resultant differentiation
		return result;
	}

	private static String optimize(String s)
	{
		int nLength = s.length();

		s = optimizeSign(s);
		StringBuilder str = new StringBuilder(s);

		int nIndex = -1;

		// replace "((....))" with "(....)"
		while ((nIndex = str.indexOf("((", nIndex + 1)) != -1)
		{
			int pClose = Utils.matchingCharacterIndex(str.toString(), nIndex + 1, '(', ')') + 1;
			if (str.charAt(pClose) == ')')
			{
				str = str.delete(pClose, pClose + 1);
				str = str.delete(nIndex, nIndex + 1);
			}
		}

		nIndex = -1;
		// remove any 1*
		while ((nIndex = str.indexOf("1*", nIndex + 1)) != -1)
		{
			if (nIndex == 0 || "+-*(".indexOf(str.charAt(nIndex - 1)) != -1)
				str = str.delete(nIndex, nIndex + 2);

			if (nIndex + 1 > str.length())
				break;
		}

		nIndex = -1;
		// remove any *1
		while ((nIndex = str.indexOf("*1", nIndex + 1)) != -1)
		{
			if (nIndex + 2 == str.length() || "+-*(".indexOf(str.charAt(nIndex + 2)) != -1)
				str = str.delete(nIndex, nIndex + 2);

			if (nIndex + 1 > str.length())
				break;
		}

		nIndex = -1;
		// remove any exponent equal 1
		while ((nIndex = str.indexOf("^1", nIndex + 1)) != -1)
		{
			if (nIndex + 2 == str.length() || "+-*(".indexOf(str.charAt(nIndex + 2)) != -1)
				str = str.delete(nIndex, nIndex + 2);

			if (nIndex + 1 > str.length())
				break;
		}

		nIndex = 0;
		// remove unneeded parentheses
		while ((nIndex = str.indexOf("(", nIndex)) != -1)
		{
			// "nscthg0" is the end characters of all supported functions
			if (nIndex > 0 && "nscthg0".indexOf(str.charAt(nIndex - 1)) != -1)
			{
				nIndex++;
				continue;
			}
			// find the parenthesis close
			int pClose = Utils.matchingCharacterIndex(str.toString(), nIndex, '(', ')') + 1;
			if (pClose == nIndex)
				return str.toString();
			// get the index of the close char
			int nCloseIndex = pClose - 1;
			// check if the parentheses in the start and the end of the string
			if ((nIndex == 0 && nCloseIndex == str.length() - 1) || nCloseIndex == nIndex + 2 ||
			// check if the string doesn't include any operator
					Utils.isNumeric(str.substring(nIndex + 1, nCloseIndex)))
			{
				// delete the far index of ')'
				str = str.delete(nCloseIndex, nCloseIndex + 1);
				// delete the near index of '('
				str = str.delete(nIndex, nIndex + 1);
			}
			else
				nIndex++;
		}

		if (nLength != str.length())
			str = new StringBuilder(optimize(str.toString()));

		return str.toString();
	}

	private static String optimizeSign(String s)
	{
		int index = 0;

		StringBuilder str = new StringBuilder(s);
		// replace "--" with "" or "+"
		while ((index = str.indexOf("--", index)) != -1)
			if (index == 0
					|| Utils.indexOfAny(str.substring(index - 1), new char[] { '(', '+', '-', '/',
							'*', '^' }) != -1)
			{
				str = str.delete(index, index + 2);
			}
			else
			{
				str = str.delete(index, index + 1);
				str = str.insert(index, "+");
			}

		// replace "+-" with "-"
		return str.toString().replace("+-", "-");
	}

	private static String trim(double d)
	{
		String str = Double.toString(d);
		if (str.indexOf('.') != -1)
			str = Utils.trimEnd(str, '0');
		return Utils.trimEnd(str, '.');
	}
}
