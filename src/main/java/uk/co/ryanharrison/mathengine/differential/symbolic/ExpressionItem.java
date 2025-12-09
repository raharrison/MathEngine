package uk.co.ryanharrison.mathengine.differential.symbolic;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.utils.Utils;

class ExpressionItem {
    private char[] operators = {'+', '-', '/', '*', '^'};

    String input;
    char operator;
    String function;
    int sign;

    public ExpressionItem(String input) {
        this.input = input;
        this.operator = (char) 0;
        this.function = null;
        this.sign = 1;
    }

    public String getInput() {
        if ((!Utils.isNumeric(input) && !input.equals("pi") && !input.equals("e")) && Utils.indexOfAny(input, operators) != -1)
            return '(' + input + ')';
        return input;
    }

    private static final Differentiator differentiator = new Differentiator();

    private static String d(String u) {
        return differentiator.differentiate(new Function(u), true).getEquation();
    }

    public String getDifferentiation() {
        String result = "";

        if (function != null) {
            int nIndex = input.indexOf("(");
            String str = input.substring(nIndex + 1);
            // get the string between function parentheses
            String u = str.substring(0, str.lastIndexOf(')'));
            switch (function) {
                case "":
                    result = d(u);
                    break;
                case "sin":
                    result = d(u) + "*cos(" + u + ")";
                    break;                // du*cos(u)
                case "cos":
                    result = d(u) + "*-sin(" + u + ")";
                    break;                    // du*(-sin(u))
                case "tan":
                    result = d(u) + "*sec(" + u + ")^2";
                    break;                    // du*sec(u)^2
                case "sec":
                    result = d(u) + "*sec(" + u + ")*tan(" + u + ")";
                    break;            // du*sec(u)*tan(u)
                case "cosec":
                    result = "(-" + d(u) + ")*cosec(" + u + ")*cot(" + u + ")";
                    break;    // -du*cosec(u)*cot(u)
                case "cot":
                    result = d(u) + "*-cosec(" + u + ")^2";
                    break;                // du*(-cosec(u)^2)
                case "sinh":
                    result = d(u) + "*cosh(" + u + ")";
                    break;                // du*cosh(u)
                case "cosh":
                    result = d(u) + "*sinh(" + u + ")";
                    break;                // du*(sinh(u))
                case "tanh":
                    result = d(u) + "*sech(" + u + ")^2";
                    break;                // du*sech(u)^2
                case "sech":
                    result = d(u) + "*sech(" + u + ")*tanh(" + u + ")";
                    break;        // du*sech(u)*tanh(u)
                case "cosech":
                    result = "(-" + d(u) + ")*cosech(" + u + ")*coth(" + u + ")";
                    break;    // -du*cosech(u)*coth(u)
                case "coth":
                    result = d(u) + "*-cosech(" + u + ")^2";
                    break;            // -du*cosech(u)^2
                case "asin":
                    result = d(u) + "/sqrt(1-(" + u + ")^2)";
                    break;            // du/sqrt(1-u^2)
                case "acos":
                    result = "(-" + d(u) + ")/sqrt(1-(" + u + ")^2)";
                    break;        // -du/sqrt(1-u^2)
                case "atan":
                    result = d(u) + "/(1+(" + u + ")^2)";
                    break;        // du/(1+u^2)
                case "asec":
                    result = d(u) + "/(abs(" + u + ")*sqrt((" + u + ")^2-1))";
                    break;// du/(|u|*sqrt(u^2-1))
                case "acosec":
                    result = "(-" + d(u) + ")/(abs(" + u + ")*sqrt((" + u + ")^2-1))";
                    break;// -du/(|u|*sqrt(u^2-1))
                case "acot":
                    result = "(-" + d(u) + ")/(1+(" + u + ")^2)";
                    break;            // -du/(1+u^2)
                case "asinh":
                    result = d(u) + "/sqrt((" + u + ")^2+1)";
                    break;            // du/sqrt(u^2+1)
                case "acosh":
                    result = d(u) + ")/sqrt((" + u + ")^2-1)";
                    break;        // du/sqrt(u^2-1)
                case "atanh":
                    result = d(u) + "/(1-(" + u + ")^2)";
                    break;            // du/(1-u^2)
                case "asech":
                    result = "(-" + d(u) + ")/((" + u + ")*sqrt(1-(" + u + ")^2))";
                    break; // -du/(u*sqrt(1-u^2))
                case "acosech":
                    result = "(-" + d(u) + ")/((" + u + ")*sqrt(1+(" + u + ")^2))";
                    break; // -du/(u*sqrt(1+u^2));
                case "acoth":
                    result = d(u) + "/(1-(" + u + ")^2)";
                    break;            // du/(1-u^2)
                case "sqrt":
                    result = d(u) + "/(2*sqrt(" + u + "))";
                    break;            // du/(2*sqrt(u))
                case "log10":
                    result = d(u) + "/((" + u + ")*log(10))";
                    break;            // du/(u*log(10))
                case "log":
                    result = d(u) + "/(" + u + ")";
                    break;            // du/u
                case "ln":
                    result = d(u) + "/(" + u + ")";
                    break;                // du/u
                case "sign":
                    result = "0";
                    break;
                case "abs":
                    result = d(u) + "*(" + u + ")/abs(" + u + ")";
                    break;        // du*u/abs(u)
            }

            result = (sign == -1 ? "-" : "") + result;
        } else {
            // dx/dx = 1
            if (input.equals("x") || input.equals("+x"))
                result = "1";
            else if (input.equals("-x"))
                result = "-1";
            else if (Utils.isNumeric(input) || input.equals("pi") || input.equals("e"))
                // dc/dx = 0, where c is constant
                result = "0";
            else
                // du/dx, where u is a function of x
                result = 'd' + input + "/dx";
        }

        return result;
    }
}