# Math Engine

A mathematical library complete with an advanced expression parser with custom functions and support for Vectors and Matrices. Includes packages for symbolic differentiation, numeric integration, equation solving, unit conversions and much more.

### Functions

The Function class provides a common interface into many packages of this library. A Function models an equation of one variable that maps to a resulting Y value. To create a basic Function object use the constructor that takes a single String argument representing the desired equation:

    Function function = new Function("x^2 + 8*x + 12");
    
To create more customised Functions, use the overloaded constructors where you can define the variable and the angle units that will be used when calculating any trigonometric values in the function:

    Function function = new Function("x^2 + 8*x + 12", "x", AngleUnit.Radians);
    
By default the Function will have a variable of 'x' and will use Radians if they are not explicitly specified in the constructor.

Once and instance of the Function class is created, you also have the ability to evaluate the function at a specified point (any instances of the variable will become the specified value during evaluation). For example:

    Function function = new Function("x^2 + 8*x + 12");
    double result = function.evaluateAt(12.5);
    
    OUTPUT -
    268.25
    
You can also evaluate the expression at another expression. This is better explained in an example:


    Function function = new Function("x^2 + 8*x + 12");
    double result = function.evaluateAt("6 + 6.5");
    
    OUTPUT -
    268.25
    
### Differential

The differential package includes classes to both symbolically and numerically differentiate Function instances. Numeric differentiation will only allow you to differentiate at a certain point (giving the value of the derivative at this point). Conversely symbolic differentiation will return a new Function object representing the exact derivative to the Function. This resulting Function can then be evaluated as above to get the a value at a certain point (as in numeric differentiation).

#### Numeric

    Function function = new Function("x^2 + 8*x + 12");
    DividedDifferenceMethod method = new DividedDifferenceMethod(function, DifferencesDirection.Central);
    
In this example a Function is first created by which the deriviatives values will be calculated. Then a new instance of a DividedDifferenceMethod differentiator is created that uses the the [Finite Difference method](http://en.wikipedia.org/wiki/Finite_difference) with central equations. A result of the first derivative at the point 3.5 can be obtained by:

    method.setTargetPoint(3.5);
    double derivative = method.deriveFirst();

    OUTPUT - 
    15.000000000000213
    
Values can change depending on the supplied value of h (the difference). By default the value is 0.01. Other classes are available that can yield more accurate results in certain circumstances are also available which include the ExtendedCentralDifferenceMethod (more accurate than standard differences as above) and [RichardsonExtrapolationMethod](http://en.wikipedia.org/wiki/Richardson_extrapolation) (which again can yield more accurate results).

The numerical methods only give estimates of the derivatives and in some cases may be wildly inaccurate. However for most well formed, continuous functions, and a sensible value of h, a reasonably accurate answer can be obtained. For exact results consider the symbolic package.

