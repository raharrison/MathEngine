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

#### Symbolic

The symbolic differentiation package offers support to obtain exact derivatives of Function objects. Instead of returning a double value of the derivative at a certain point (as in the numeric package), the symbolic package returns a new Function object representing the exact derivative function. This can then be evaluated to get the derivative at a certain point:

    Function function = new Function("x^2 + 8*x + 12");
    Differentiator differentiator = new Differentiator();
    Function derivative = differentiator.differentiate(function, true);
    derivative.evaluateAt(3.5);
    
    OUTPUT -
    f(x) = 2*x+8
    15.0
    
As you can see the symbolic differentiator returns a Function representing the equation of the derivative, which in this case is '2*x + 8'. This function is then evaluated at the some point 3.5 to get an answer of 15. Note how this result is exact and without the small error retrieved from the numeric method of differentiation.

### Distributions

The distributions package offers classes to evaluate both discrete and continuous probability distributions.

#### Discrete distributions -
 - Binomial

#### Continuous distributions -
 - Beta
 - Exponential
 - F
 - Logistic
 - Normal
 - Student T

Both the cumulative and density functions of each of these distributions can be evaluated by created the relevant object and calling either the .density or .cumulative methods. Here is an example using the Normal distribution:

    NormalDistribution normal = new NormalDistribution(15, 2.6);
    double density = normal.density(15.7);
    double cumulative = normal.cumulative(15.7);
    
    OUTPUT - 
    0.14797786432400178
    0.6061238859734724
    
### GUI

The gui package includes graphical user interfaces to various packages in the library, along with other helpful controls.

The Converter class provides a JFrame of a unit conversion program using the corresponding package in the library. Users can can select a unit from the two drop down menus and convert a value from one unit to the other. The frame also offers a way to interface with the string conversion capability of the package. For example entering the string:

    12 mph in kph
    
will automatically parse the string and use the preset list of units aliases to match a conversion. This gives an answer of:

    7.456304193410567
    
which is 12 miles per hour in kilometres per hour.

![Converter example](http://ryanharrison.co.uk/downloads/mathengine/converter.jpg)

The HistoricalTextField offers a custom text-field control which remembers all previous input. This emulates the likes of a terminal window where the arrow keys can be used to navigate a list of previous input. This control is then used in the MainFrame class.

The MainFrame class provides a graphical user interface that allows the user to use the parser package. Users can simply type in their expression and the parser will evaluate and return a result which will be displayed in the frame. All supported result types are displayed including fractions, vectors, matrices and functions.

![MainFrame example](http://ryanharrison.co.uk/downloads/mathengine/mainframe.jpg)

### Integral

The integral package provides multiple methods to numerically integrate functions between two points. The classes provide a numerical estimate of the integral and do not offer exact integrals. There therefore may be some error in the results.

To integrate a function, an object of the chosen IntegrationMethod should be instantiated, the lower and upper points of integration set, and the number of iterations to use specified. For example the TrapeziumIntegrator which sums the areas of trapezia under the function to estimate the integral. The more iterations, the more trapezia, and therefore the more accurate the result will be (for continuous functions):

    Function function = new Function("x^2 + 8*x + 12");
    TrapeziumIntegrator integrator = new TrapeziumIntegrator(function);
    integrator.setLower(0.5);
	integrator.setUpper(5);
	integrator.setIterations(100);
	double result = integrator.integrate();
        
    OUTPUT - 
    194.62651875000006
    
The exact integral of this function between 0.5 and 5 is:

    194.625

The SimpsonIntegrator, which fits the function to a quadratic equation, will tend to give the most accurate results.

### Linear Algebra

The linear algebra package contains classes that model Vectors and Matrices. Each class allows users to perform various common arithmetic and mathematical functions on the underlying data.

#### Vectors

To create a new Vector object, simply use one of the constructors which can take one value, a size of the vector, an array of values or even a string representing the vector of the form "{num1, num2, num3, numx}".

    double[] data = new double[] { 21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98 };
    Vector vector = new Vector(data);
    
Once an object has been created, the methods can be called that perform the various mathematical operations. For example:

    Vector result = vector.multiply(new Vector("{1,2,3,4,5,6,7,8}"));
    
    OUTPUT - 
    {21.05, 47.02, 72.69, 110.84, 154.3, 275.1, 364.84, 447.84}
    
    
    Vector result = new Vector("{8,3,4}").crossProduct(new Vector("{23,6,84}"));
    
    OUTPUT - 
    {228.0, -580.0, -21.0}

When performing arithmetic on Vectors with different sizes, the smaller vector fill be padded with zeroes until it is the same size as the larger Vector:

    Vector result = vector.add(new Vector("{1,2,3}"));
    
    OUTPUT - 
    {22.05, 25.51, 27.23, 27.71, 30.86, 45.85, 52.12, 55.98}

Note how the first three numbers were changed, however the rest remained unchanged. In fact behind the scenes zero is being added to each of the numbers.

#### Matrix

To create a new Matrix object, simply use one of the constructors which can take a range of values including the number of rows and columns, a unit vector or a two dimensional double array:

    double[][] data = new double[][] {{1,2,3}, {4,5,6}, {7,8,9}};
    Matrix matrix = new Matrix(data);
    
    OUTPUT - 
    1.0    2.0	3.0
    4.0    5.0	6.0
    7.0    8.0	9.0
    
Again just like with the Vector class, once an object is created, there are various methods that can be called to carry out mathematical operations on the Matrix:

    double[][] newData = new double[][] {{10, 11, 12}, {13,14,15}, {16,17,18}};
    Matrix result = matrix.multiply(new Matrix(newData));
    
    OUTPUT - 
    84.0    90.0	96.0
    201.0	216.0	231.0
    318.0	342.0	366.0
    
    double[][] data = new double[][] { { 3, 10, -1 }, { 8, -2, 5 }, { -7, 1, 3 } };
    Matrix inverse = new Matrix(data).inverse();
    
    OUTPUT - 
    0.017828200972447323    0.05024311183144247	-0.07779578606158834
    0.09562398703403564	-0.0032414910858995145	0.03727714748784441
    0.00972447325769854	0.11831442463533225	0.1393841166936791
    
The linear algebra package also includes classes to obtain a least squares solution for two matrices through the the QRDecomposition class, and a solution to the equation A*X = B where A and B are known and X is to be found:

    Matrix A = new Matrix(new double[][] { { 3, 7 }, { 4, 12 } });
	Matrix B = new Matrix(new double[][] { { -4, 5 }, { 8, 1 } });
	Matrix X = A.solve(B);
        
    OUTPUT - 
    -13.0   6.625
    5.0     -2.125
    
And to check that the solution is valid (returns the B matrix):

    A.multiply(X)
    
    OUTPUT - 
    -4.0    5.0
    8.0     1.0
    
### Parser

The parser package contains the expression parser and evaluator classes that are the main focus of this library as a whole. It is hoped that eventually all of other packages will be integrated into the parser through their own commands, which would allow users to access the functionality of the whole library by simply entering commands into the evaluator.

The main public interface to the parser is through the Evaluator class. An object can be created through one of its the static factory methods. Depending on which factory method is executed, different operators will be added to the Evaluator instance (allowing strings containing those operators to be successfully evaluated):

    // Creates an Evaluator with simple binary operators such as +,-,* etc
    Evaluator evaluator = Evaluator.newSimpleBinaryEvaluator();
    
Once an instance has been created, the evaluate methods can be called to parse and evaluate an input string into some kind of output. Methods that return a double/string are added as helpers which coerce all output into the corresponding type. The most flexible approach however is to use the .evaluateConstant(String) method. This returns a NodeConstant object which, depending on the evaluation, could be a double, rational, vector, function or a matrix. Here is an example of a simple evaluation which returns a NodeConstant:

    NodeConstant result = evaluator.evaluateConstant("(7 * -3) + (2 + (3 * -5))");
    
    OUTPUT - 
    -34
    
This is a simple example which returns a number. Custom variables can also be added to the Evaluator, and then used during evaluation:

    evaluator.addVariable("x", "3 + 4/3");
	result = evaluator.evaluateConstant("{1,2,3} + x");
    
    OUTPUT - 
    { 16/3, 19/3, 22/3 }
    
In this example the variable 'x' is being added, which takes a value of the result of '3 + 4/3'. The example also demonstrates a use of vectors during evaluations. Here the value of x is being added to each element of the vector. As when during calculation, both operands are rationals (the evaluation of x results in a rational and whole numbers are treated as rationals), the resulting vector also consists of rational number objects. This capability means that fractional information is not lost during evaluation. This also produces exact results to calculations with no loss of precision.

Using a different factory methods unlocks the use of different kinds of operators. With this example we can now use unary operators such as 'abs', 'sin', 'log' etc:

	evaluator = Evaluator.newSimpleEvaluator();
	result = evaluator.evaluateConstant("(abs(-5) - (6 / 5)) * (11 percent of 26)");

    OUTPUT -
    2717/250
    
In this example the abs function is used within the evaluation. In addition this example shows off a use of percentage functions. The evaluator has full support for percentages which allows for input strings such as '10% of 33' or even '11 as a % of 26' which returns a percentage object.

The parser package also has support for custom functions. These can be added by evaluating a special input string using the ':=' operator. Similarly this operator can be used to add other variables as mentioned above:

    evaluator = Evaluator.newEvaluator();
	evaluator.evaluateConstant("f(x) := x * 2 <= 8");
	evaluator.evaluateConstant("g(x) := x + 10");
	evaluator.evaluateConstant("v := {1,2,3,4,5,6}");
    
These functions and variables can then be used in future input strings:

    result = evaluator.evaluateConstant("(v where f) select g");
    
    OUTPUT - 
    { 11, 12, 13, 14 }
    
In this example the elements of the vector 'v', which was added above, has been limited to only remain if the function f return true when that element is passed as a parameter. In this case all elements which are less than or equal to 8 when multiplied by 2 are are kept, and all other elements are removed. This resulting vector is then passed into the select operator which translates all the elements through the function g. In this case all of the elements are increased by ten. This results in the final vector in the output.

This is just a small example of the capability of the parser. Other features include:

 - Logical expression including AND, OR, NOT etc
 - Matrix support with matrix operators such as determinant etc
 - Operators to find the roots of custom functions and find symbolic derivatives
 - Various operators for vector including reverse, sort, sum etc
 - Each operator can handle any kind of input object. For example when using the operator sum with a matrix, each row of the matrix will be converted to a vector, summed, and then added to a result vector. Similarly when passing a vector to a number operator such as sin, each element of the vector will be treated as an operand to the operator. As such in this case the sin of all vector elements will be found and returned as another vector.
 
To play around with the parser package and find out more about its capabilities, run the MainFrame class which provides a graphical user interface to the parser package. Users can simply type in their expression and the parser will evaluate and return a result which will then be displayed in the frame.

### Plotting

The plotting package includes a custom graphical control that plots a Function object onto a panel. The user can pan around the graph with the left mouse button held down, and can zoom in and out through the mouse wheel. It is hoped that this control will be extended further in the future to be able to plot multiple Function objects at the same time, whilst also providing more opportunity for graphical customisations.

The control makes extensive usage of the parser package - particularly the Evaluator class to evaluate the Function at different points for graphing.

[Click here to see the Plotter control in action](http://ryanharrison.co.uk/downloads/mathengine/grapher.jpg)

### Regression

The regression package includes classes to operate with different regression models given some sample data. These models return functions that are the functions of best fit. The format of these functions changes depending on the model used.

This package is being developed in parallel with my Regression tutorial series. For more information about this package see these current posts in the series which cover the creation of the classes:

 1. [Java Simple Regression Library Part 1 – Regression Models](http://ryanharrison.co.uk/2013/java-simple-regression-library-part-1-regression-models/)
 2. [Java Simple Regression Library Part 2 – Linear Regression Model](http://ryanharrison.co.uk/2013/java-simple-regression-library-part-2-linear-model/)

### Solvers

The solvers package provides various numerical root finding algorithms that aim to estimate the solutions to Function objects. To use the classes, simply create an object, passing in the Function object you wish to apply the algorithm to. You can then set the upper and lower bounds for the algorithm to search inside of, along with the number of iterations to use. The more iterations the more accurate your results will be, but the slower the algorithm will take to get there. Once these properties have been said, use the solve() method to run the algorithm and return an estimate of the root between the upper and lower bounds:

	Function function = new Function("x^2 + 8*x + 12");
	BrentSolver solver = new BrentSolver(function);
	solver.setUpperBound(-5);
	solver.setLowerBound(-10);
	solver.setIterations(25);
	double root = solver.solve();
	
    OUTPUT - 
    -6.0000008988963085

A ConvergenceCriteria property can also be set which determines the criteria by which the solver has converged onto a root. This can be either within a defined tolerance (which may not take the full number of iterations), or within iterations (which means that the algorithm must have converged within specified number of iterations).

Using the solve() method will return just one root within the specified upper and lower bound (if there is one). Each solver also provides a solveAll() method which attempts to find all of the roots within the specified bounds:

    BrentSolver solver = new BrentSolver(function);
	solver.setUpperBound(2);
	solver.setLowerBound(-8);
	solver.setIterations(25);
	List<Double> roots = solver.solveAll();
    
    OUTPUT - 
    [-5.999999999999998, -1.9999999999999973]

The roots of the function are at -6 and -2. As you can see the algorithms only provide estimates and do not always give precise results.

### Special

The special package offers access to evaluate many special functions including Gamma, Beta, Pi and the Error function:

    double gamma = Gamma.gamma(3.4);
    double beta = Beta.beta(3, 4);
    
    OUTPUT - 
    2.9812064265043796
    0.016666666653514156
    
The Primes class offers various utility methods to work with the prime numbers:

    boolean result = Primes.isPrime(263);
    double next = Primes.nextPrime(263);
	List<Long> factors = Primes.primeFactors(147);
    
    OUTPUT - 
    true
    269.0
    [3, 7, 7]

### Unit Conversion

The unit conversion package offers an interface to convert from one unit to another. What makes this package so different to all the rest is the way in which it handles aliases. You can simply pass in a unit as a string and the engine will do all the work for you. The great thing is that the input string can have multiple possibilities that all evaluate to the same unit. To do unit conversions, make a new instance of the ConversionEngine class and then call the convert methods. The conversion factors and aliases for each of the many units supported by the engine are stored in the unit.xml file.

    ConversionEngine engine = new ConversionEngine();
    String result = engine.convertToString(12, "mph", "kph");
    
    OUTPUT - 
    12.0 miles per hour = 7.4563042 kilometres per hour
    
The same result can be obtained using different strings for the units:

	String result = engine.convertToString(12, "miles per hour", "kilometer per hour");
    
    OUTPUT - 
    12.0 miles per hour = 7.4563042 kilometres per hour
    
The results string (various other results objects are available through different convert methods of the engine) will display the singular or plural version of the unit depending on the quantity.

The ConversionEngine also allows you to convert from one timezone to another (by passing in the city names). To allow this behaviour, first call the updateTimeZones() method:

    engine.updateTimeZones();
	String result = engine.convertToString(11, "london", "new york");
    
This will convert 11am in London to the time in New York. The result is:

    OUTPUT - 
    11.0 London = 600.0 New York
    
This means that 11am is 6am in New York.

The engine will automatically update the conversions depending on the time of year - taking into account changes in daylight savings etc. The data used is sourced from the [TimeZone Database](http://timezonedb.com/).

In addition the engine allows you to convert between currencies. By default the engine includes predefined conversion factors for the currencies, however the latest conversion rates can be downloaded by using the .updateCurrencies() method:

    engine.updateCurrencies();
	String result = engine.convertToString(38.5, "gbp", "usd");
    
    OUTPUT - 
    38.5 British Pounds Sterling = 59.7403081 United States Dollars
    
The currency data is sourced from both:

 - [Open Exchange Rates](https://openexchangerates.org/)
 - [European Central Bank](http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml)
 
### Other

The library also includes various utility classes that contain many helpful methods that are used throughout other parts of the library. These classes are located in the base library package.

#### MathUtils

The MathUtils class contains many mathematical methods that are extensions to the built in Math class. These consist mainly of additional trigonometric methods such as hyperbolic and inverse functions:

    double asech = MathUtils.asech(0.2);
	double cosh = MathUtils.cosh(1.2);
	double atanh = MathUtils.atanh(0.76);
    
    OUTPUT - 
    2.2924316695611777
    1.8106555673243747
    0.9962150823451031
    
The class also offers other non-trigonometric methods that may prove helpful:

    double round = MathUtils.round(15.67825, 3);
	long factorial = MathUtils.factorial(6);
	int gcd = MathUtils.gcd(72, 105);
	boolean isWithin = MathUtils.isEqualWithinPerCent(7, 10, 50);
	double combination = MathUtils.combination(8, 3);
    
    OUTPUT - 
    15.678
    720
    3
    true
    56
    
#### StatUtils

The StatUtils class offers various helpful statistical functions which are also used throughout the library:

    double[] data = {22.05, 25.51, 27.23, 27.71, 30.86, 45.85, 52.12, 55.98};
	double mean = StatUtils.mean(data);
	double median = StatUtils.median(data);
	double max = StatUtils.max(data);
	double stddev = StatUtils.standardDeviation(data);
	double iqr = StatUtils.percentile(data, 0.25);
	double kurtosis = StatUtils.kurtosis(data);
    
    OUTPUT - 
    35.91375
    29.285
    55.98
    13.270358954667136
    20.6175
    1.581398314485157
    
Even more helpful utility methods can be found in the Utils class. There is also an implementation of BigRational which models fractions in arbitrary precision. This class is used throughout the library in calculations so that no precision is lost when doing calculations (unlike when using simple doubles). This also enables the ability to return results as fractions (rationals). Which is a very helpful feature in the parser package.